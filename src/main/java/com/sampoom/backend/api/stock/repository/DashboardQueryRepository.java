package com.sampoom.backend.api.stock.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sampoom.backend.api.stock.dto.DashboardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.sampoom.backend.api.part.entity.QPart.part;
import static com.sampoom.backend.api.stock.entity.QAgencyStock.agencyStock;

@Repository
@RequiredArgsConstructor
public class DashboardQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 모든 부품을 기준으로 대시보드 데이터 조회
     * - 재고가 없는 부품도 0으로 계산됨
     */
    public DashboardResponseDTO getDashboardData(Long agencyId) {
        // 전체 부품 수
        Long totalPartsResult = queryFactory
                .select(part.count())
                .from(part)
                .where(part.deleted.eq(false))
                .fetchOne();
        long totalParts = totalPartsResult != null ? totalPartsResult : 0L;

        // 해당 대리점의 실제 재고가 있는 부품 수
        Long stockedPartsResult = queryFactory
                .select(agencyStock.countDistinct())
                .from(agencyStock)
                .where(agencyStock.agency.id.eq(agencyId))
                .fetchOne();
        long stockedParts = stockedPartsResult != null ? stockedPartsResult : 0L;

        // 품절 부품 수 = 전체 부품 수 - 재고가 있는 부품 수
        long outOfStockParts = totalParts - stockedParts;

        // 재고 부족 부품 수 (1~10개)
        Long lowStockPartsResult = queryFactory
                .select(agencyStock.count())
                .from(agencyStock)
                .where(agencyStock.agency.id.eq(agencyId)
                        .and(agencyStock.quantity.between(1, 10)))
                .fetchOne();
        long lowStockParts = lowStockPartsResult != null ? lowStockPartsResult : 0L;

        // 총 재고 수량
        Integer totalQuantityResult = queryFactory
                .select(agencyStock.quantity.sum())
                .from(agencyStock)
                .where(agencyStock.agency.id.eq(agencyId))
                .fetchOne();
        long totalQuantity = totalQuantityResult != null ? totalQuantityResult.longValue() : 0L;

        return DashboardResponseDTO.builder()
                .totalParts(totalParts)
                .outOfStockParts(outOfStockParts)
                .lowStockParts(lowStockParts)
                .totalQuantity(totalQuantity)
                .build();
    }
}
