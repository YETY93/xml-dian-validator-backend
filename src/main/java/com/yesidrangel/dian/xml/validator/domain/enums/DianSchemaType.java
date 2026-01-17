package com.yesidrangel.dian.xml.validator.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum DianSchemaType {

	INVOICE("INVOICE", "xsd/factura/maindoc/UBL-Invoice-2.1.xsd"),

	CREDIT_NOTE("CREDIT_NOTE", "xsd/factura/maindoc/UBL-CreditNote-2.1.xsd"),

	DOCUMENTO_SOPORTE("DOCUMENTO_SOPORTE", "xsd/documento-soporte/maindoc/UBL-Invoice-2.1.xsd");

	private final String code;
	private final String xsdPath;

	DianSchemaType(String code, String xsdPath) {
		this.code = code;
		this.xsdPath = xsdPath;
	}

	private static final Map<String, DianSchemaType> BY_NAME = Arrays.stream(values())
			.collect(Collectors.toUnmodifiableMap(e -> e.name().toUpperCase(), Function.identity()));

	/**
	 * Busca un valor del enum por su nombre (case-sensitive).
	 * 
	 * @param name
	 *            Nombre del enum (ej. "INVOICE")
	 * @return Optional con el enum si se encuentra, vacío si no.
	 */
	public static Optional<DianSchemaType> forName(String name) {
		return Optional.ofNullable(name == null ? null : BY_NAME.get(name.toUpperCase()));
	}

}
