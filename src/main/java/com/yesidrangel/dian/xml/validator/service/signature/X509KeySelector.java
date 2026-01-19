package com.yesidrangel.dian.xml.validator.service.signature;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import java.io.ByteArrayInputStream;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class X509KeySelector extends KeySelector {

    @Override
    public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose,
                                    AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException {
        if (keyInfo == null) {
            throw new KeySelectorException("KeyInfo es nulo");
        }
        for (Object obj : keyInfo.getContent()) {
            if (obj instanceof X509Data x509Data) {
                for (Object data : x509Data.getContent()) {
                    if (data instanceof byte[] certBytes) {
                        try {
                            CertificateFactory cf = CertificateFactory.getInstance("X.509");
                            X509Certificate cert = (X509Certificate) cf.generateCertificate(
                                    new ByteArrayInputStream(certBytes)
                            );
                            return new SimpleKeySelectorResult(cert.getPublicKey());
                        } catch (Exception e) {
                            throw new KeySelectorException("Error al procesar certificado", e);
                        }
                    }
                }
            }
        }
        throw new KeySelectorException("No se encontró certificado X509 en la firma");
    }

    private static class SimpleKeySelectorResult implements KeySelectorResult {
        private final PublicKey publicKey;
        SimpleKeySelectorResult(PublicKey publicKey) {
            this.publicKey = publicKey;
        }
        @Override
        public Key getKey() {
            return publicKey;
        }
    }
}
