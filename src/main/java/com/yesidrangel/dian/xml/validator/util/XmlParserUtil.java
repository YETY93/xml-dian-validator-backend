package com.yesidrangel.dian.xml.validator.util;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.yesidrangel.dian.xml.validator.exception.TechnicalException;

public class XmlParserUtil {

    public static Document parse(String xml){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            return factory.newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new TechnicalException("Error parseando XML", e);
        }
    }
}
