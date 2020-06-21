package com.rolea.learning.validation.service;

import com.rolea.learning.validation.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;

@Slf4j
public class RuleFiredEventListener extends DefaultAgendaEventListener {

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        String ruleName = event.getMatch().getRule().getName();
        Long productId = ((Product) event.getMatch().getObjects().get(0)).getId();
        log.info("Rule {} fired for product with id {}", ruleName, productId);
    }

}
