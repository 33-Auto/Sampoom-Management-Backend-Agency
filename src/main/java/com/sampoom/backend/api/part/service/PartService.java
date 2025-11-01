package com.sampoom.backend.api.part.service;

import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.part.dto.CategorySimpleResponseDTO;
import com.sampoom.backend.api.part.dto.PartFlatResponseDTO;
import com.sampoom.backend.api.part.dto.PartWithStockResponseDTO;
import com.sampoom.backend.api.part.dto.PartGroupResponseDTO;
import com.sampoom.backend.api.part.entity.Part;
import com.sampoom.backend.api.part.repository.PartQueryRepository;
import com.sampoom.backend.api.stock.service.StockService;
import com.sampoom.backend.common.dto.CategoryResponseDTO;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.mapper.ResponseMapper;
import com.sampoom.backend.common.response.ErrorStatus;
import com.sampoom.backend.common.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartReadService partReadService;
    private final StockService stockService;
    private final AgencyRepository agencyRepository;
    private final PartQueryRepository partQueryRepository;
    private final ResponseMapper responseMapper;

    // 카테고리 목록 조회
    public List<CategorySimpleResponseDTO> getCategories() {
        return partReadService.getCategories().stream()
                .map(CategorySimpleResponseDTO::fromEntity)
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
    public PageResponseDTO<CategoryResponseDTO> searchParts(Long agencyId, String keyword, int page, int size) {

        // 대리점 존재 여부 확인
        agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);

        Page<PartFlatResponseDTO> flatPage = partQueryRepository.searchPartsWithStock(agencyId, keyword, pageable);

        // flat → nested 변환
        List<CategoryResponseDTO> nested = responseMapper.toNestedStructure(flatPage.getContent());

        return PageResponseDTO.<CategoryResponseDTO>builder()
                .content(nested)
                .totalElements(flatPage.getTotalElements())
                .totalPages(flatPage.getTotalPages())
                .currentPage(page)
                .build();
    }
}
