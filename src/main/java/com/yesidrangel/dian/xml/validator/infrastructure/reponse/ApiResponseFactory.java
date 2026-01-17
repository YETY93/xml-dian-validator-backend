package com.yesidrangel.dian.xml.validator.infrastructure.reponse;

import com.yesidrangel.dian.xml.validator.domain.dto.ApiResponseDto;
import com.yesidrangel.dian.xml.validator.domain.enums.ResponseCodeEnum;

public class ApiResponseFactory {

	public static <T> ApiResponseDto<T> success(String action, T data) {
		return ApiResponseDto.<T>builder().status("SUCCESS").success(true).code(ResponseCodeEnum.SUCCESS.getCode())
				.action(action).lastAction(ResponseCodeEnum.SUCCESS.getMessage()).data(data).build();
	}

	public static ApiResponseDto<Void> error(String action, ResponseCodeEnum code, String message) {
		return ApiResponseDto.<Void>builder().status(code.name()).success(false).action(action).lastAction(message)
				.build();
	}

	public static ApiResponseDto<Void> badRequest(String message) {
		return error("BAD_REQUEST", ResponseCodeEnum.VALIDATION_ERROR, message);
	}

}
