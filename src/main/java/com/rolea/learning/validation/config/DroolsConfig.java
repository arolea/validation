package com.rolea.learning.validation.config;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.springframework.core.io.support.ResourcePatternUtils.getResourcePatternResolver;

@Configuration
@Slf4j
public class DroolsConfig {

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public KieContainer kieContainer() throws IOException {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        Arrays.stream(getResourcePatternResolver(resourceLoader).getResources("classpath:rules/*"))
                .forEach(resource -> {
                    log.info("Loading rule file: {}", resource.getFilename());
                    try {
                        kieFileSystem.write(ResourceFactory.newFileResource(new File(resource.getURI())));
                    } catch (IOException e) {
                        log.warn("Unable to load file: {}", resource.getFilename());
                    }
                });

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}