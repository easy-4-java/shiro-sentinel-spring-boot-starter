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
package org.apache.shiro.spring.boot.sentinel.web.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * Servlet filter for all requests that integrates with Sentinel for rate limiting.
 * This is a Jakarta Servlet compatible version of the Sentinel CommonTotalFilter.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 * @see com.alibaba.csp.sentinel.adapter.servlet.CommonTotalFilter
 */
public class CommonTotalFilter implements Filter {

    /**
     * Resource name for total URL requests.
     */
    public static final String TOTAL_URL_REQUEST = "total-url-request";

    /**
     * Default context name for the web servlet.
     */
    public static final String WEB_SERVLET_CONTEXT_NAME = "sentinel_web_servlet_context";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest sRequest = (HttpServletRequest) request;
        Entry entry = null;

        try {
            ContextUtil.enter(WEB_SERVLET_CONTEXT_NAME);
            entry = SphU.entry(TOTAL_URL_REQUEST, ResourceTypeConstants.COMMON_WEB);
            chain.doFilter(request, response);
        } catch (BlockException e) {
            HttpServletResponse sResponse = (HttpServletResponse) response;
            blockHandle(sRequest, sResponse, e);
        } catch (IOException | ServletException | RuntimeException e2) {
            Tracer.trace(e2);
            throw e2;
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    @Override
    public void destroy() {
        // no-op
    }

    /**
     * Handle block exception by sending an error response.
     */
    void blockHandle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws IOException {
        response.setStatus(429);
        response.getWriter().write("Blocked by Sentinel: " + e.getRule().getResource());
    }

}
