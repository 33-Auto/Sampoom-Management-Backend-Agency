package com.sampoom.backend.api.partread.controller;

import com.sampoom.backend.api.partread.dto.PartWithStockResponseDTO;
import com.sampoom.backend.api.partread.dto.CategoryResponseDTO;
import com.sampoom.backend.api.partread.dto.PartGroupResponseDTO;
import com.sampoom.backend.api.partread.service.PartService;
import com.sampoom.backend.common.response.ApiResponse;
import com.sampoom.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Part", description = "대리점 전용 부품 조회 API")
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    @Operation(summary = "대리점 별 부품 카테고리 조회", description = "대리점 별 부품 카테고리 조회")
    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getCategories() {
        List<CategoryResponseDTO> categoryList = partService.getCategories();
        return ApiResponse.success(SuccessStatus.CATEGORY_LIST_SUCCESS, categoryList);
    }

    @Operation(summary = "대리점 별 카테고리 별 그룹 조회", description = "대리점 별 카테고리 별 그룹 조회")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<PartGroupResponseDTO>>> getGroups(
            @PathVariable Long categoryId
    ) {
        List<PartGroupResponseDTO> groupList = partService.getGroups(categoryId);
        return ApiResponse.success(SuccessStatus.GROUP_LIST_SUCCESS, groupList);
    }

    @Operation(summary = "대리점 별 부품 목록 조회 (그룹 기준)", description = "대리점 별 부품 목록 조회")
    @GetMapping("/{agencyId}/group/{groupId}")
    public ResponseEntity<ApiResponse<List<PartWithStockResponseDTO>>> getParts(
            @PathVariable Long agencyId,
            @PathVariable Long groupId
    ) {
        List<PartWithStockResponseDTO> partList = partService.getPartsWithStock(agencyId, groupId);
        return ApiResponse.success(SuccessStatus.PART_LIST_SUCCESS, partList);
    }

    @GetMapping("/{agencyId}/search")
    @Operation(summary = "부품 검색", description = "부품 코드 또는 이름으로 검색")
    public ResponseEntity<ApiResponse<List<PartWithStockResponseDTO>>> searchParts(
            @PathVariable Long agencyId,
            @RequestParam String keyword
    ) {
        List<PartWithStockResponseDTO> result = partService.searchParts(agencyId, keyword);
        return ApiResponse.success(SuccessStatus.PART_SEARCH_SUCCESS, result);
    }

}
