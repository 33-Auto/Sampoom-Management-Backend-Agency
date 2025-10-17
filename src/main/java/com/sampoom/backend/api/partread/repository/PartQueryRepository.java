package com.sampoom.backend.api.partread.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sampoom.backend.api.partread.dto.PartFlatResponseDTO;
import com.sampoom.backend.api.partread.dto.QPartFlatResponseDTO;
import com.sampoom.backend.api.partread.entity.QPart;
import com.sampoom.backend.api.partread.entity.QPartGroup;
import com.sampoom.backend.api.partread.entity.QCategory;
import com.sampoom.backend.api.stock.entity.QAgencyStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PartQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<PartFlatResponseDTO> searchPartsWithStock(Long agencyId, String keyword) {
        QPart part = QPart.part;
        QPartGroup group = QPartGroup.partGroup;
        QCategory category = QCategory.category;
        QAgencyStock stock = QAgencyStock.agencyStock;

        return queryFactory
                .select(new QPartFlatResponseDTO(
                        category.id,
                        category.name,
                        group.id,
                        group.name,
                        part.id,
                        part.code,
                        part.name,
                        stock.quantity.coalesce(0) // 재고 없으면 0
                ))
                .from(part)
                .join(group).on(part.groupId.eq(group.id))
                .join(category).on(group.categoryId.eq(category.id))
                .leftJoin(stock)
                .on(stock.partId.eq(part.id)
                        .and(stock.agency.id.eq(agencyId))) // 대리점별 재고 조인
                .where(
                        part.name.containsIgnoreCase(keyword)
                                .or(part.code.containsIgnoreCase(keyword))
                )
                .fetch();
    }
}
