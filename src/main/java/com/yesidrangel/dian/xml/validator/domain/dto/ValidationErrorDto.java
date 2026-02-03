package com.yesidrangel.dian.xml.validator.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationErrorDto {
    private String type;      // XSD | SEMANTIC | SIGNATURE
    private String severity;  // INFO | WARNING | ERROR
    private String message;
}
