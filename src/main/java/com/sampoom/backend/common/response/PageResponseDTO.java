package com.sampoom.backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> content;     // 실제 데이터
    private long totalElements;  // 총 데이터 개수
    private int totalPages;      // 총 페이지 수
    private int currentPage;     // 현재 페이지 번호
}
