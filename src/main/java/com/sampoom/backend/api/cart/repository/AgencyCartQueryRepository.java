package com.sampoom.backend.api.cart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sampoom.backend.api.cart.dto.AgencyCartResponseDTO;
import com.sampoom.backend.api.cart.dto.QAgencyCartResponseDTO;
import com.sampoom.backend.api.cart.entity.QAgencyCartItem;
import com.sampoom.backend.api.part.entity.QCategory;
import com.sampoom.backend.api.part.entity.QPart;
import com.sampoom.backend.api.part.entity.QPartGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AgencyCartQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<AgencyCartResponseDTO> findCartItemsWithNames(Long agencyId) {
        QAgencyCartItem cart = QAgencyCartItem.agencyCartItem;
        QPart part = QPart.part;
        QPartGroup group = QPartGroup.partGroup;
        QCategory category = QCategory.category;

        return queryFactory
                .select(new QAgencyCartResponseDTO(
                        cart.id,
                        part.id,
                        part.name,
                        part.code,
                        cart.quantity,
                        group.id,
                        group.name,
                        category.id,
                        category.name,
                        part.standardCost
                ))
                .from(cart)
                .join(part).on(cart.partId.eq(part.id))
                .leftJoin(group).on(part.groupId.eq(group.id))
                .leftJoin(category).on(group.categoryId.eq(category.id))
                .where(cart.agency.id.eq(agencyId))
                .fetch();
    }

    // 사용자별 장바구니 조회 메서드 (개인별)
    public List<AgencyCartResponseDTO> findCartItemsByUserId(String userId) {
        QAgencyCartItem cart = QAgencyCartItem.agencyCartItem;
        QPart part = QPart.part;
        QPartGroup group = QPartGroup.partGroup;
        QCategory category = QCategory.category;

        return queryFactory
                .select(new QAgencyCartResponseDTO(
                        cart.id,
                        part.id,
                        part.name,
                        part.code,
                        cart.quantity,
                        group.id,
                        group.name,
                        category.id,
                        category.name,
                        part.standardCost
                ))
                .from(cart)
                .join(part).on(cart.partId.eq(part.id))
                .leftJoin(group).on(part.groupId.eq(group.id))
                .leftJoin(category).on(group.categoryId.eq(category.id))
                .where(cart.userId.eq(userId))
                .fetch();
    }
}
