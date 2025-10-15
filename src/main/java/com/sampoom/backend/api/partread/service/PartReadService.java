package com.sampoom.backend.api.partread.service;

import com.sampoom.backend.api.partread.entity.Category;
import com.sampoom.backend.api.partread.entity.Part;
import com.sampoom.backend.api.partread.entity.PartGroup;
import com.sampoom.backend.api.partread.repository.CategoryRepository;
import com.sampoom.backend.api.partread.repository.PartGroupRepository;
import com.sampoom.backend.api.partread.repository.PartRepository;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartReadService {

    private final CategoryRepository categoryRepository;
    private final PartGroupRepository partGroupRepository;
    private final PartRepository partRepository;

    // 카테고리 전체 조회
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    // 특정 카테고리의 그룹 목록 조회
    public List<PartGroup> getGroupsByCategory(Long categoryId) {
        return partGroupRepository.findByCategoryId(categoryId);
    }

    // 특정 그룹 내 부품 목록 조회
    public List<Part> getPartsByGroup(Long groupId) {
        return partRepository.findByGroupId(groupId);
    }

    // 키워드 검색
    public List<Part> searchParts(String keyword) {
        return partRepository.searchByKeyword(keyword);
    }

    // 부품 ID로 부품 조회
    public Part getPartById(Long partId) {
        return partRepository.findById(partId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PART_NOT_FOUND.getMessage()));
    }
}
