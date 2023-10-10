package com.ddang.ddang.configuration.initialization;

import com.ddang.ddang.region.application.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "data.init.region.enabled", havingValue = "true")
@RequiredArgsConstructor
public class InitializationRegionConfiguration implements ApplicationRunner {

    private final RegionService regionService;

    @Override
    public void run(final ApplicationArguments args) {
        regionService.createRegions();
    }
}
