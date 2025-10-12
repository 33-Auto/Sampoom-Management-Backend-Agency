package com.sampoom.backend.api.part.feign;

import com.sampoom.backend.api.part.feign.dto.ApiResponse;
import com.sampoom.backend.api.part.feign.dto.CategoryResponseDTO;
import com.sampoom.backend.api.part.feign.dto.PartGroupResponseDTO;
import com.sampoom.backend.api.part.feign.dto.PartResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// Part 서버의 REST API 호출용 클라이언트
@FeignClient(name = "partClient", url = "http://3.38.218.173:8080/api/parts")
public interface PartClient {

    // 부품 카테고리 목록 조회
    @GetMapping("/categories")
    ApiResponse<List<CategoryResponseDTO>> getCategories();

    // 카테고리별 그룹 목록 조회
    @GetMapping("/categories/{categoryId}/groups")
    ApiResponse<List<PartGroupResponseDTO>> getGroupsByCategory(@PathVariable("categoryId") Long categoryId);

    // 그룹별 부품 목록 조회
    @GetMapping
    ApiResponse<List<PartResponseDTO>> getPartsByGroup(@RequestParam("groupId") Long groupId);
}

