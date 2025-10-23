package com.sampoom.backend.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorStatus {

    // 400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", 10400),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.", 10401),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", 10406),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다.", 10404),

    // 409 CONFLICT
    CONFLICT(HttpStatus.CONFLICT, "충돌이 발생했습니다.", 10409),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", 10500),



    // 대리점
    AGENCY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 대리점을 찾을 수 없습니다.", 60100),

    // 직원
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "직원을 찾을 수 없습니다.", 60110),
    EMPLOYEE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 직원입니다.", 60111),
    EMPLOYEE_FORBIDDEN(HttpStatus.FORBIDDEN, "직원 관리 권한이 없습니다.", 60112),
    EMPLOYEE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "직원 정보에 접근할 수 없습니다.", 60113),

    // 장바구니
    AGENCY_CART_MISMATCH(HttpStatus.BAD_REQUEST, "해당 대리점의 장바구니 항목이 아닙니다.", 60120),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 항목을 찾을 수 없습니다.", 60121),
    CART_EMPTY(HttpStatus.BAD_REQUEST, "장바구니가 비어 있습니다.", 60122),


    // 주문
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다.", 61100),
    ORDER_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다.", 61101),
    ORDER_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 완료된 주문입니다.", 61102),
    ORDER_STATUS_INVALID(HttpStatus.BAD_REQUEST, "주문 상태가 잘못 되었습니다.", 61103),

    // 출고
    OUTBOUND_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "출고 항목을 찾을 수 없습니다.", 62100),

    // 부품
    PART_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 부품을 찾을 수 없습니다.", 63100),
    STOCK_INSUFFICIENT(HttpStatus.BAD_REQUEST, "재고 수량이 부족합니다.", 63110),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "수량은 0보다 커야 합니다.", 63111),
    STOCK_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 대리점 재고 항목입니다.", 63112),

    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;

    public int getStatusCode() {
        return this.httpStatus.value();
    }

}
