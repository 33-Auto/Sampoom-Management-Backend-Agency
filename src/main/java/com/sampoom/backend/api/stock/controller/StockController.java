package com.sampoom.backend.api.stock.controller;

import com.sampoom.backend.api.stock.dto.DashboardResponseDTO;
import com.sampoom.backend.api.stock.dto.PartUpdateRequestDTO;
import com.sampoom.backend.api.stock.service.StockService;
import com.sampoom.backend.common.response.ApiResponse;
import com.sampoom.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Stock", description = "대리점 전용 재고 API")
@RestController
@RequestMapping("/{agencyId}")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(summary = "대리점 주문 입고 처리", description = "대리점이 주문한 부품 입고 처리")
    @PatchMapping("/stock")
    public ResponseEntity<ApiResponse<Void>> stockingParts(
            @PathVariable Long agencyId,
            @Valid @RequestBody PartUpdateRequestDTO partUpdateRequestDTO) {

        // 여러 품목에 대한 일괄 입고 처리
        stockService.processOrderReceiving(agencyId, partUpdateRequestDTO);

        return ApiResponse.success_only(SuccessStatus.OK);
    }

    @Operation(summary = "대리점 대시보드 조회", description = "대리점의 재고 현황을 요약한 대시보드 정보를 조회합니다")
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponseDTO>> getDashboard(@PathVariable Long agencyId) {
        DashboardResponseDTO dashboard = stockService.getDashboardData(agencyId);
        return ApiResponse.success(SuccessStatus.DASHBOARD_SUCCESS, dashboard);
    }

}
