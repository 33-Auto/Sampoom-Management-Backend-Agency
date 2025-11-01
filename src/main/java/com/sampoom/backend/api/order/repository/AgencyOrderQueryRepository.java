package com.sampoom.backend.api.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sampoom.backend.api.order.dto.AgencyOrderFlatResponseDTO;
import com.sampoom.backend.api.order.dto.QAgencyOrderFlatResponseDTO;
import com.sampoom.backend.api.order.entity.QAgencyOrder;
import com.sampoom.backend.api.order.entity.QAgencyOrderItem;
import com.sampoom.backend.api.part.entity.QPart;
import com.sampoom.backend.api.part.entity.QPartGroup;
import com.sampoom.backend.api.part.entity.QCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AgencyOrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 대리점별 주문 목록 조회
    public List<AgencyOrderFlatResponseDTO> findOrderItemsWithNames(Long agencyId) {
        QAgencyOrder order = QAgencyOrder.agencyOrder;
        QAgencyOrderItem item = QAgencyOrderItem.agencyOrderItem;
        QPart part = QPart.part;
        QPartGroup group = QPartGroup.partGroup;
        QCategory category = QCategory.category;

        return queryFactory
                .select(new QAgencyOrderFlatResponseDTO(
                        order.id,
                        order.orderNumber,
                        order.createdAt,
                        order.status,
                        order.agencyName,
                        category.id,
                        category.name,
                        group.id,
                        group.name,
                        part.id,
                        part.code,
                        part.name,
                        item.quantity
                ))
                .from(order)
                .join(item).on(item.agencyOrder.id.eq(order.id))
                .join(part).on(item.partId.eq(part.id))
                .join(group).on(part.groupId.eq(group.id))
                .join(category).on(group.categoryId.eq(category.id))
                .where(order.agencyId.eq(agencyId))
                .fetch();
    }

    // 주문 단건 조회
    public List<AgencyOrderFlatResponseDTO> findOrderItemsByOrderId(Long orderId) {
        QAgencyOrder order = QAgencyOrder.agencyOrder;
        QAgencyOrderItem item = QAgencyOrderItem.agencyOrderItem;
        QPart part = QPart.part;
        QPartGroup group = QPartGroup.partGroup;
        QCategory category = QCategory.category;

        return queryFactory
                .select(new QAgencyOrderFlatResponseDTO(
                        order.id,
                        order.orderNumber,
                        order.createdAt,
                        order.status,
                        order.agencyName,
                        category.id,
                        category.name,
                        group.id,
                        group.name,
                        part.id,
                        part.code,
                        part.name,
                        item.quantity
                ))
                .from(order)
                .join(item).on(item.agencyOrder.id.eq(order.id))
                .join(part).on(item.partId.eq(part.id))
                .join(group).on(part.groupId.eq(group.id))
                .join(category).on(group.categoryId.eq(category.id))
                .where(order.id.eq(orderId))
                .fetch();
    }
}
