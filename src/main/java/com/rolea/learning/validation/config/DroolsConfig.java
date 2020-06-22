package com.rolea.learning.validation.config;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSessionsPool;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

@Configuration
@Slf4j
public class DroolsConfig {

    @Value("${rules.directory}")
    private String rulesDirectory;

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        File rulesDirectory = getRulesDirectory();
        loadRules(kieFileSystem, rulesDirectory);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    @Bean
    public KieSessionsPool kieSessionsPool() {
        return kieContainer().newKieSessionsPool(10);
    }

    private File getRulesDirectory() {
        return Optional.ofNullable(getClass().getClassLoader().getResource(rulesDirectory))
                .map(directoryUrl -> new File(directoryUrl.getFile()))
                .orElseThrow(() -> new RuntimeException("Unable to locate rule directory"));
    }

    private void loadRules(KieFileSystem kieFileSystem, File rulesDirectory) {
        Optional.ofNullable(rulesDirectory.list())
                .ifPresent(ruleFiles -> Arrays.stream(ruleFiles)
                        .forEach(ruleFile -> kieFileSystem.write(loadRule(rulesDirectory, ruleFile))));
    }

    private Resource loadRule(File folder, String ruleFile) {
        String currentRule = folder.getPath() + File.separator + ruleFile;
        log.info("Loading rule {}", currentRule);
        return ResourceFactory.newFileResource(new File(currentRule));
    }

}