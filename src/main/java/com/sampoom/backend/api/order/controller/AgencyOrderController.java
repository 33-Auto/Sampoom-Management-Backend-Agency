package com.sampoom.backend.api.order.controller;

import com.sampoom.backend.api.order.dto.AgencyOrderResponseDTO;
import com.sampoom.backend.api.order.service.AgencyOrderService;
import com.sampoom.backend.common.response.ApiResponse;
import com.sampoom.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "대리점 주문 API")
@RestController
@RequestMapping("/{agencyId}/orders")
@RequiredArgsConstructor
public class AgencyOrderController {

    private final AgencyOrderService agencyOrderService;

    // 주문 생성 (장바구니 → 주문으로 전환)
    @Operation(summary = "주문 생성", description = "장바구니에 담긴 품목을 기반으로 새 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<AgencyOrderResponseDTO>> createOrder(
            @PathVariable Long agencyId
    ) {
        AgencyOrderResponseDTO order = agencyOrderService.createOrder(agencyId);
        return ApiResponse.success(SuccessStatus.ORDER_CREATE_SUCCESS, order);
    }

    // 대리점별 주문 목록 조회
    @Operation(summary = "주문 목록 조회", description = "대리점의 모든 주문 내역을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AgencyOrderResponseDTO>>> getOrders(
            @PathVariable Long agencyId
    ) {
        List<AgencyOrderResponseDTO> orders = agencyOrderService.getOrdersByAgency(agencyId);
        return ApiResponse.success(SuccessStatus.ORDER_LIST_SUCCESS, orders);
    }

    // 주문 상세 조회
    @Operation(summary = "주문 상세 조회", description = "특정 주문의 상세 정보를 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<AgencyOrderResponseDTO>> getOrderDetail(
            @PathVariable Long agencyId,
            @PathVariable Long orderId
    ) {
        AgencyOrderResponseDTO order = agencyOrderService.getOrderDetail(agencyId, orderId);
        return ApiResponse.success(SuccessStatus.ORDER_DETAIL_SUCCESS, order);
    }

    // 주문 입고 처리 (완료 처리)
    @Operation(summary = "주문 입고 처리", description = "주문 상태를 입고 완료(COMPLETED)로 변경합니다.")
    @PatchMapping("/{orderId}/receive")
    public ResponseEntity<ApiResponse<Void>> markOrderReceived(
            @PathVariable Long agencyId,
            @PathVariable Long orderId
    ) {
        agencyOrderService.markOrderReceived(agencyId, orderId);
        return ApiResponse.success(SuccessStatus.ORDER_RECEIVE_SUCCESS, null);
    }

    // 주문 취소
    @Operation(summary = "주문 취소", description = "특정 주문을 취소합니다.")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @PathVariable Long agencyId,
            @PathVariable Long orderId
    ) {
        agencyOrderService.cancelOrder(agencyId, orderId);
        return ApiResponse.success(SuccessStatus.ORDER_CANCEL_SUCCESS, null);
    }
}