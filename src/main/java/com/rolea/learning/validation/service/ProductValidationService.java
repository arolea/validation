package com.rolea.learning.validation.service;

import com.rolea.learning.validation.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionsPool;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Slf4j
public class ProductValidationService {

    @Autowired
    private KieContainer kieContainer;

    @Autowired
    private KieSessionsPool kieSessionsPool;

    @Autowired
    private RuleFiredEventListener eventListener;

    public Map<Product, ValidationResult> validateProductsStateful(List<Product> products, ProductAdditionalData data) {
        if (isEmpty(products)) {
            return emptyMap();
        }

        // create a new stateful KIE session
        KieSession kieSession = kieContainer.newKieSession();

        // add some rule engine execution logging
        kieSession.addEventListener(eventListener);

        // initialize and set the result container and the additional data
        ValidationResultContainer validationResultContainer = ValidationResultContainer.builder()
                .build();
        validationResultContainer.initializeContainer(products);
        kieSession.setGlobal("resultContainer", validationResultContainer);
        kieSession.setGlobal("additionalData", data);

        // add facts to the session
        products.forEach(kieSession::insert);

        // execute the rules
        kieSession.fireAllRules();

        // detach the event listener
        kieSession.removeEventListener(eventListener);

        // avoid any memory leaks
        kieSession.dispose();

        // get the validation result
        return validationResultContainer.getValidationResultMap();
    }

    public Map<Product, ValidationResult> validateProductsStateless(List<Product> products, ProductAdditionalData data) {
        if (isEmpty(products)) {
            return emptyMap();
        }

        // create a new stateless KIE session
        StatelessKieSession kieSession = kieSessionsPool.newStatelessKieSession();

        // add some rule engine execution logging
        kieSession.addEventListener(eventListener);

        // initialize and set the result container and the additional data
        ValidationResultContainer validationResultContainer = ValidationResultContainer.builder()
                .build();
        validationResultContainer.initializeContainer(products);
        kieSession.setGlobal("resultContainer", validationResultContainer);
        kieSession.setGlobal("additionalData", data);

        // add facts to the session, no calls to fireAllRules() and dispose() are required
        kieSession.execute(products);

        // detach the event listener
        kieSession.removeEventListener(eventListener);

        // get the validation result
        return validationResultContainer.getValidationResultMap();
    }

}
