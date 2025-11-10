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
import com.sampoom.backend.common.exception.BadRequestException;
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

    // 개인별 장바구니 부품 추가 (JWT userId 사용)
    @Transactional
    public void addCartItem(Long agencyId, String userId, AgencyCartRequestDTO agencyCartRequestDTO) {
        // Agency 조회
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        // Part 조회
        Part part = partReadService.getPartById(agencyCartRequestDTO.getPartId());

        // 해당 사용자의 기존 장바구니 항목 확인 (userId 기준)
        AgencyCartItem existingItem = agencyCartItemRepository
                .findByUserIdAndPartId(userId, agencyCartRequestDTO.getPartId())
                .orElse(null);

        if (existingItem != null) {
            existingItem.addQuantity(agencyCartRequestDTO.getQuantity());
        } else {
            AgencyCartItem newItem = AgencyCartItem.create(
                    agency, userId, part, agencyCartRequestDTO.getQuantity()
            );
            agencyCartItemRepository.save(newItem);
        }
    }

    // 개인별 장바구니 목록 조회 (JWT userId 사용)
    @Transactional
    public List<CategoryResponseDTO> getCartItems(Long agencyId, String userId) {
        if (!agencyRepository.existsById(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND);
        }

        // 해당 사용자의 장바구니 항목만 조회 (userId 기준)
        List<AgencyCartResponseDTO> items = agencyCartQueryRepository.findCartItemsByUserId(userId);

        // flat → nested 구조 변환
        return responseMapper.toNestedStructure(items);
    }

    // 개인별 장바구니 수량 수정 (JWT userId 사용)
    @Transactional
    public void updateCartItem(Long agencyId, Long cartItemId, String userId, AgencyCartUpdateRequestDTO dto) {
        AgencyCartItem item = agencyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.CART_ITEM_NOT_FOUND));

        // 해당 아이템이 이 사용자의 것인지 확인
        if (!item.getUserId().equals(userId)) {
            throw new BadRequestException(ErrorStatus.ACCESS_DENIED);
        }

        item.setQuantity(dto.getQuantity());
    }

    // 개인별 장바구니 항목 삭제 (JWT userId 사용)
    @Transactional
    public void deleteCartItem(Long agencyId, Long cartItemId, String userId) {
        AgencyCartItem item = agencyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.CART_ITEM_NOT_FOUND));

        // 해당 아이템이 이 사용자의 것인지 확인
        if (!item.getUserId().equals(userId)) {
            throw new BadRequestException(ErrorStatus.ACCESS_DENIED);
        }

        agencyCartItemRepository.delete(item);
    }

    // 개인별 장바구니 전체 비우기 (JWT userId 사용)
    @Transactional
    public void clearCart(Long agencyId, String userId) {
        // 해당 사용자의 모든 장바구니 항목 삭제 (agencyId 검증은 선택사항)
        agencyCartItemRepository.deleteAllByUserId(userId);
    }
}
