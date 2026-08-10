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

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link ShiroSentinelProperties }}.
 *
 * <p>Verifies default values, getters/setters and POJO contract.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ShiroSentinelProperties Tests")
class ShiroSentinelPropertiesTest {

    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testDefaultInstance() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        assertThat(props).isNotNull();
    }

    @Test
    @DisplayName("Default value of 'enabled' is false")
    void testDefaultEnabled() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        assertThat(props.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("Field 'enabled' can be set and read")
    void testEnabledField() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Default value of 'httpMethodSpecify' is false")
    void testDefaultHttpMethodSpecify() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        assertThat(props.isHttpMethodSpecify()).isFalse();
    }

    @Test
    @DisplayName("Field 'httpMethodSpecify' can be set and read")
    void testHttpMethodSpecifyField() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        props.setHttpMethodSpecify(true);
        assertThat(props.isHttpMethodSpecify()).isTrue();
    }

    @Test
    @DisplayName("Default value of 'webContextUnify' is true")
    void testDefaultWebContextUnify() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        assertThat(props.isWebContextUnify()).isTrue();
    }

    @Test
    @DisplayName("Field 'webContextUnify' can be set and read")
    void testWebContextUnifyField() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        props.setWebContextUnify(false);
        assertThat(props.isWebContextUnify()).isFalse();
    }

    @Test
    @DisplayName("Default 'authorityRules' is empty list")
    void testDefaultAuthorityRules() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        assertThat(props.getAuthorityRules()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Field 'authorityRules' can be set and read")
    void testAuthorityRulesField() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        List<AuthorityRule> rules = new ArrayList<>();
        rules.add(new AuthorityRule());
        props.setAuthorityRules(rules);
        assertThat(props.getAuthorityRules()).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("Default 'flowRules' is empty list")
    void testDefaultFlowRules() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        assertThat(props.getFlowRules()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Field 'flowRules' can be set and read")
    void testFlowRulesField() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        List<FlowRule> rules = new ArrayList<>();
        rules.add(new FlowRule());
        props.setFlowRules(rules);
        assertThat(props.getFlowRules()).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("Default 'degradeRules' is empty list")
    void testDefaultDegradeRules() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        assertThat(props.getDegradeRules()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Field 'degradeRules' can be set and read")
    void testDegradeRulesField() {
        ShiroSentinelProperties props = new ShiroSentinelProperties();
        List<DegradeRule> rules = new ArrayList<>();
        rules.add(new DegradeRule());
        props.setDegradeRules(rules);
        assertThat(props.getDegradeRules()).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("Public constant 'PREFIX' has expected value")
    void testPREFIXConstant() {
        assertThat(ShiroSentinelProperties.PREFIX).isEqualTo("shiro.sentinel");
    }
}
