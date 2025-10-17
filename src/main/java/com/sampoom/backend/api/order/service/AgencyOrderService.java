package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.cart.entity.AgencyCartItem;
import com.sampoom.backend.api.cart.repository.AgencyCartItemRepository;
import com.sampoom.backend.api.order.dto.AgencyOrderItemResponseDTO;
import com.sampoom.backend.api.order.dto.AgencyOrderResponseDTO;
import com.sampoom.backend.api.order.entity.AgencyOrder;
import com.sampoom.backend.api.order.entity.AgencyOrderItem;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.repository.AgencyOrderItemRepository;
import com.sampoom.backend.api.order.repository.AgencyOrderRepository;
import com.sampoom.backend.api.partread.entity.Category;
import com.sampoom.backend.api.partread.entity.Part;
import com.sampoom.backend.api.partread.entity.PartGroup;
import com.sampoom.backend.api.partread.repository.CategoryRepository;
import com.sampoom.backend.api.partread.repository.PartGroupRepository;
import com.sampoom.backend.api.partread.repository.PartRepository;
import com.sampoom.backend.api.stock.service.StockService;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgencyOrderService {

    private final AgencyRepository agencyRepository;
    private final AgencyOrderRepository agencyOrderRepository;
    private final AgencyCartItemRepository agencyCartItemRepository;
    private final PartRepository partRepository;
    private final PartGroupRepository partGroupRepository;
    private final CategoryRepository categoryRepository;
    private final AgencyOrderItemRepository agencyOrderItemRepository;
    private final StockService stockService;

    // 장바구니 → 주문 생성
    @Transactional
    public AgencyOrderResponseDTO createOrder(Long agencyId) {

        Agency agency = validateAgency(agencyId);

        // 장바구니 조회
        List<AgencyCartItem> cartItems = agencyCartItemRepository.findByAgency_Id(agencyId);

        if (cartItems.isEmpty()) {
            throw new BadRequestException(ErrorStatus.CART_EMPTY);
        }

        // 주문 생성
        AgencyOrder agencyOrder = AgencyOrder.create(agencyId);

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

        return mapToOrderResponse(savedOrder, agency.getName());
    }

    // 대리점별 주문 목록 조회
    @Transactional
    public List<AgencyOrderResponseDTO> getOrdersByAgency(Long agencyId) {

        Agency agency = validateAgency(agencyId);

        return agencyOrderRepository.findByAgencyIdOrderByCreatedAtDesc(agencyId).stream()
                .map(order -> mapToOrderResponse(order, agency.getName()))
                .collect(Collectors.toList());
    }

    // 주문 단건 조회
    @Transactional
    public AgencyOrderResponseDTO getOrderDetail(Long agencyId, Long orderId) {

        Agency agency = validateAgency(agencyId);

        AgencyOrder order = agencyOrderRepository.findByIdAndAgencyId(orderId, agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND));

        return mapToOrderResponse(order, agency.getName());
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

        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new BadRequestException(ErrorStatus.ORDER_ALREADY_CANCELED);
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BadRequestException(ErrorStatus.ORDER_ALREADY_COMPLETED);
        }

        // 주문 품목 조회
        List<AgencyOrderItem> items = agencyOrderItemRepository.findByAgencyOrder_Id(orderId);

        // 각 품목별 재고 증가
        for (AgencyOrderItem item : items) {
            stockService.increaseStock(agencyId, item.getPartId(), item.getQuantity());
        }

        // 주문 상태 변경
        order.markReceived();
    }

    private Agency validateAgency(Long agencyId) {
        return agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));
    }

    private AgencyOrderResponseDTO mapToOrderResponse(AgencyOrder order, String agencyName) {

        // 주문 아이템에서 모든 partId 수집
        Set<Long> partIds = order.getItems().stream()
                .map(AgencyOrderItem::getPartId)
                .collect(Collectors.toSet());

        // 부품 ID들 한 번에 조회
        Map<Long, Part> partMap = partRepository.findAllById(partIds).stream()
                .collect(Collectors.toMap(Part::getId, p -> p));

        // 그룹 ID 전부 추출 후 한 번에 조회
        Set<Long> groupIds = partMap.values().stream()
                .map(Part::getGroupId)
                .collect(Collectors.toSet());

        Map<Long, PartGroup> groupMap = partGroupRepository.findAllById(groupIds).stream()
                .collect(Collectors.toMap(PartGroup::getId, g -> g));

        // 카테고리 ID 추출 후 한 번에 조회
        Set<Long> categoryIds = groupMap.values().stream()
                .map(PartGroup::getCategoryId)
                .collect(Collectors.toSet());

        Map<Long, Category> categoryMap = categoryRepository.findAllById(categoryIds).stream()
                .collect(Collectors.toMap(Category::getId, c -> c));

        // 매핑
        List<AgencyOrderItemResponseDTO> itemDTOs = order.getItems().stream()
                .map(item -> {
                    // Part 정보 조회
                    Part part = partMap.get(item.getPartId());
                    if (part == null) return null;

                    PartGroup group = groupMap.get(part.getGroupId());
                    Category category = categoryMap.get(group.getCategoryId());

                    return AgencyOrderItemResponseDTO.builder()
                            .partId(part.getId())
                            .partName(part.getName())
                            .partCode(part.getCode())
                            .quantity(item.getQuantity())
                            .categoryName(category != null ? category.getName() : null)
                            .groupName(group != null ? group.getName() : null)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return AgencyOrderResponseDTO.builder()
                .orderId(order.getId())
                .agencyId(order.getAgencyId())
                .agencyName(agencyName)
                .orderNumber(order.getOrderNumber())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .items(itemDTOs)
                .build();
    }


}
