package com.sampoom.backend.api.agency.dto;

import com.sampoom.backend.api.agency.entity.AgencyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AgencyUpdateRequestDTO {

    private String name;
    private String address;
    private AgencyStatus status;
}
