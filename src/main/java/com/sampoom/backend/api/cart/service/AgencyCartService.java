package com.sampoom.backend.api.cart.service;

import com.sampoom.backend.api.cart.dto.AgencyCartRequestDTO;
import com.sampoom.backend.api.cart.dto.AgencyCartResponseDTO;
import com.sampoom.backend.api.cart.entity.AgencyCartItem;
import com.sampoom.backend.api.cart.repository.AgencyCartItemRepository;
import com.sampoom.backend.api.part.feign.PartClient;
import com.sampoom.backend.api.part.feign.dto.PartResponseDTO;
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
    private final PartClient partClient;

    // 장바구니 부품 추가
    @Transactional
    public void addCartItem(Long agencyId, AgencyCartRequestDTO agencyCartRequestDTO) {
        // 기존 장바구니에 같은 부품이 있으면 수량만 증가
        AgencyCartItem existing = agencyCartItemRepository.findByAgencyIdAndPartId(agencyId, agencyCartRequestDTO.getPartId())
                .orElse(null);

        if (existing != null) {
            existing.updateQuantity(existing.getQuantity() + agencyCartRequestDTO.getQuantity());
        } else {
            AgencyCartItem newItem = AgencyCartItem.builder()
                    .agencyId(agencyId)
                    .partId(agencyCartRequestDTO.getPartId())
                    .quantity(agencyCartRequestDTO.getQuantity())
                    .build();
            agencyCartItemRepository.save(newItem);
        }
    }

    // 장바구니 목록 조회
    @Transactional
    public List<AgencyCartResponseDTO> getCartItems(Long agencyId) {
        List<AgencyCartItem> items = agencyCartItemRepository.findByAgencyId(agencyId);

        return items.stream()
                .map(item -> {
                    // 부품 상세 조회 (FeignClient 사용)
                    PartResponseDTO part = partClient.getPartById(item.getPartId()).getData();

                    return AgencyCartResponseDTO.builder()
                            .cartItemId(item.getId())
                            .partId(item.getPartId())
                            .partName(part.getName())
                            .partCode(part.getCode())
                            .quantity(item.getQuantity())
                            .build();
                })
                .toList();
    }

    // 장바구니 수량 수정
    @Transactional
    public void updateCartItem(Long agencyId, Long cartItemId, AgencyCartRequestDTO dto) {
        AgencyCartItem item = agencyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.CART_ITEM_NOT_FOUND.getMessage()));

        if (!item.getAgencyId().equals(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_CART_MISMATCH.getMessage());
        }

        item.updateQuantity(dto.getQuantity());
    }

    // 장바구니 항목 삭제
    @Transactional
    public void deleteCartItem(Long agencyId, Long cartItemId) {
        AgencyCartItem item = agencyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.CART_ITEM_NOT_FOUND.getMessage()));

        if (!item.getAgencyId().equals(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_CART_MISMATCH.getMessage());
        }

        agencyCartItemRepository.delete(item);
    }

    // 장바구니 전체 비우기
    @Transactional
    public void clearCart(Long agencyId) {
        List<AgencyCartItem> items = agencyCartItemRepository.findByAgencyId(agencyId);
        agencyCartItemRepository.deleteAll(items);
    }
}
