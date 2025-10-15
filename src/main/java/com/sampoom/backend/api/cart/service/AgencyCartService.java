package com.sampoom.backend.api.cart.service;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.cart.dto.AgencyCartRequestDTO;
import com.sampoom.backend.api.cart.dto.AgencyCartResponseDTO;
import com.sampoom.backend.api.cart.entity.AgencyCartItem;
import com.sampoom.backend.api.cart.repository.AgencyCartItemRepository;
import com.sampoom.backend.api.partread.entity.Part;
import com.sampoom.backend.api.partread.service.PartReadService;
import com.sampoom.backend.common.exception.NotFoundException;
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

    // 장바구니 부품 추가
    @Transactional
    public void addCartItem(Long agencyId, AgencyCartRequestDTO agencyCartRequestDTO) {
        // Agency 조회
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND.getMessage()));

        // Part 조회
        Part part = partReadService.getPartById(agencyCartRequestDTO.getPartId()); // 수정됨

        // 기존 장바구니 항목 확인
        AgencyCartItem existingItem = agencyCartItemRepository
                .findByAgency_IdAndPart_Id(agencyId, agencyCartRequestDTO.getPartId())
                .orElse(null);

        if (existingItem != null) {
            existingItem.updateQuantity(agencyCartRequestDTO.getQuantity());
        } else {
            AgencyCartItem newItem = AgencyCartItem.builder()
                    .agency(agency)
                    .part(part)
                    .quantity(agencyCartRequestDTO.getQuantity())
                    .build();
            agencyCartItemRepository.save(newItem);
        }
    }

    // 장바구니 목록 조회
    @Transactional
    public List<AgencyCartResponseDTO> getCartItems(Long agencyId) {
        // Agency 조회
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND.getMessage()));

        List<AgencyCartItem> items = agencyCartItemRepository.findByAgency_Id(agencyId);

        return items.stream()
                .map(item -> AgencyCartResponseDTO.builder()
                        .cartItemId(item.getId())
                        .partId(item.getPart().getId())
                        .partName(item.getPart().getName())
                        .partCode(item.getPart().getCode())
                        .quantity(item.getQuantity())
                        .build())
                .toList();
    }

    // 장바구니 수량 수정
    @Transactional
    public void updateCartItem(Long agencyId, Long cartItemId, AgencyCartRequestDTO dto) {
        AgencyCartItem item = agencyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.CART_ITEM_NOT_FOUND.getMessage()));

        if (!item.getAgency().getId().equals(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_CART_MISMATCH.getMessage());
        }

        item.updateQuantity(dto.getQuantity());
    }

    // 장바구니 항목 삭제
    @Transactional
    public void deleteCartItem(Long agencyId, Long cartItemId) {
        AgencyCartItem item = agencyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.CART_ITEM_NOT_FOUND.getMessage()));

        if (!item.getAgency().getId().equals(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_CART_MISMATCH.getMessage());
        }

        agencyCartItemRepository.delete(item);
    }

    // 장바구니 전체 비우기
    @Transactional
    public void clearCart(Long agencyId) {
        List<AgencyCartItem> items = agencyCartItemRepository.findByAgency_Id(agencyId);
        agencyCartItemRepository.deleteAll(items);
    }

}

