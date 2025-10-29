package com.sampoom.backend.api.cart.service;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.cart.dto.AgencyCartRequestDTO;
import com.sampoom.backend.api.cart.dto.AgencyCartResponseDTO;
import com.sampoom.backend.api.cart.dto.AgencyCartUpdateRequestDTO;
import com.sampoom.backend.api.cart.entity.AgencyCartItem;
import com.sampoom.backend.api.cart.repository.AgencyCartItemRepository;
import com.sampoom.backend.api.cart.repository.AgencyCartQueryRepository;
import com.sampoom.backend.api.part.entity.Part;
import com.sampoom.backend.api.part.service.PartReadService;
import com.sampoom.backend.common.dto.CategoryResponseDTO;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.mapper.ResponseMapper;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgencyCartService {

    private final AgencyCartItemRepository agencyCartItemRepository;
    private final PartReadService partReadService;
    private final AgencyRepository agencyRepository;
    private final AgencyCartQueryRepository agencyCartQueryRepository;
    private final ResponseMapper responseMapper;

    // 장바구니 부품 추가
    @Transactional
    public void addCartItem(Long agencyId, AgencyCartRequestDTO agencyCartRequestDTO) {
        // Agency 조회
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        // Part 조회
        Part part = partReadService.getPartById(agencyCartRequestDTO.getPartId()); // 수정됨

        // 기존 장바구니 항목 확인
        AgencyCartItem existingItem = agencyCartItemRepository
                .findByAgency_IdAndPartId(agencyId, agencyCartRequestDTO.getPartId())
                .orElse(null);

        if (existingItem != null) {
            existingItem.addQuantity(agencyCartRequestDTO.getQuantity());
        } else {
            AgencyCartItem newItem = AgencyCartItem.create(
                    agency,
                    part,
                    agencyCartRequestDTO.getQuantity()
            );
            agencyCartItemRepository.save(newItem);
        }
    }

    // 장바구니 목록 조회
    @Transactional
    public List<CategoryResponseDTO> getCartItems(Long agencyId) {

        if (!agencyRepository.existsById(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND);
        }

        // flat 데이터 조회 (읽기전용 DB, QueryDSL 그대로 유지)
        List<AgencyCartResponseDTO> items = agencyCartQueryRepository.findCartItemsWithNames(agencyId);

        // flat → nested 구조 변환
        return responseMapper.toNestedStructure(items);
    }

    // 장바구니 수량 수정
    @Transactional
    public void updateCartItem(Long agencyId, Long cartItemId, AgencyCartUpdateRequestDTO dto) {
        AgencyCartItem item = agencyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.CART_ITEM_NOT_FOUND));

        if (!item.getAgency().getId().equals(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_CART_MISMATCH);
        }

        item.setQuantity(dto.getQuantity());
    }

    // 장바구니 항목 삭제
    @Transactional
    public void deleteCartItem(Long agencyId, Long cartItemId) {
        AgencyCartItem item = agencyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.CART_ITEM_NOT_FOUND));

        if (!item.getAgency().getId().equals(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_CART_MISMATCH);
        }

        agencyCartItemRepository.delete(item);
    }

    // 장바구니 전체 비우기
    @Transactional
    public void clearCart(Long agencyId) {
        agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        List<AgencyCartItem> items = agencyCartItemRepository.findByAgency_Id(agencyId);
        agencyCartItemRepository.deleteAll(items);
    }

}

