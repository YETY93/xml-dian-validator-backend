package com.yesidrangel.dian.xml.validator.service.semantic;

import org.w3c.dom.Document;

import java.util.List;

public interface DianSemanticValidator {

    List<String> validate(Document xmlDocument, String documentType, String technicalKey);

}
