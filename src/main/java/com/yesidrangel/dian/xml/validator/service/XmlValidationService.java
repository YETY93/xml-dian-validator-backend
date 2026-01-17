package com.yesidrangel.dian.xml.validator.service;

import com.yesidrangel.dian.xml.validator.domain.dto.XmlValidationRequestDto;
import com.yesidrangel.dian.xml.validator.domain.dto.XmlValidationResponseDto;

public interface XmlValidationService {

	XmlValidationResponseDto validate(XmlValidationRequestDto requestDto);
}
