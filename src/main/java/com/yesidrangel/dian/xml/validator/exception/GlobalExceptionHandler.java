package com.yesidrangel.dian.xml.validator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yesidrangel.dian.xml.validator.domain.dto.ApiResponseDto;
import com.yesidrangel.dian.xml.validator.infrastructure.reponse.ApiResponseFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponseDto handleIllegalArgument(IllegalArgumentException ex) {
		return ApiResponseFactory.badRequest(ex.getMessage());
	}

	// Manejo de errores técnicos → 500 Internal Server Error
	@ExceptionHandler(TechnicalException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponseDto<Void> handleTechnicalException(TechnicalException ex) {
		log.error("Error técnico inesperado", ex);
		return ApiResponseFactory.internalError("Error interno del servidor");
	}

	// Manejo de errores funcionales → 400 Bad Request
	@ExceptionHandler(FunctionalException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponseDto<Void> handleFunctionalException(FunctionalException ex) {
		return ApiResponseFactory.badRequest(ex.getMessage());
	}

}
