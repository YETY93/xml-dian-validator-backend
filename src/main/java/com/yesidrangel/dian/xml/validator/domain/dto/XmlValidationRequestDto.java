package com.yesidrangel.dian.xml.validator.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class XmlValidationRequestDto {

	private String xml;
	private String documentType;

}
