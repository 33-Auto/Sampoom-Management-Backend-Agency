package com.sampoom.backend.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {

    // 공통 성공 메시지
    OK(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다."),

    // 재고 관련
    STOCK_LIST_SUCCESS(HttpStatus.OK, "재고 목록 조회 성공"),
    STOCK_INBOUND_SUCCESS(HttpStatus.OK, "재고 입고 처리 성공"),
    STOCK_OUTBOUND_SUCCESS(HttpStatus.OK, "재고 출고 처리 성공"),
    STOCK_ADJUST_SUCCESS(HttpStatus.OK, "재고 수량 조정 성공"),

    // 대리점 관련
    AGENCY_LIST_SUCCESS(HttpStatus.OK, "대리점 목록 조회 성공"),
    AGENCY_DETAIL_SUCCESS(HttpStatus.OK, "대리점 상세 조회 성공"),

    CART_ADD_SUCCESS(HttpStatus.OK,  "장바구니 담기 성공"),
    CART_LIST_SUCCESS(HttpStatus.OK, "장바구니 목록 조회 성공"),
    CART_UPDATE_SUCCESS(HttpStatus.OK, "장바구니 수량 수정 성공"),
    CART_DELETE_SUCCESS(HttpStatus.OK, "장바구니 항목 삭제 성공"),
    CART_CLEAR_SUCCESS(HttpStatus.OK, "장바구니 전체 비우기 성공"),

    CATEGORY_LIST_SUCCESS(HttpStatus.OK, "카테고리 목록 조회 성공"),
    GROUP_LIST_SUCCESS(HttpStatus.OK, "그룹 목록 조회 성공"),
    PART_LIST_SUCCESS(HttpStatus.OK, "부품 목록 조회 성공"),
    PART_SEARCH_SUCCESS(HttpStatus.OK, "부품 검색 성공"),
    ;




    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
