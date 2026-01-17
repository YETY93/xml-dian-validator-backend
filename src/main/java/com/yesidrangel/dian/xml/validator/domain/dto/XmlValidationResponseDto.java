package com.yesidrangel.dian.xml.validator.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class XmlValidationResponseDto {

	private boolean valid;
	private List<String> errors;
}
