package com.yesidrangel.dian.xml.validator.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class XmlValidationResponseDto {

	private boolean valid;
	private List<String> errors;
}
