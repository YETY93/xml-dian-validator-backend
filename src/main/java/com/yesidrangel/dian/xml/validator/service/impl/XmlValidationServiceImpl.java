package com.yesidrangel.dian.xml.validator.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yesidrangel.dian.xml.validator.domain.dto.XmlValidationRequestDto;
import com.yesidrangel.dian.xml.validator.domain.dto.XmlValidationResponseDto;
import com.yesidrangel.dian.xml.validator.domain.enums.DianSchemaType;
import com.yesidrangel.dian.xml.validator.service.XmlValidationService;
import com.yesidrangel.dian.xml.validator.util.XsdValidationUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class XmlValidationServiceImpl implements XmlValidationService {

	@Override
	public XmlValidationResponseDto validate(XmlValidationRequestDto requestDto) {
		log.info("Validando documento {}", requestDto.getDocumentType());
		String xsdPath = resolveXsd(requestDto.getDocumentType());
		List<String> errors = XsdValidationUtil.validate(requestDto.getXml(), xsdPath);
		if (!errors.isEmpty()) {
			log.warn("Errores DIAN detectados: {}", errors);
		}
		XmlValidationResponseDto responseDto = new XmlValidationResponseDto();
		responseDto.setValid(errors.isEmpty());
		responseDto.setErrors(errors);
		return responseDto;
	}

	private String resolveXsd(String documentType) {
		return DianSchemaType.forName(documentType).map(DianSchemaType::getXsdPath)
				.orElseThrow(() -> new IllegalArgumentException("Tipo de documento no soportado: " + documentType));
	}

}
