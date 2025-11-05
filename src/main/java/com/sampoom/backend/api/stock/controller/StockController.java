package com.sampoom.backend.api.stock.controller;

import com.sampoom.backend.api.stock.dto.PartUpdateRequestDTO;
import com.sampoom.backend.api.stock.service.StockService;
import com.sampoom.backend.common.response.ApiResponse;
import com.sampoom.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Stock", description = "대리점 전용 재고 API")
@RestController
@RequestMapping("/{agencyId}/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> stockingParts(
            @PathVariable Long agencyId,
            @Valid @RequestBody PartUpdateRequestDTO partUpdateRequestDTO) {

        // 여러 품목에 대한 일괄 입고 처리
        stockService.processOrderReceiving(agencyId, partUpdateRequestDTO);

        return ApiResponse.success_only(SuccessStatus.OK);
    }

}
