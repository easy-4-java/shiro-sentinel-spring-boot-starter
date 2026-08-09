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
import java.io.PrintWriter;
import java.io.StringWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {{ @link CommonTotalFilter }}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("CommonTotalFilter Tests")
class CommonTotalFilterTest {

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        CommonTotalFilter instance = new CommonTotalFilter();
        assertThat(instance).isNotNull();
    }

    @Test
    @DisplayName("Constants have expected values")
    void testConstants() {
        assertThat(CommonTotalFilter.TOTAL_URL_REQUEST).isEqualTo("total-url-request");
        assertThat(CommonTotalFilter.WEB_SERVLET_CONTEXT_NAME).isEqualTo("sentinel_web_servlet_context");
    }

    @Test
    @DisplayName("init does not throw")
    void testInit() throws ServletException {
        CommonTotalFilter filter = new CommonTotalFilter();
        FilterConfig config = mock(FilterConfig.class);
        filter.init(config);
        // no exception expected
    }

    @Test
    @DisplayName("doFilter processes request through chain")
    void testDoFilter() throws IOException, ServletException {
        CommonTotalFilter filter = new CommonTotalFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/test");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter handles RuntimeException from chain")
    void testDoFilterHandlesRuntimeException() throws IOException, ServletException {
        CommonTotalFilter filter = new CommonTotalFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/test");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        doThrow(new RuntimeException("test")).when(chain).doFilter(any(), any());
        try {
            filter.doFilter(request, response, chain);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("test");
        }
    }

    @Test
    @DisplayName("destroy does not throw")
    void testDestroy() {
        CommonTotalFilter filter = new CommonTotalFilter();
        filter.destroy();
        // no exception expected
    }

    @Test
    @DisplayName("blockHandle writes 429 status and message")
    void testBlockHandle() throws Exception {
        CommonTotalFilter filter = new CommonTotalFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        com.alibaba.csp.sentinel.slots.block.flow.FlowRule rule = new com.alibaba.csp.sentinel.slots.block.flow.FlowRule();
        rule.setResource("test-resource");
        com.alibaba.csp.sentinel.slots.block.flow.FlowException blockException =
                new com.alibaba.csp.sentinel.slots.block.flow.FlowException("test", rule);
        filter.blockHandle(request, response, blockException);
        verify(response).setStatus(429);
        assertThat(stringWriter.toString()).contains("Blocked by Sentinel");
    }
}
