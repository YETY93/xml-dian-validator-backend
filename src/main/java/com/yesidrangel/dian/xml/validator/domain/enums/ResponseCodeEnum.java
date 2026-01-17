package com.yesidrangel.dian.xml.validator.domain.enums;

@Getter
public enum ResponseCodeEnum {

    SUCCESS("200", "Operación exitosa"),
    VALIDATION_ERROR("400", "Error de validación"),
    NOT_FOUND("404", "Recurso no encontrado"),
    INTERNAL_ERROR("500", "Error interno del servidor");

    private final String code;
    private final String message;

    ResponseCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}