package com.sampoom.backend.api.stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long agencyId; // 대리점 ID

    @Column(nullable = false)
    private Long partId; // 부품 ID

    @Enumerated(EnumType.STRING)
    private HistoryAction action; // 활동 유형 (INBOUND, OUTBOUND)

    @Column(nullable = false)
    private Integer quantity; // 수량 (입고/출고는 실제 수량)

    @Column(nullable = false)
    private LocalDateTime date; // 활동 일시

    public static PartHistory createInboundHistory(Long agencyId, Long partId, Integer quantity) {
        return PartHistory.builder()
                .agencyId(agencyId)
                .partId(partId)
                .action(HistoryAction.INBOUND)
                .quantity(quantity) // 실제 입고 수량
                .date(LocalDateTime.now())
                .build();
    }

    public static PartHistory createOutboundHistory(Long agencyId, Long partId, Integer quantity) {
        return PartHistory.builder()
                .agencyId(agencyId)
                .partId(partId)
                .action(HistoryAction.OUTBOUND)
                .quantity(quantity) // 실제 출고 수량
                .date(LocalDateTime.now())
                .build();
    }
}
