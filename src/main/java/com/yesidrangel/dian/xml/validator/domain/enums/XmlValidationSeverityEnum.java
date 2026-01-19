package com.yesidrangel.dian.xml.validator.domain.enums;

import lombok.Getter;

@Getter
public enum XmlValidationSeverityEnum {

	WARNING("[WARNING]"), ERROR("[ERROR]"), FATAL("[FATAL]");

	private final String label;

	XmlValidationSeverityEnum(String label) {
		this.label = label;
	}

}
