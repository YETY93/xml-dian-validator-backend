package com.yesidrangel.dian.xml.validator.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DianTaxExtractorUtil {

    public static Map<String, String> extractTaxesByCode(Document doc) {
        Map<String, String> taxes = new HashMap<>();
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            // Obtener todos los nodos cac:TaxTotal
            NodeList taxTotals = (NodeList) xpath.evaluate(
                    "/Invoice/cac:TaxTotal",
                    doc,
                    XPathConstants.NODESET
            );
            for (int i = 0; i < taxTotals.getLength(); i++) {
                Node taxTotal = taxTotals.item(i);
                // Extraer código
                Object codeResult = xpath.evaluate(
                        "cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID",
                        taxTotal,
                        XPathConstants.STRING
                );
                String code = (codeResult instanceof String) ? ((String) codeResult).trim() : "";
                // Extraer monto
                Object amountResult = xpath.evaluate(
                        "cbc:TaxAmount",
                        taxTotal,
                        XPathConstants.STRING
                );
                String amount = (amountResult instanceof String) ? ((String) amountResult).trim() : "";
                if (!code.isEmpty()) {
                    // Si ya existe, sumar (aunque en DIAN normalmente no se repite)
                    if (taxes.containsKey(code)) {
                        double existing = Double.parseDouble(taxes.get(code));
                        double current = Double.parseDouble(normalizeAmount(amount));
                        taxes.put(code, String.format("%.2f", existing + current));
                    } else {
                        taxes.put(code, normalizeAmount(amount));
                    }
                }
            }
            // Asegurar que los códigos requeridos estén presentes (aunque sean 0.00)
            ensureTax(taxes, "01");
            ensureTax(taxes, "04");
            ensureTax(taxes, "03");

        } catch (Exception e) {
            throw new RuntimeException("Error extrayendo impuestos DIAN", e);
        }
        return taxes;
    }

    private static void ensureTax(Map<String, String> taxes, String code) {
        taxes.putIfAbsent(code, "0.00");
    }

    private static String normalizeAmount(String amount) {
        if (amount == null || amount.isBlank()) {
            return "0.00";
        }
        try {
            // Eliminar comas si existen (ej: "1,500.00" → "1500.00")
            String clean = amount.replace(",", "");
            double value = Double.parseDouble(clean);
            return String.format("%.2f", value);
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }
}