package com.sampoom.backend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDTO {
    private Long groupId;
    private String groupName;
    private List<PartResponseDTO> parts;
}