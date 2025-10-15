package com.sampoom.backend.api.cart.controller;

import com.sampoom.backend.api.cart.dto.AgencyCartRequestDTO;
import com.sampoom.backend.api.cart.dto.AgencyCartResponseDTO;
import com.sampoom.backend.api.cart.service.AgencyCartService;
import com.sampoom.backend.common.response.ApiResponse;
import com.sampoom.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart", description = "대리점 장바구니 API")
@RestController
@RequestMapping("/{agencyId}/cart")
@RequiredArgsConstructor
public class AgencyCartController {

    private final AgencyCartService agencyCartService;

    // 장바구니에 부품 추가
    @Operation(summary = "장바구니에 부품 추가", description = "대리점 장바구니에 새로운 부품을 추가합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addCartItem(
            @PathVariable Long agencyId,
            @Valid @RequestBody AgencyCartRequestDTO agencyCartRequestDTO
    ) {
        agencyCartService.addCartItem(agencyId, agencyCartRequestDTO);
        return ApiResponse.success(SuccessStatus.CART_ADD_SUCCESS, null);
    }

    // 장바구니 목록 조회
    @Operation(summary = "장바구니 목록 조회", description = "대리점의 장바구니에 담긴 부품 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AgencyCartResponseDTO>>> getCartItems(
            @PathVariable Long agencyId
    ) {
        List<AgencyCartResponseDTO> result = agencyCartService.getCartItems(agencyId);
        return ApiResponse.success(SuccessStatus.CART_LIST_SUCCESS, result);
    }

    // 장바구니 항목 수량 수정
    @Operation(summary = "장바구니 항목 수량 수정", description = "특정 장바구니 아이템의 수량을 변경합니다.")
    @PutMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> updateCartItem(
            @PathVariable Long agencyId,
            @PathVariable Long cartItemId,
            @Valid @RequestBody AgencyCartRequestDTO agencyCartRequestDTO
    ) {
        agencyCartService.updateCartItem(agencyId, cartItemId, agencyCartRequestDTO);
        return ApiResponse.success(SuccessStatus.CART_UPDATE_SUCCESS, null);
    }

    // 장바구니 항목 삭제
    @Operation(summary = "장바구니 항목 삭제", description = "특정 부품을 장바구니에서 제거합니다.")
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(
            @PathVariable Long agencyId,
            @PathVariable Long cartItemId
    ) {
        agencyCartService.deleteCartItem(agencyId, cartItemId);
        return ApiResponse.success(SuccessStatus.CART_DELETE_SUCCESS, null);
    }

    // 장바구니 전체 비우기
    @Operation(summary = "장바구니 전체 비우기", description = "대리점의 장바구니를 전체 비웁니다.")
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long agencyId) {
        agencyCartService.clearCart(agencyId);
        return ApiResponse.success(SuccessStatus.CART_CLEAR_SUCCESS, null);
    }
}
