package com.yesidrangel.dian.xml.validator.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum DocumentTypePathEnum {

    INVOICE("xsd/maindoc/UBL-Invoice-2.1.xsd"),
    CREDIT_NOTE("xsd/maindoc/UBL-CreditNote-2.1.xsd"),
    DEBIT_NOTE("xsd/maindoc/UBL-DebitNote-2.1.xsd");

    private final String xsdPath;

    DocumentTypePathEnum(String xsdPath) {
        this.xsdPath = xsdPath;
    }

    private static final Map<String, DocumentTypePathEnum> BY_NAME =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(
                            e -> e.name().toUpperCase(),
                            Function.identity()
                    ));

    /**
     * Busca un valor del enum por su nombre (case-sensitive).
     * @param name Nombre del enum (ej. "INVOICE")
     * @return Optional con el enum si se encuentra, vacío si no.
     */
    public static Optional<DocumentTypePathEnum> forName(String name) {
        return Optional.ofNullable(
                name == null ? null : BY_NAME.get(name.toUpperCase())
        );
    }

}
