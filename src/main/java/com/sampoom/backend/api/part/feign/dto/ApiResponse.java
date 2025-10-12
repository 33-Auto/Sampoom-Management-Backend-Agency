package com.sampoom.backend.api.part.feign.dto;

import lombok.Getter;
import lombok.Setter;

// Part 서버 공통 응답 포맷 매핑용
@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;
}
