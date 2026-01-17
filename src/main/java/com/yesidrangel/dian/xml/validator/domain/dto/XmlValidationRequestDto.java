package com.yesidrangel.dian.xml.validator.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class XmlValidationRequest {

    private String xml;
    private String documentType;

}
