package com.sampoom.backend.api.agency.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencyUpdatedEvent {

    private Long agencyId;
    private String name;
    private String address;
    private String status;
    private boolean deleted;
}
