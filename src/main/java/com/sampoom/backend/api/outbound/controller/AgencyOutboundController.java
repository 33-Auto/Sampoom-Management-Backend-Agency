package com.sampoom.backend.api.outbound.controller;

import com.sampoom.backend.api.outbound.dto.AgencyOutboundRequestDTO;
import com.sampoom.backend.api.outbound.dto.AgencyOutboundUpdateRequestDTO;
import com.sampoom.backend.api.outbound.service.AgencyOutboundService;
import com.sampoom.backend.common.dto.CategoryResponseDTO;
import com.sampoom.backend.common.response.ApiResponse;
import com.sampoom.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Outbound", description = "대리점 부품 출고 API")
@RestController
@RequestMapping("/{agencyId}/outbound")
@RequiredArgsConstructor
public class AgencyOutboundController {

    private final AgencyOutboundService outboundService;

    @Operation(summary = "출고 목록에 부품 추가")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addItem(
            @PathVariable Long agencyId,
            @Valid @RequestBody AgencyOutboundRequestDTO request) {
        outboundService.addItem(agencyId, request);

        return ApiResponse.success(SuccessStatus.OUTBOUND_ADD_SUCCESS, null);
    }

    @Operation(summary = "출고 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getItems(
            @PathVariable Long agencyId) {
        List<CategoryResponseDTO> list = outboundService.getOutboundItems(agencyId);

        return ApiResponse.success(SuccessStatus.OUTBOUND_LIST_SUCCESS, list);
    }

    @Operation(summary = "출고 수량 변경")
    @PatchMapping("/{outboundId}")
    public ResponseEntity<ApiResponse<Void>> updateQuantity(
            @PathVariable Long agencyId,
            @PathVariable Long outboundId,
            @RequestBody @Valid AgencyOutboundUpdateRequestDTO request) {
        outboundService.updateQuantity(agencyId, outboundId, request.getQuantity());
        return ApiResponse.success(SuccessStatus.OUTBOUND_UPDATE_SUCCESS, null);
    }

    @Operation(summary = "출고 항목 삭제")
    @DeleteMapping("/{outboundId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable Long agencyId,
            @PathVariable Long outboundId) {
        outboundService.deleteItem(agencyId, outboundId);
        return ApiResponse.success(SuccessStatus.OUTBOUND_DELETE_SUCCESS, null);
    }

    @Operation(summary = "출고 처리")
    @PostMapping("/process")
    public ResponseEntity<ApiResponse<Void>> processOutbound(@PathVariable Long agencyId) {
        outboundService.processOutbound(agencyId);
        return ApiResponse.success(SuccessStatus.OUTBOUND_PROCESS_SUCCESS, null);
    }
}