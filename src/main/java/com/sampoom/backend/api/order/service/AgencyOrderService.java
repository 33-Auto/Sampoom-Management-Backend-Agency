package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.cart.dto.AgencyCartResponseDTO;
import com.sampoom.backend.api.cart.repository.AgencyCartItemRepository;
import com.sampoom.backend.api.cart.repository.AgencyCartQueryRepository;
import com.sampoom.backend.api.order.dto.AgencyOrderFlatResponseDTO;
import com.sampoom.backend.api.order.dto.AgencyOrderResponseDTO;
import com.sampoom.backend.api.order.entity.AgencyOrder;
import com.sampoom.backend.api.order.entity.AgencyOrderItem;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.repository.AgencyOrderItemRepository;
import com.sampoom.backend.api.order.repository.AgencyOrderQueryRepository;
import com.sampoom.backend.api.order.repository.AgencyOrderRepository;
import com.sampoom.backend.api.stock.service.StockService;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.mapper.ResponseMapper;
import com.sampoom.backend.common.response.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AgencyOrderService {

    private final AgencyRepository agencyRepository;
    private final AgencyOrderRepository agencyOrderRepository;
    private final AgencyCartItemRepository agencyCartItemRepository;
    private final AgencyOrderItemRepository agencyOrderItemRepository;
    private final StockService stockService;
    private final AgencyOrderQueryRepository orderQueryRepository;
    private final ResponseMapper responseMapper;
    private final AgencyCartQueryRepository agencyCartQueryRepository;

    // 장바구니 → 주문 생성
    @Transactional
    public List<AgencyOrderResponseDTO> createOrder(Long agencyId) {

        // 1. 대리점 존재 확인
        Agency agency = validateAgency(agencyId);

        // 2. 장바구니 조회 (읽기전용 QueryDSL)
        List<AgencyCartResponseDTO> cartItems = agencyCartQueryRepository.findCartItemsWithNames(agencyId);

        if (cartItems.isEmpty()) {
            throw new BadRequestException(ErrorStatus.CART_EMPTY);
        }

        // 3. 주문 엔티티 생성
        AgencyOrder order = AgencyOrder.create(agency.getId(), agency.getName());

        // 4. 장바구니 항목을 주문 항목으로 변환
        cartItems.forEach(item -> {
            AgencyOrderItem orderItem = AgencyOrderItem.fromCartItem(
                    item.getPartId(),
                    item.getPartName(),
                    item.getPartCode(),
                    item.getQuantity()
            );
            order.addItem(orderItem);
        });

        // 5. DB 저장
        agencyOrderRepository.saveAndFlush(order);

        // 6. 장바구니 비우기
        agencyCartItemRepository.deleteAllByAgencyId(agencyId);

        // 방금 생성된 주문 내역 반환
        List<AgencyOrderFlatResponseDTO> createdOrderFlat = orderQueryRepository.findOrderItemsByOrderId(order.getId());
        return responseMapper.toNestedOrderStructure(createdOrderFlat);
    }

    // 대리점별 주문 목록 조회
    @Transactional
    public List<AgencyOrderResponseDTO> getOrdersByAgency(Long agencyId) {

        if (!agencyRepository.existsById(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND);
        }

        // flat 데이터 조회
        List<AgencyOrderFlatResponseDTO> flatList = orderQueryRepository.findOrderItemsWithNames(agencyId);

        // flat → nested (주문별 묶기)
        return responseMapper.toNestedOrderStructure(flatList);
    }

    // 주문 단건 조회
    @Transactional
    public List<AgencyOrderResponseDTO> getOrderDetail(Long agencyId, Long orderId) {

        if (!agencyRepository.existsById(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND);
        }

        // flat 데이터 조회
        List<AgencyOrderFlatResponseDTO> flatList = orderQueryRepository.findOrderItemsByOrderId(orderId);

        if (flatList.isEmpty()) {
            throw new NotFoundException(ErrorStatus.ORDER_NOT_FOUND);
        }

        return responseMapper.toNestedOrderStructure(flatList);
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long agencyId, Long orderId) {

        validateAgency(agencyId);

        AgencyOrder order = agencyOrderRepository.findByIdAndAgencyId(orderId, agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BadRequestException(ErrorStatus.ORDER_CANNOT_CANCEL);
        }
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new BadRequestException(ErrorStatus.ORDER_ALREADY_CANCELED);
        }
        order.cancel();
    }

    // 주문 상태 변경
    @Transactional
    public void markOrderReceived(Long agencyId, Long orderId) {

        validateAgency(agencyId);

        AgencyOrder order = agencyOrderRepository.findByIdAndAgencyId(orderId, agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new BadRequestException(ErrorStatus.ORDER_ALREADY_CANCELED);
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BadRequestException(ErrorStatus.ORDER_ALREADY_COMPLETED);
        }

        // 주문 품목 조회
        List<AgencyOrderItem> items = agencyOrderItemRepository.findByAgencyOrder_Id(orderId);

        items.forEach(item ->
                stockService.increaseStock(agencyId, item.getPartId(), item.getQuantity())
        );

        // 주문 상태 변경
        order.markReceived();
    }

    private Agency validateAgency(Long agencyId) {
        return agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));
    }
}
