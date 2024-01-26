package com.berkinozturk.event.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Category {
    private String categoryName;
    private String categoryDescription;
}
