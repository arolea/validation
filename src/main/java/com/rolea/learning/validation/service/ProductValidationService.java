package com.rolea.learning.validation.service;

import com.rolea.learning.validation.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Slf4j
public class ProductValidationService {

    @Autowired
    private KieContainer kieContainer;

    public ValidationResult validateProduct(Product product, ProductAdditionalData data){
        KieSession kieSession = kieContainer.newKieSession();

        ValidationResult validationResult = ValidationResult.builder()
                .validationWarnings(new LinkedList<>())
                .validationErrors(new LinkedList<>())
                .build();
        kieSession.setGlobal("validationResult", validationResult);
        kieSession.setGlobal("additionalData", data);

        kieSession.insert(product);
        kieSession.fireAllRules();
        kieSession.dispose();

        return validationResult;
    }

    public Map<Product, ValidationResult> validateProducts(List<Product> products, ProductAdditionalData data){
        if(isEmpty(products)){
            return emptyMap();
        }

        Map<Product, ValidationResult> result = new HashMap<>();

        products.forEach(product -> {
            try {
                log.info("Validation product with id {}", product.getId());
                result.put(product, validateProduct(product, data));
                log.info("Successfully validated product with id {}", product.getId());
            } catch (Exception e){
                log.warn("Failed to validate product with id {}", product.getId(), e);
            }
        });

        return result;
    }

}
