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

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;


@ConfigurationProperties(ShiroSentinelProperties.PREFIX)
/**\n * Configuration properties for Shiro Sentinel.\n *\n * @author <a href="https://github.com/loong10k">Loong Wan</a>\n * @since 1.0.0\n */
public class ShiroSentinelProperties{

	public static final String PREFIX = "shiro.sentinel";

	private boolean enabled = false;

    private boolean httpMethodSpecify = false;
    
    private boolean webContextUnify = true;
    
	private List<AuthorityRule> authorityRules = new ArrayList<>();
	
	private List<FlowRule> flowRules = new ArrayList<>();
	
	private List<DegradeRule> degradeRules = new ArrayList<>();

	/**
	 * Returns the enabled.
	 *
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled.
	 *
	 * @param enabled the enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Returns the http method specify.
	 *
	 * @return the http method specify
	 */
	public boolean isHttpMethodSpecify() {
		return httpMethodSpecify;
	}

	/**
	 * Sets the http method specify.
	 *
	 * @param httpMethodSpecify the http method specify
	 */
	public void setHttpMethodSpecify(boolean httpMethodSpecify) {
		this.httpMethodSpecify = httpMethodSpecify;
	}

	/**
	 * Returns the web context unify.
	 *
	 * @return the web context unify
	 */
	public boolean isWebContextUnify() {
		return webContextUnify;
	}

	/**
	 * Sets the web context unify.
	 *
	 * @param webContextUnify the web context unify
	 */
	public void setWebContextUnify(boolean webContextUnify) {
		this.webContextUnify = webContextUnify;
	}

	/**
	 * Returns the authority rules.
	 *
	 * @return the authority rules
	 */
	public List<AuthorityRule> getAuthorityRules() {
		return authorityRules;
	}

	/**
	 * Sets the authority rules.
	 *
	 * @param authorityRules the authority rules
	 */
	public void setAuthorityRules(List<AuthorityRule> authorityRules) {
		this.authorityRules = authorityRules;
	}

	/**
	 * Returns the flow rules.
	 *
	 * @return the flow rules
	 */
	public List<FlowRule> getFlowRules() {
		return flowRules;
	}

	/**
	 * Sets the flow rules.
	 *
	 * @param flowRules the flow rules
	 */
	public void setFlowRules(List<FlowRule> flowRules) {
		this.flowRules = flowRules;
	}

	/**
	 * Returns the degrade rules.
	 *
	 * @return the degrade rules
	 */
	public List<DegradeRule> getDegradeRules() {
		return degradeRules;
	}

	/**
	 * Sets the degrade rules.
	 *
	 * @param degradeRules the degrade rules
	 */
	public void setDegradeRules(List<DegradeRule> degradeRules) {
		this.degradeRules = degradeRules;
	}
	
}
