package com.sampoom.backend.api.cart.repository;

import com.sampoom.backend.api.cart.entity.AgencyCartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AgencyCartItemRepository extends CrudRepository<AgencyCartItem, Long> {

    // 대리점 ID로 장바구니 항목 전체 조회
    List<AgencyCartItem> findByAgency_Id(Long agencyId);

    // 대리점 ID + 부품 ID로 단일 항목 조회
    Optional<AgencyCartItem> findByAgency_IdAndPart_Id(Long agencyId, Long partId);

    // 대리점 ID + 부품 ID로 항목 삭제
    void deleteByAgency_IdAndPart_Id(Long agencyId, Long partId);

    void deleteAllByAgencyId(Long agencyId);
}
