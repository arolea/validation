package com.rolea.learning.validation.service;

import com.rolea.learning.validation.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.springframework.util.CollectionUtils.isEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ValidationResultContainer {

    private Map<Product, ValidationResult> validationResultMap;

    public void initializeContainer(List<Product> products) {
        validationResultMap = new LinkedHashMap<>();
        if (!isEmpty(products)) {
            products.forEach(this::initializeValidationResult);
        }
    }

    public void addValidationWarning(Product product, String message) {
        synchronized (validationResultMap.get(product)){
            validationResultMap.get(product).addValidationWarning(ValidationWarning.builder()
                    .message(message)
                    .build());;
        }
    }

    public void addValidationError(Product product, String message) {
        synchronized (validationResultMap.get(product)){
            validationResultMap.get(product).addValidationError(ValidationError.builder()
                    .message(message)
                    .build());;
        }
    }

    private void initializeValidationResult(Product product) {
        log.info("Initializing validation result for product with id {}", product.getId());
        ValidationResult validationResult = ValidationResult.builder()
                .validationWarnings(new LinkedList<>())
                .validationErrors(new LinkedList<>())
                .build();
        validationResultMap.put(product, validationResult);
    }

}
