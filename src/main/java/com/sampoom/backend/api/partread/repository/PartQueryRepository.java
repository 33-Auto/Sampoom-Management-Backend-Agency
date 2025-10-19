package com.sampoom.backend.api.partread.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sampoom.backend.api.partread.dto.PartFlatResponseDTO;
import com.sampoom.backend.api.partread.dto.QPartFlatResponseDTO;
import com.sampoom.backend.api.partread.entity.QPart;
import com.sampoom.backend.api.partread.entity.QPartGroup;
import com.sampoom.backend.api.partread.entity.QCategory;
import com.sampoom.backend.api.stock.entity.QAgencyStock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PartQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<PartFlatResponseDTO> searchPartsWithStock(Long agencyId, String keyword, Pageable pageable) {
        QPart part = QPart.part;
        QPartGroup group = QPartGroup.partGroup;
        QCategory category = QCategory.category;
        QAgencyStock stock = QAgencyStock.agencyStock;

        // 실제 페이지 내용
        List<PartFlatResponseDTO> content = queryFactory
                .select(new QPartFlatResponseDTO(
                        category.id,
                        category.name,
                        group.id,
                        group.name,
                        part.id,
                        part.code,
                        part.name,
                        stock.quantity.coalesce(0)
                ))
                .from(part)
                .join(group).on(part.groupId.eq(group.id))
                .join(category).on(group.categoryId.eq(category.id))
                .leftJoin(stock).on(stock.partId.eq(part.id)
                        .and(stock.agency.id.eq(agencyId)))
                .where(
                        keyword != null && !keyword.isBlank()
                                ? part.name.containsIgnoreCase(keyword)
                                .or(part.code.containsIgnoreCase(keyword))
                                : null
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수
        Long total = queryFactory
                .select(part.count())
                .from(part)
                .join(group).on(part.groupId.eq(group.id))
                .join(category).on(group.categoryId.eq(category.id))
                .leftJoin(stock).on(stock.partId.eq(part.id)
                        .and(stock.agency.id.eq(agencyId)))
                .where(
                        keyword != null && !keyword.isBlank()
                                ? part.name.containsIgnoreCase(keyword)
                                .or(part.code.containsIgnoreCase(keyword))
                                : null
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
