package com.sampoom.backend.api.part.service;

import com.sampoom.backend.api.part.feign.PartClient;
import com.sampoom.backend.api.part.feign.dto.CategoryResponseDTO;
import com.sampoom.backend.api.part.feign.dto.PartGroupResponseDTO;
import com.sampoom.backend.api.part.feign.dto.PartResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartClient partClient;

    // 부품 카테고리 목록 조회
    public List<CategoryResponseDTO> getCategories(Long agencyId) {
        return unwrap(partClient.getCategories());
    }

    // 카테고리 내 그룹 목록 조회
    public List<PartGroupResponseDTO> getGroups(Long agencyId, Long categoryId) {
        return unwrap(partClient.getGroupsByCategory(categoryId));
    }

    // 그룹 내 부품 목록 조회
    public List<PartResponseDTO> getParts(Long agencyId, Long groupId) {
        return unwrap(partClient.getPartsByGroup(groupId));
    }

    // 공통 응답 언랩 메서드
    private static <T> T unwrap(com.sampoom.backend.api.part.feign.dto.ApiResponse<T> response) {
        if (response == null || response.getData() == null) {
            throw new IllegalStateException("Part 서버 응답이 비어 있습니다.");
        }
        return response.getData();
    }
}
