package com.rolea.learning.validation.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAdditionalData {

    // if a given product is not on stock a validation warning should be provided
    private Map<Long, Long> productStockData;

}
