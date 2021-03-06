/*
 *
 *  Copyright 2019 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.genie.web.spring.autoconfigure.agent.launchers;

import com.netflix.genie.web.agent.launchers.impl.LocalAgentLauncherImpl;
import com.netflix.genie.web.data.services.JobSearchService;
import com.netflix.genie.web.introspection.GenieWebHostInfo;
import com.netflix.genie.web.introspection.GenieWebRpcInfo;
import com.netflix.genie.web.properties.LocalAgentLauncherProperties;
import com.netflix.genie.web.util.ExecutorFactory;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

/**
 * Tests for {@link AgentLaunchersAutoConfiguration}.
 *
 * @author tgianos
 * @since 4.0.0
 */
class AgentLaunchersAutoConfigurationTest {

    private ApplicationContextRunner contextRunner =
        new ApplicationContextRunner()
            .withConfiguration(
                AutoConfigurations.of(
                    AgentLaunchersAutoConfiguration.class
                )
            )
            .withUserConfiguration(UserConfig.class);

    /**
     * All the expected beans should exist when the auto configuration is applied.
     */
    @Test
    void testExpectedBeansExist() {
        this.contextRunner.run(
            context -> {
                Assertions.assertThat(context).hasSingleBean(LocalAgentLauncherProperties.class);
                Assertions.assertThat(context).hasSingleBean(ExecutorFactory.class);
                Assertions.assertThat(context).hasSingleBean(LocalAgentLauncherImpl.class);
            }
        );
    }

    static class UserConfig {
        @Bean
        GenieWebHostInfo genieWebHostInfo() {
            return new GenieWebHostInfo(UUID.randomUUID().toString());
        }

        @Bean
        GenieWebRpcInfo genieWebRpcInfo() {
            return new GenieWebRpcInfo(33_433);
        }

        @Bean
        JobSearchService jobSearchService() {
            return Mockito.mock(JobSearchService.class);
        }

        @Bean
        MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }
}
