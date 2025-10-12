package com.sampoom.backend.api.part.service;

import com.sampoom.backend.api.part.dto.PartWithStockResponseDTO;
import com.sampoom.backend.api.part.feign.PartClient;
import com.sampoom.backend.api.part.feign.dto.CategoryResponseDTO;
import com.sampoom.backend.api.part.feign.dto.PartGroupResponseDTO;
import com.sampoom.backend.api.part.feign.dto.PartResponseDTO;
import com.sampoom.backend.api.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartClient partClient;
    private final StockService stockService;

    // 부품 카테고리 목록 조회
    public List<CategoryResponseDTO> getCategories(Long agencyId) {
        return partClient.getCategories().getData();
    }

    // 카테고리 내 그룹 목록 조회
    public List<PartGroupResponseDTO> getGroups(Long agencyId, Long categoryId) {
        return partClient.getGroupsByCategory(categoryId).getData();
    }

    // 그룹 내 부품 목록 조회
//    public List<PartResponseDTO> getParts(Long agencyId, Long groupId) {
//        return unwrap(partClient.getPartsByGroup(groupId));
//    }

    public List<PartWithStockResponseDTO> getPartsWithStock(Long agencyId, Long groupId) {

        try {
            List<PartResponseDTO> parts = partClient.getPartsByGroup(groupId).getData();
            Map<Long, Integer> stockMap = stockService.getStockByAgency(agencyId);

            return parts.stream()
                    .map(part -> PartWithStockResponseDTO.builder()
                            .partId(part.getPartId())
                            .name(part.getName())
                            .code(part.getCode())
                            .quantity(stockMap.getOrDefault(part.getPartId(), 0))
                            .build())
                    .toList();

        } catch (Exception e) {
            // 부품 서버 응답 실패 시 비어있는 리스트 반환
            return List.of();
        }
    }
}
