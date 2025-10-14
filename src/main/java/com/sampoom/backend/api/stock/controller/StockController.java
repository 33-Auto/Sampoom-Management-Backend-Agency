//package com.sampoom.backend.api.stock.controller;
//
//import com.sampoom.backend.api.stock.dto.StockInboundRequestDTO;
//import com.sampoom.backend.api.stock.dto.StockOutboundRequestDTO;
//import com.sampoom.backend.api.stock.dto.StockResponseDTO;
//import com.sampoom.backend.api.stock.service.StockService;
//import com.sampoom.backend.common.response.ApiResponse;
//import com.sampoom.backend.common.response.SuccessStatus;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Tag(name = "Stock", description = "재고 API")
//@Slf4j
//@RestController
//@RequestMapping("/{agencyId}/stocks")
//@RequiredArgsConstructor
//public class StockController {
//
//    private final StockService stockService;
//
//    @Operation(summary = "대리점 재고 입고 처리", description = "대리점 재고 입고 처리")
//    @PostMapping("/inbound")
//    public ResponseEntity<ApiResponse<Void>> inboundStock(
//            @PathVariable Long agencyId,
//            @Valid @RequestBody StockInboundRequestDTO stockInboundRequestDTO
//    ) {
//        stockService.inboundStock(agencyId, stockInboundRequestDTO);
//
//        return ApiResponse.success(SuccessStatus.STOCK_INBOUND_SUCCESS, null);
//    }
//
//    @Operation(summary = "대리점 재고 출고 처리", description = "여러 부품 재고를 출고 처리")
//    @PostMapping("/outbound")
//    public ResponseEntity<ApiResponse<Void>> outboundStock(
//            @PathVariable Long agencyId,
//            @Valid @RequestBody StockOutboundRequestDTO stockOutboundRequestDTO
//    ) {
//        stockService.outboundStock(agencyId, stockOutboundRequestDTO);
//
//        return ApiResponse.success(SuccessStatus.STOCK_OUTBOUND_SUCCESS, null);
//    }
//}