package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.cart.entity.AgencyCartItem;
import com.sampoom.backend.api.cart.repository.AgencyCartItemRepository;
import com.sampoom.backend.api.order.dto.AgencyOrderResponseDTO;
import com.sampoom.backend.api.order.entity.AgencyOrder;
import com.sampoom.backend.api.order.entity.AgencyOrderItem;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.repository.AgencyOrderItemRepository;
import com.sampoom.backend.api.order.repository.AgencyOrderRepository;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgencyOrderService {

    private final AgencyRepository agencyRepository;
    private final AgencyOrderRepository agencyOrderRepository;
    private final AgencyCartItemRepository agencyCartItemRepository;

    private Agency validateAgency(Long agencyId) {
        return agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));
    }

    // 장바구니 → 주문 생성
    @Transactional
    public AgencyOrderResponseDTO createOrder(Long agencyId) {

        validateAgency(agencyId);

        // 장바구니 조회
        List<AgencyCartItem> cartItems = agencyCartItemRepository.findByAgency_Id(agencyId);

        if (cartItems.isEmpty()) {
            throw new BadRequestException(ErrorStatus.CART_EMPTY);
        }

        // 주문번호 생성
        String orderNumber = generateOrderNumber();

        // 주문 생성
        AgencyOrder agencyOrder = AgencyOrder.create(agencyId, orderNumber);

        // 장바구니 품목을 주문 품목으로 변환
        for (AgencyCartItem cartItem : cartItems) {
            AgencyOrderItem orderItem = AgencyOrderItem.fromCartItem(
                    cartItem.getPart().getId(),
                    cartItem.getPart().getName(),
                    cartItem.getPart().getCode(),
                    cartItem.getQuantity()
            );
            agencyOrder.addItem(orderItem);
        }

        // 주문 저장
        AgencyOrder savedOrder = agencyOrderRepository.save(agencyOrder);

        // 장바구니 비우기
        agencyCartItemRepository.deleteAll(cartItems);

        return AgencyOrderResponseDTO.fromEntity(savedOrder);
    }

    // 대리점별 주문 목록 조회
    @Transactional
    public List<AgencyOrderResponseDTO> getOrdersByAgency(Long agencyId) {

        validateAgency(agencyId);

        return agencyOrderRepository.findByAgencyId(agencyId).stream()
                .map(AgencyOrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 주문 단건 조회
    @Transactional
    public AgencyOrderResponseDTO getOrderDetail(Long agencyId, Long orderId) {

        validateAgency(agencyId);

        AgencyOrder order = agencyOrderRepository.findByIdAndAgencyId(orderId, agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND));
        return AgencyOrderResponseDTO.fromEntity(order);
    }

    // 주문 취소
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
    public void markOrderReceived(Long agencyId, Long orderId) {

        validateAgency(agencyId);

        AgencyOrder order = agencyOrderRepository.findByIdAndAgencyId(orderId, agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND));

        order.markReceived();
    }

    // 주문번호 생성
    private String generateOrderNumber() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long countToday = agencyOrderRepository.countByCreatedAtBetween(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay()
        );

        String sequence = String.format("%03d", countToday + 1); // 001, 002, 003 ...
        return "ORD-" + today + "-" + sequence;
    }

}
