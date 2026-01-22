package com.yesidrangel.dian.xml.validator.service.semantic;

import java.util.List;

import org.w3c.dom.Document;

public interface DianSemanticValidator {

    List<String> validate(Document xmlDocument, String documentType, String technicalKey);

}
