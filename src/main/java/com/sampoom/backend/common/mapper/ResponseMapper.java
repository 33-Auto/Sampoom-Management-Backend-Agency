package com.sampoom.backend.common.mapper;

//import com.sampoom.backend.api.order.dto.AgencyOrderFlatResponseDTO;
//import com.sampoom.backend.api.order.dto.AgencyOrderResponseDTO;
import com.sampoom.backend.common.dto.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

    // flat한 DTO를 카테고리-그룹-파트 구조로 변환
    public <T extends PartFlatDTO> List<CategoryResponseDTO> toNestedStructure(List<T> items) {

        // 카테고리 단위로 그룹핑
        Map<Long, List<T>> byCategory = items.stream()
                .collect(Collectors.groupingBy(T::getCategoryId));

        List<CategoryResponseDTO> categories = new ArrayList<>();

        for (Map.Entry<Long, List<T>> categoryEntry : byCategory.entrySet()) {
            List<T> categoryItems = categoryEntry.getValue();

            // 각 카테고리 내에서 그룹 단위로 그룹핑
            Map<Long, List<T>> byGroup = categoryItems.stream()
                    .collect(Collectors.groupingBy(T::getGroupId));

            List<GroupResponseDTO> groups = new ArrayList<>();

            for (Map.Entry<Long, List<T>> groupEntry : byGroup.entrySet()) {
                List<T> groupItems = groupEntry.getValue();

                // 각 그룹 내의 부품 리스트 구성
                List<PartResponseDTO> parts = groupItems.stream()
                        .map(p -> new PartResponseDTO(
                                p.getCartItemId(),
                                p.getOutboundId(),
                                p.getPartId(),
                                p.getPartCode(),
                                p.getPartName(),
                                p.getQuantity()
                        ))
                        .toList();

                groups.add(new GroupResponseDTO(
                        groupEntry.getKey(),
                        groupItems.get(0).getGroupName(),
                        parts
                ));
            }

            categories.add(new CategoryResponseDTO(
                    categoryEntry.getKey(),
                    categoryItems.get(0).getCategoryName(),
                    groups
            ));
        }

        return categories;
    }

//    // 주문 전용 mapper
//    public List<AgencyOrderResponseDTO> toNestedOrderStructure(List<AgencyOrderFlatResponseDTO> flatList) {
//        Map<Long, List<AgencyOrderFlatResponseDTO>> groupedByOrder = flatList.stream()
//                .collect(Collectors.groupingBy(AgencyOrderFlatResponseDTO::getOrderId));
//
//        return groupedByOrder.entrySet().stream()
//                .map(entry -> {
//                    AgencyOrderFlatResponseDTO first = entry.getValue().get(0);
//                    return AgencyOrderResponseDTO.builder()
//                            .orderId(first.getOrderId())
//                            .orderNumber(first.getOrderNumber())
//                            .createdAt(first.getCreatedAt())
//                            .status(first.getStatus())
//                            .agencyName(first.getAgencyName())
//                            .items(toNestedStructure(entry.getValue()))
//                            .build();
//                })
//                .sorted(Comparator.comparing(
//                        AgencyOrderResponseDTO::getCreatedAt,
//                        Comparator.nullsLast(Comparator.reverseOrder())
//                ))
////                .sorted(Comparator.comparing(AgencyOrderResponseDTO::getCreatedAt).reversed())
//                .collect(Collectors.toList());
//    }
}
