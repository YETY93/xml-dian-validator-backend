package com.yesidrangel.dian.xml.validator.util;

import com.yesidrangel.dian.xml.validator.service.signature.DianNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class XmlXPathUtil {
    public static String getTag(Document doc, String expression) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            String value = (String) xpath.evaluate(expression, doc, XPathConstants.STRING);
            return value != null ? value.trim() : "";
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo XPath: " + expression, e);
        }
    }

    public static NodeList getNodes(Document doc, String expression) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new DianNamespaceContext());
            return (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
        } catch (Exception e) {
            throw new RuntimeException("Error evaluando XPath: " + expression, e);
        }
    }
}
