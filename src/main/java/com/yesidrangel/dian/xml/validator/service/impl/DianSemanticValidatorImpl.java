package com.yesidrangel.dian.xml.validator.service.impl;

import com.yesidrangel.dian.xml.validator.service.semantic.DianSemanticValidator;
import com.yesidrangel.dian.xml.validator.util.CufeGeneratorUtil;
import com.yesidrangel.dian.xml.validator.util.DianTaxExtractorUtil;
import com.yesidrangel.dian.xml.validator.util.XmlXPathUtil;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yesidrangel.dian.xml.validator.util.XmlXPathUtil.getTag;

@Component
public class DianSemanticValidatorImpl implements DianSemanticValidator {

    @Override
    public List<String> validate(Document doc, String documentType, String technicalKey) {
        List<String> errors = new ArrayList<>();

        if ("INVOICE".equalsIgnoreCase(documentType)) {
            validateInvoice(doc, errors);
            validateCufe(doc, errors, technicalKey);
        }
        return errors;
    }

    private void validateInvoice(Document doc, List<String> errors) {
        // Regla 1: Invoice.cbc:ID obligatorio
        NodeList idNodes = doc.getElementsByTagName("cbc:ID");
        if (idNodes.getLength() == 0 || idNodes.item(0).getTextContent().isBlank()) {
            errors.add("ERROR: Invoice ID es obligatorio");
        }

        // Regla 2: IssueDate no puede ser futura
        NodeList dateNodes = doc.getElementsByTagName("cbc:IssueDate");
        if (dateNodes.getLength() > 0) {
            LocalDate issueDate = LocalDate.parse(dateNodes.item(0).getTextContent());

            if (issueDate.isAfter(LocalDate.now())) {
                errors.add("ERROR: IssueDate no puede ser una fecha futura");
            }
        }

        // Regla 3: NIT del emisor obligatorio
        NodeList supplierNodes = doc.getElementsByTagName("cbc:CompanyID");
        if (supplierNodes.getLength() == 0 || supplierNodes.item(0).getTextContent().isBlank()) {
            errors.add("ERROR: NIT del emisor es obligatorio");
        }
    }

    private void validateCufe(Document doc, List<String> errors, String technicalKey) {
        // 1. Validar presencia del CUFE en el XML
        String cufeXml = XmlXPathUtil.getTag(doc, "/Invoice/cbc:UUID");
        if (cufeXml == null || cufeXml.isBlank()) {
            errors.add("ERROR: El CUFE es obligatorio");
            return;
        }
        // 2. Validar clave técnica
        if (technicalKey == null || technicalKey.isBlank()) {
            errors.add("WARNING: No se pudo validar CUFE completo (clave técnica no enviada)");
            return;
        }
        // 3. Extraer campos básicos
        String invoiceNumber = XmlXPathUtil.getTag(doc, "/Invoice/cbc:ID");
        String issueDate = XmlXPathUtil.getTag(doc, "/Invoice/cbc:IssueDate");
        String issueTime = XmlXPathUtil.getTag(doc, "/Invoice/cbc:IssueTime");
        String totalGross = XmlXPathUtil.getTag(doc, "/Invoice/cac:LegalMonetaryTotal/cbc:LineExtensionAmount");
        String totalPayable = XmlXPathUtil.getTag(doc, "/Invoice/cac:LegalMonetaryTotal/cbc:PayableAmount");
        String supplierNit = XmlXPathUtil.getTag(doc,
                "/Invoice/cac:AccountingSupplierParty/cac:Party/cac:PartyTaxScheme/cbc:CompanyID");
        String customerNit = XmlXPathUtil.getTag(doc,
                "/Invoice/cac:AccountingCustomerParty/cac:Party/cac:PartyTaxScheme/cbc:CompanyID");
        String environment = XmlXPathUtil.getTag(doc, "/Invoice/cbc:ProfileExecutionID");
        // 4. Extraer impuestos (01=IVA, 04=INC, 03=ICA)
        Map<String, String> taxes = DianTaxExtractorUtil.extractTaxesByCode(doc);
        String valImp1 = taxes.get("01"); // IVA
        String valImp2 = taxes.get("04"); // INC
        String valImp3 = taxes.get("03"); // ICA
        // 5. Construir la cadena EXACTA según la DIAN v1.9
        String raw = invoiceNumber +
                issueDate +
                issueTime +
                totalGross +
                "01" + valImp1 +
                "04" + valImp2 +
                "03" + valImp3 +
                totalPayable +
                supplierNit +
                customerNit +
                technicalKey +
                environment;

        // 6. Generar y comparar
        String generatedCufe = CufeGeneratorUtil.sha384(raw);

        if (!generatedCufe.equalsIgnoreCase(cufeXml)) {
            errors.add("ERROR: El CUFE no coincide con el calculado por DIAN. " +
                    "Esperado: " + generatedCufe + ", Recibido: " + cufeXml);
        }
    }

}
