package com.kano.project.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Page {
    private Long pageSize = 20L;
    private Long page = 1L;
}
