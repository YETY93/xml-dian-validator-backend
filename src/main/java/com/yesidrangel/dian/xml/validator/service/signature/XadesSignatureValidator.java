package com.yesidrangel.dian.xml.validator.service.signature;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yesidrangel.dian.xml.validator.util.XmlXPathUtil;

@Component
public class XadesSignatureValidator {
    private static final String DIAN_POLICY_URL =
            "https://facturaelectronica.dian.gov.co/politicadefirma/v2/politicadefirmav2.pdf";
    private static final List<String> TRUSTED_ISSUERS = List.of(
            "ORGANISMO NACIONAL DE ACREDITACION DE COLOMBIA",
            "CERTICAMARA",
            "ANDES SCD",
            "GSE",
            "ECOLOMBIA",
            "GLOBALSIGN",
            "SUCERED"
    );

    public List<String> validate(Document doc) {
        List<String> errors = new ArrayList<>();
        NodeList sigList = XmlXPathUtil.getNodes(doc, "//ds:Signature");
        if (sigList.getLength() != 1) {
            errors.add("ERROR: Se esperaba exactamente una firma digital (ds:Signature)");
            return errors;
        }
        Node signatureNode = sigList.item(0);
        try {
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
            DOMValidateContext valContext = new DOMValidateContext(new X509KeySelector(), signatureNode);
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);
            boolean valid = signature.validate(valContext);
            if (!valid) {
                errors.add("ERROR: La firma digital es inválida");
            }
            X509Certificate cert = extractCertificate(signatureNode);
            if (cert == null) {
                errors.add("ERROR: El certificado X509 es obligatorio");
            } else {
                validateCertificate(cert, errors);
                validateCertificateIssuer(cert, errors);
                // Validaciones XAdES adicionales
                validateSignaturePolicy(doc, errors);
                validateSigningTime(doc, errors);
                validateSigningRole(doc, errors);
                validateAlgorithms(doc, errors);
            }
        } catch (Exception e) {
            errors.add("ERROR: No se pudo validar la firma digital: " + e.getMessage());
        }
        return errors;
    }

    private X509Certificate extractCertificate(Node signatureNode) throws Exception {
        String certStr = XmlXPathUtil.getTag(signatureNode.getOwnerDocument(),
                "//ds:X509Certificate");
        if (certStr == null || certStr.isBlank()) {
            return null;
        }
        byte[] certBytes = Base64.getDecoder().decode(certStr);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
    }

    private void validateCertificate(X509Certificate cert, List<String> errors) {
        try {
            cert.checkValidity();
        } catch (Exception e) {
            errors.add("ERROR: Certificado vencido o no válido aún");
        }
        boolean[] usage = cert.getKeyUsage();
        if (usage == null || !usage[1]) {
            errors.add("ERROR: El certificado no tiene uso de no repudio habilitado");
        }
        String sigAlg = cert.getSigAlgName();
        if (!sigAlg.contains("SHA")) {
            errors.add("ERROR: Algoritmo de firma no soportado por DIAN: " + sigAlg);
        }
    }

    private void validateSignaturePolicy(Document doc, List<String> errors) {
        String policyId = XmlXPathUtil.getTag(doc,
                "//xades:SigPolicyId/xades:Identifier");

        if (policyId == null || policyId.isBlank()) {
            errors.add("ERROR: No se encontró SignaturePolicyIdentifier (obligatorio DIAN)");
            return;
        }

        if (!DIAN_POLICY_URL.equals(policyId.trim())) {
            errors.add("ERROR: Política de firma DIAN inválida: " + policyId);
        }
    }

    private void validateSigningTime(Document doc, List<String> errors) {
        String signingTime = XmlXPathUtil.getTag(doc, "//xades:SigningTime");
        if (signingTime == null || signingTime.isBlank()) {
            errors.add("ERROR: SigningTime es obligatorio en la firma XAdES");
            return;
        }
        try {
            OffsetDateTime.parse(signingTime);
        } catch (Exception e) {
            errors.add("ERROR: SigningTime tiene formato inválido: " + signingTime);
        }
    }

    private void validateSigningRole(Document doc, List<String> errors) {
        String role = XmlXPathUtil.getTag(doc,
                "//xades:SignerRole/xades:ClaimedRoles/xades:ClaimedRole");
        if (role == null || role.isBlank()) {
            errors.add("ERROR: SigningRole es obligatorio (Supplier o Third party)");
            return;
        }
        if (!role.equalsIgnoreCase("supplier") &&
                !role.equalsIgnoreCase("third party")) {
            errors.add("ERROR: SigningRole inválido: " + role);
        }
    }

    private void validateCertificateIssuer(X509Certificate cert, List<String> errors) {
        if (cert == null) {
            return;
        }
        String issuer = cert.getIssuerX500Principal().getName();
        boolean trusted = TRUSTED_ISSUERS.stream()
                .anyMatch(issuer::contains);
        if (!trusted) {
            errors.add("WARNING: El emisor del certificado no está en la lista ONAC conocida: " + issuer);
        }
    }

    private void validateAlgorithms(Document doc, List<String> errors) {
        String digestAlg = XmlXPathUtil.getTag(doc, "//ds:Reference/ds:DigestMethod/@Algorithm");
        String sigAlg = XmlXPathUtil.getTag(doc, "//ds:SignatureMethod/@Algorithm");
        List<String> allowedDigests = List.of(
                "http://www.w3.org/2001/04/xmlenc#sha256",
                "http://www.w3.org/2001/04/xmldsig-more#sha384",
                "http://www.w3.org/2001/04/xmlenc#sha512"
        );
        if (!allowedDigests.contains(digestAlg)) {
            errors.add("ERROR: Algoritmo de resumen no soportado: " + digestAlg);
        }
        List<String> allowedSigs = List.of(
                "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256",
                "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384",
                "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512"
        );
        if (!allowedSigs.contains(sigAlg)) {
            errors.add("ERROR: Algoritmo de firma no soportado: " + sigAlg);
        }
    }

}
