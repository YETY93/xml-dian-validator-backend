package com.yesidrangel.dian.xml.validator.service.signature;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
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
                validateCertificate(cert, errors); // 👈 aquí lo llamas
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

}
