package com.yesidrangel.dian.xml.validator.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DianSeverityLevel {
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR"),
    FATAL("FATAL");

    private final String label;
}
