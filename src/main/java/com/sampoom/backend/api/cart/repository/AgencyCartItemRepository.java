package com.sampoom.backend.api.cart.repository;

import com.sampoom.backend.api.cart.entity.AgencyCartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AgencyCartItemRepository extends CrudRepository<AgencyCartItem, Long> {

    // 사용자별 장바구니 항목 전체 조회 (개인별)
    List<AgencyCartItem> findByUserId(String userId);

    // 사용자 ID + 부품 ID로 단일 항목 조회 (개인별)
    Optional<AgencyCartItem> findByUserIdAndPartId(String userId, Long partId);

    // 사용자별 장바구니 전체 삭제 (개인별)
    void deleteAllByUserId(String userId);

    // 기존 대리점별 메서드들도 유지 (필요시)
    List<AgencyCartItem> findByAgency_Id(Long agencyId);
    Optional<AgencyCartItem> findByAgency_IdAndPartId(Long agencyId, Long partId);
}
