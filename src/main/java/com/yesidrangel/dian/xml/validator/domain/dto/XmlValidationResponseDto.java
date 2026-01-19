package com.yesidrangel.dian.xml.validator.domain.dto;

import java.util.List;

import com.yesidrangel.dian.xml.validator.domain.enums.DianSeverityLevel;
import lombok.Data;

@Data
public class XmlValidationResponseDto {

	private boolean valid;
	private List<String> errors;
	private DianSeverityLevel maxSeverity;

}
