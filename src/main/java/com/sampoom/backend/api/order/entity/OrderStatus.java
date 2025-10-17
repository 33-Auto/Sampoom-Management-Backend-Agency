package com.sampoom.backend.api.order.entity;

public enum OrderStatus {
    PENDING,     // 주문 요청 중
    CONFIRMED,     // 주문 승인
    COMPLETED,    // 배송 완료
    CANCELED     // 주문 취소
}