/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.shiro.spring.boot;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link ShiroSentinelAutoConfiguration }}.
 *
 * <p>Verifies the auto-configuration activates under the expected conditions
 * and exposes its declared beans.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ShiroSentinelAutoConfiguration Tests")
class ShiroSentinelAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner();

    @Test
    @DisplayName("Auto-configuration class can be instantiated")
    void testInstantiation() {
        ShiroSentinelAutoConfiguration configuration = new ShiroSentinelAutoConfiguration();
        assertThat(configuration).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration loads when 'shiro.sentinel.enabled=true'")
    void testLoadsWhenEnabledPropertySet() {
        runner.withUserConfiguration(ShiroSentinelAutoConfiguration.class)
                .withPropertyValues("shiro.sentinel.enabled=true")
                .run(context -> assertThat(context).hasSingleBean(ShiroSentinelAutoConfiguration.class));
    }

    @Test
    @DisplayName("Auto-configuration is absent when property is not set")
    void testNotLoadedWhenPropertyAbsent() {
        runner.withUserConfiguration(ShiroSentinelAutoConfiguration.class)
                .run(context -> assertThat(context).doesNotHaveBean(ShiroSentinelAutoConfiguration.class));
    }

    @Test
    @DisplayName("commonFilter bean is registered")
    void testCommonFilterBean() {
        runner.withUserConfiguration(ShiroSentinelAutoConfiguration.class)
                .withPropertyValues("shiro.sentinel.enabled=true")
                .run(context -> {
                    assertThat(context).hasBean("origin-sentinel");
                    FilterRegistrationBean<?> bean = context.getBean("origin-sentinel", FilterRegistrationBean.class);
                    assertThat(bean).isNotNull();
                });
    }

    @Test
    @DisplayName("commonTotalFilter bean is registered")
    void testCommonTotalFilterBean() {
        runner.withUserConfiguration(ShiroSentinelAutoConfiguration.class)
                .withPropertyValues("shiro.sentinel.enabled=true")
                .run(context -> {
                    assertThat(context).hasBean("total-sentinel");
                    FilterRegistrationBean<?> bean = context.getBean("total-sentinel", FilterRegistrationBean.class);
                    assertThat(bean).isNotNull();
                });
    }

    @Test
    @DisplayName("afterPropertiesSet with empty rules does not throw")
    void testAfterPropertiesSetWithEmptyRules() throws Exception {
        ShiroSentinelAutoConfiguration configuration = new ShiroSentinelAutoConfiguration();
        ShiroSentinelProperties properties = new ShiroSentinelProperties();
        // Use reflection to set the shiroSentinelProperties field
        java.lang.reflect.Field field = ShiroSentinelAutoConfiguration.class.getDeclaredField("shiroSentinelProperties");
        field.setAccessible(true);
        field.set(configuration, properties);
        configuration.afterPropertiesSet();
        // no exception expected
    }

    @Test
    @DisplayName("afterPropertiesSet with authority rules")
    void testAfterPropertiesSetWithAuthorityRules() throws Exception {
        ShiroSentinelAutoConfiguration configuration = new ShiroSentinelAutoConfiguration();
        ShiroSentinelProperties properties = new ShiroSentinelProperties();
        // Set authorityRules field
        java.lang.reflect.Field authField = ShiroSentinelAutoConfiguration.class.getDeclaredField("authorityRules");
        authField.setAccessible(true);
        List<AuthorityRule> authRules = new ArrayList<>();
        authRules.add(new AuthorityRule());
        authField.set(configuration, authRules);
        // Set shiroSentinelProperties field
        java.lang.reflect.Field propsField = ShiroSentinelAutoConfiguration.class.getDeclaredField("shiroSentinelProperties");
        propsField.setAccessible(true);
        propsField.set(configuration, properties);
        configuration.afterPropertiesSet();
        // no exception expected
    }

    @Test
    @DisplayName("afterPropertiesSet with flow rules")
    void testAfterPropertiesSetWithFlowRules() throws Exception {
        ShiroSentinelAutoConfiguration configuration = new ShiroSentinelAutoConfiguration();
        ShiroSentinelProperties properties = new ShiroSentinelProperties();
        // Set flowRules field
        java.lang.reflect.Field flowField = ShiroSentinelAutoConfiguration.class.getDeclaredField("flowRules");
        flowField.setAccessible(true);
        List<FlowRule> flowRules = new ArrayList<>();
        flowRules.add(new FlowRule());
        flowField.set(configuration, flowRules);
        // Set shiroSentinelProperties field
        java.lang.reflect.Field propsField = ShiroSentinelAutoConfiguration.class.getDeclaredField("shiroSentinelProperties");
        propsField.setAccessible(true);
        propsField.set(configuration, properties);
        configuration.afterPropertiesSet();
        // no exception expected
    }

    @Test
    @DisplayName("afterPropertiesSet with degrade rules")
    void testAfterPropertiesSetWithDegradeRules() throws Exception {
        ShiroSentinelAutoConfiguration configuration = new ShiroSentinelAutoConfiguration();
        ShiroSentinelProperties properties = new ShiroSentinelProperties();
        // Set degradeRules field
        java.lang.reflect.Field degradeField = ShiroSentinelAutoConfiguration.class.getDeclaredField("degradeRules");
        degradeField.setAccessible(true);
        List<DegradeRule> degradeRules = new ArrayList<>();
        degradeRules.add(new DegradeRule());
        degradeField.set(configuration, degradeRules);
        // Set shiroSentinelProperties field
        java.lang.reflect.Field propsField = ShiroSentinelAutoConfiguration.class.getDeclaredField("shiroSentinelProperties");
        propsField.setAccessible(true);
        propsField.set(configuration, properties);
        configuration.afterPropertiesSet();
        // no exception expected
    }
}
