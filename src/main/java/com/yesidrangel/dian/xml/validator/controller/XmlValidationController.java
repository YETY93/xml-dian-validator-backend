package com.yesidrangel.dian.xml.validator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yesidrangel.dian.xml.validator.domain.dto.ApiResponseDto;
import com.yesidrangel.dian.xml.validator.domain.dto.XmlValidationRequestDto;
import com.yesidrangel.dian.xml.validator.domain.dto.XmlValidationResponseDto;
import com.yesidrangel.dian.xml.validator.infrastructure.reponse.ApiResponseFactory;
import com.yesidrangel.dian.xml.validator.service.XmlValidationService;

@RestController
@RequestMapping("/api/xml")
public class XmlValidationController {
	public static final String XML_VALIDATION = "XML_VALIDATION";

	private final XmlValidationService xmlService;

	public XmlValidationController(XmlValidationService xmlService) {
		this.xmlService = xmlService;
	}

	@PostMapping("/validate")
	public ResponseEntity<ApiResponseDto<XmlValidationResponseDto>> validate(
			@RequestBody XmlValidationRequestDto request) {
		XmlValidationResponseDto result = xmlService.validate(request);
		return ResponseEntity.ok(
				ApiResponseFactory.success(XML_VALIDATION, result)
		);
	}
}
