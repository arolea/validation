package com.rolea.learning.validation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private Long id;
    // each product must have a nun-null name
    private String name;
    // each product must have a positive price
    private Double price;

    // each product must have at least one category associated with it
    // a product can't be in both 'Category 1' and 'Category 2' at the same time
    private List<Category> categories;

}
