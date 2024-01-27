package com.berkinozturk.event.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@Document
public class CategoryEntity implements Serializable {

    @Id
    private String id;
    private String categoryName;
    private String categoryDescription;
}
