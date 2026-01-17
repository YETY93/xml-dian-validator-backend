package com.yesidrangel.dian.xml.validator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto<T> {

	private boolean success; // true | false
	private String status; // SUCCESS | ERROR
	private String code; // HTTP code
	private String action; // Qué se ejecutó
	private String lastAction; // Mensaje para el usuario
	private T data; // Payload real

}
