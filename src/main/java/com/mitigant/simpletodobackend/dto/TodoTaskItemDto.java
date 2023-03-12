package com.mitigant.simpletodobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoTaskItemDto {

    public String description;
    public String dueDateTime;
}
