package com.sampoom.backend.api.outbound.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sampoom.backend.api.outbound.dto.AgencyOutboundResponseDTO;
import com.sampoom.backend.api.outbound.entity.QAgencyOutboundItem;
import com.sampoom.backend.api.part.entity.QCategory;
import com.sampoom.backend.api.part.entity.QPart;
import com.sampoom.backend.api.part.entity.QPartGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AgencyOutboundQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<AgencyOutboundResponseDTO> findOutboundItemsWithNames(Long agencyId) {
        QAgencyOutboundItem outbound = QAgencyOutboundItem.agencyOutboundItem;
        QPart part = QPart.part;
        QPartGroup group = QPartGroup.partGroup;
        QCategory category = QCategory.category;

        return queryFactory
                .select(Projections.constructor(AgencyOutboundResponseDTO.class,
                        outbound.id,
                        part.id,
                        part.name,
                        part.code,
                        outbound.quantity,
                        group.id,
                        group.name,
                        category.id,
                        category.name
                ))
                .from(outbound)
                .join(part).on(outbound.partId.eq(part.id))
                .join(group).on(part.groupId.eq(group.id))
                .join(category).on(group.categoryId.eq(category.id))
                .where(outbound.agency.id.eq(agencyId))
                .fetch();
    }
}