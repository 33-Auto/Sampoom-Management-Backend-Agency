package com.sampoom.backend.api.part.service;

import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.part.dto.CategorySimpleResponseDTO;
import com.sampoom.backend.api.part.dto.PartFlatResponseDTO;
import com.sampoom.backend.api.part.dto.PartWithStockResponseDTO;
import com.sampoom.backend.api.part.dto.PartGroupResponseDTO;
import com.sampoom.backend.api.part.entity.Part;
import com.sampoom.backend.api.part.entity.Category;
import com.sampoom.backend.api.part.entity.PartGroup;
import com.sampoom.backend.api.stock.service.StockService;
import com.sampoom.backend.common.dto.CategoryResponseDTO;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.mapper.ResponseMapper;
import com.sampoom.backend.common.response.ErrorStatus;
import com.sampoom.backend.common.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartService {

    private final PartReadService partReadService;
    private final StockService stockService;
    private final AgencyRepository agencyRepository;
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

    // 그룹별 부품 목록 + 재고 포함
    public List<PartWithStockResponseDTO> getPartsWithStock(Long agencyId, Long groupId) {
        // 대리점 존재 여부 확인
        if (!agencyRepository.existsById(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND);
        }

        // 그룹별 부품 목록 조회
        List<Part> parts = partReadService.getPartsByGroup(groupId);

        // 대리점별 재고 정보 조회
        Map<Long, Integer> stockMap = stockService.getStockByAgency(agencyId);

        // 부품 + 재고 정보 결합
        return parts.stream()
                .map(part -> PartWithStockResponseDTO.of(
                    part,
                    stockMap.getOrDefault(part.getId(), 0)
                ))
                .toList();
    }

    // 부품 검색
    public PageResponseDTO<CategoryResponseDTO> searchParts(Long agencyId, String keyword, int page, int size) {
        // 대리점 존재 여부 확인
        if (!agencyRepository.existsById(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND);
        }

        // ...existing code...
        // 부품 검색 (페이징 없이 전체 조회 후 수동 페이징)
        List<Part> allParts = partReadService.searchParts(keyword);

        // 대리점별 재고 정보 조회
        Map<Long, Integer> stockMap = stockService.getStockByAgency(agencyId);

        // 부품을 PartFlatResponseDTO로 변환
        List<PartFlatResponseDTO> flatParts = allParts.stream()
                .map(part -> {
                    // 그룹과 카테고리 정보 조회
                    List<PartGroup> groups = partReadService.getGroupsByCategory(part.getCategoryId());
                    PartGroup group = groups.stream()
                            .filter(g -> g.getId().equals(part.getGroupId()))
                            .findFirst()
                            .orElse(null);

                    List<Category> categories = partReadService.getCategories();
                    Category category = categories.stream()
                            .filter(c -> c.getId().equals(part.getCategoryId()))
                            .findFirst()
                            .orElse(null);

                    return new PartFlatResponseDTO(
                            part.getCategoryId(),
                            category != null ? category.getName() : "",
                            part.getGroupId(),
                            group != null ? group.getName() : "",
                            part.getId(),
                            part.getCode(),
                            part.getName(),
                            stockMap.getOrDefault(part.getId(), 0),
                            part.getStandardCost()
                    );
                })
                .toList();

        // 수동 페이징 처리
        int start = page * size;
        int end = Math.min(start + size, flatParts.size());
        List<PartFlatResponseDTO> pagedParts = start < flatParts.size() ?
                flatParts.subList(start, end) : List.of();

        // flat → nested 구조 변환
        List<CategoryResponseDTO> nestedStructure = responseMapper.toNestedStructure(pagedParts);

        return PageResponseDTO.<CategoryResponseDTO>builder()
                .content(nestedStructure)
                .totalElements(flatParts.size())
                .totalPages((int) Math.ceil((double) flatParts.size() / size))
                .currentPage(page)
                .build();
    }
}
