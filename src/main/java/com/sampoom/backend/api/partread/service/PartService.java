package com.sampoom.backend.api.partread.service;

import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.partread.dto.PartWithStockResponseDTO;
import com.sampoom.backend.api.partread.dto.CategoryResponseDTO;
import com.sampoom.backend.api.partread.dto.PartGroupResponseDTO;
import com.sampoom.backend.api.partread.entity.Part;
import com.sampoom.backend.api.stock.service.StockService;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartReadService partReadService;
    private final StockService stockService;
    private final AgencyRepository agencyRepository;

    // 카테고리 목록 조회
    public List<CategoryResponseDTO> getCategories() {
        return partReadService.getCategories().stream()
                .map(CategoryResponseDTO::fromEntity)
                .toList();
    }

    // 카테고리 내 그룹 목록 조회
    public List<PartGroupResponseDTO> getGroups(Long categoryId) {
        return partReadService.getGroupsByCategory(categoryId).stream()
                .map(PartGroupResponseDTO::fromEntity)
                .toList();
    }

    // 그룹별 부품 목록 + 재고 포함 (재고 없으면 0)
    public List<PartWithStockResponseDTO> getPartsWithStock(Long agencyId, Long groupId) {

        // 대리점 존재 여부 확인
        agencyRepository.findById(agencyId)
                .orElseThrow(() ->  new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        List<Part> parts = partReadService.getPartsByGroup(groupId);
        Map<Long, Integer> stockMap = stockService.getStockByAgency(agencyId);

        return parts.stream()
                .map(part -> PartWithStockResponseDTO.of(
                        part,
                        stockMap.getOrDefault(part.getId(), 0)
                ))
                .toList();
    }

    // 검색 결과 + 재고 포함 (재고 없으면 0)
    public List<PartWithStockResponseDTO> searchParts(Long agencyId, String keyword) {

        // 대리점 존재 여부 확인
        agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        List<Part> parts = partReadService.searchParts(keyword);
        Map<Long, Integer> stockMap = stockService.getStockByAgency(agencyId);

        return parts.stream()
                .map(part -> PartWithStockResponseDTO.of(
                        part,
                        stockMap.getOrDefault(part.getId(), 0)
                ))
                .toList();
    }
}
