package com.yesidrangel.dian.xml.validator.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.yesidrangel.dian.xml.validator.domain.dto.XmlValidationRequestDto;
import com.yesidrangel.dian.xml.validator.domain.dto.XmlValidationResponseDto;
import com.yesidrangel.dian.xml.validator.domain.enums.DianSchemaType;
import com.yesidrangel.dian.xml.validator.domain.enums.DianSeverityLevel;
import com.yesidrangel.dian.xml.validator.exception.FunctionalException;
import com.yesidrangel.dian.xml.validator.service.XmlValidationService;
import com.yesidrangel.dian.xml.validator.service.semantic.DianSemanticValidator;
import com.yesidrangel.dian.xml.validator.service.signature.XadesSignatureValidator;
import com.yesidrangel.dian.xml.validator.util.XmlParserUtil;
import com.yesidrangel.dian.xml.validator.util.XsdValidationUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class XmlValidationServiceImpl implements XmlValidationService {

	@Autowired
	private DianSemanticValidator semanticValidator;
	@Autowired
	private XadesSignatureValidator signatureValidator;

	@Override
	public XmlValidationResponseDto validate(XmlValidationRequestDto requestDto) {
		log.info("Validando documento {}", requestDto.getDocumentType());
		validateBase(requestDto);
		String xsdPath = resolveXsd(requestDto.getDocumentType());
		// 🔹 1. Validación estructural XSD
		List<String> xsdErrors = XsdValidationUtil.validate(requestDto.getXml(), xsdPath);
		// 🔹 2. Parseo del XML
		Document xmlDocument = XmlParserUtil.parse(requestDto.getXml());
		// 🔹 3. Validación semántica DIAN
		List<String> semanticErrors = semanticValidator.validate(xmlDocument, requestDto.getDocumentType(), requestDto.getTechnicalKey());
		// 🔹 4. Validación errores certificado
		List<String> signatureErrors = signatureValidator.validate(xmlDocument);
		// 🔹 5. Combinar errores
		List<String> allErrors = new ArrayList<>();
		allErrors.addAll(xsdErrors);
		allErrors.addAll(semanticErrors);
		allErrors.addAll(signatureErrors);
		if (!allErrors.isEmpty()) {
			log.warn("Errores DIAN detectados: {}", allErrors);
		}
		// 🔹 5. Construir respuesta
		XmlValidationResponseDto responseDto = new XmlValidationResponseDto();
		responseDto.setValid(allErrors.isEmpty());
		responseDto.setErrors(allErrors);
		responseDto.setMaxSeverity(detectSeverity(allErrors));
		return responseDto;
	}

	private String resolveXsd(String documentType) {
		return DianSchemaType.forName(documentType).map(DianSchemaType::getXsdPath)
				.orElseThrow(() -> new IllegalArgumentException("Tipo de documento no soportado: " + documentType));
	}

	private void validateBase(XmlValidationRequestDto requestDto){
		if (requestDto.getXml() == null || requestDto.getXml().isBlank()) {
			throw new FunctionalException("El XML es obligatorio");
		}
		if (requestDto.getDocumentType() == null || requestDto.getDocumentType().isBlank()) {
			throw new FunctionalException("El tipo de documento es obligatorio");
		}
	}

	private DianSeverityLevel detectSeverity(List<String> errors) {
		DianSeverityLevel[] levels = DianSeverityLevel.values();
		for (int i = levels.length - 1; i >= 0; i--) {
			DianSeverityLevel level = levels[i];
			if (level == DianSeverityLevel.INFO) {
				continue;
			}
			boolean found = errors.stream()
					.anyMatch(e -> e.startsWith(level.getLabel()));
			if (found) {
				return level;
			}
		}
		return DianSeverityLevel.INFO;
	}

}
