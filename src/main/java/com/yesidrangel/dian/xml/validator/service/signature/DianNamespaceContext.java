package com.yesidrangel.dian.xml.validator.service.signature;

import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

public class DianNamespaceContext implements NamespaceContext {

    private static final Map<String, String> NS = Map.of(
            "cbc", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2",
            "cac", "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2",
            "ext", "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2",
            "ds",  "http://www.w3.org/2000/09/xmldsig#"
    );

    @Override
    public String getNamespaceURI(String prefix) {
        return NS.get(prefix);
    }

    @Override
    public String getPrefix(String namespaceURI) { return null; }

    @Override
    public Iterator<String> getPrefixes(String namespaceURI) { return null; }

}
