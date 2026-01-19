package com.yesidrangel.dian.xml.validator.infrastructure.reponse;

import com.yesidrangel.dian.xml.validator.domain.dto.ApiResponseDto;
import com.yesidrangel.dian.xml.validator.domain.enums.ResponseCodeEnum;

public class ApiResponseFactory {
		public static <T> ApiResponseDto<T> success(String action, T data) {
			return ApiResponseDto.<T>builder()
					.status("SUCCESS")
					.success(true)
					.code(ResponseCodeEnum.SUCCESS.getCode())
					.action(action)
					.lastAction(ResponseCodeEnum.SUCCESS.getMessage())
					.data(data)
					.build();
		}

		public static ApiResponseDto<Void> badRequest(String message) {
			return ApiResponseDto.<Void>builder()
					.status("ERROR")
					.success(false)
					.code(ResponseCodeEnum.VALIDATION_ERROR.getCode())
					.action("VALIDATION_ERROR")
					.lastAction(message)
					.build();
		}

		public static ApiResponseDto<Void> internalError(String message) {
			return ApiResponseDto.<Void>builder()
					.status("ERROR")
					.success(false)
					.code(ResponseCodeEnum.INTERNAL_ERROR.getCode())
					.action("INTERNAL_ERROR")
					.lastAction(message)
					.build();
		}
}
