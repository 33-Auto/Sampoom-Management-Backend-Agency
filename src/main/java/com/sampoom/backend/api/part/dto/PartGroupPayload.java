package com.sampoom.backend.api.part.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PartGroupPayload {
    private Long groupId;
    private String groupName;
    private String groupCode;
    private Long categoryId;
}

