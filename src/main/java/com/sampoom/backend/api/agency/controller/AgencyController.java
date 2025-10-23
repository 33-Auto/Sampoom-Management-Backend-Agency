package com.sampoom.backend.api.agency.controller;

import com.sampoom.backend.api.agency.dto.AgencyCreateRequestDTO;
import com.sampoom.backend.api.agency.dto.AgencyUpdateRequestDTO;
import com.sampoom.backend.api.agency.service.AgencyService;
import com.sampoom.backend.common.response.ApiResponse;
import com.sampoom.backend.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AgencyController {

    private final AgencyService agencyService;

    @Operation(summary = "대리점 생성", description = "새로운 대리점을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createAgency(
            @RequestBody AgencyCreateRequestDTO agencyCreateRequestDTO
    ) {
        agencyService.createAgency(agencyCreateRequestDTO);
        return ApiResponse.success(SuccessStatus.AGENCY_CREATE_SUCCESS, null);
    }

    @Operation(summary = "대리점 정보 수정", description = "대리점 정보를 수정하고 Outbox 이벤트를 기록합니다.")
    @PatchMapping("/{agencyId}")
    public ResponseEntity<ApiResponse<Void>> updateAgency(
            @PathVariable Long agencyId,
            @RequestBody AgencyUpdateRequestDTO agencyUpdateRequestDTO
    ) {
        agencyService.updateAgency(agencyId, agencyUpdateRequestDTO);
        return ApiResponse.success(SuccessStatus.AGENCY_UPDATE_SUCCESS, null);
    }

    @Operation(summary = "대리점 삭제", description = "대리점을 삭제하고 Outbox 이벤트를 기록합니다.")
    @DeleteMapping("/{agencyId}")
    public ResponseEntity<ApiResponse<Void>> deleteAgency(@PathVariable Long agencyId) {
        agencyService.deleteAgency(agencyId);
        return ApiResponse.success(SuccessStatus.AGENCY_DELETE_SUCCESS, null);
    }
}
