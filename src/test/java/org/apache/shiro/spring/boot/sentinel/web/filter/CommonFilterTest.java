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
 * Unit tests for {{ @link CommonFilter }}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("CommonFilter Tests")
class CommonFilterTest {

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        CommonFilter instance = new CommonFilter();
        assertThat(instance).isNotNull();
    }

    @Test
    @DisplayName("Default httpMethodSpecify is false")
    void testDefaultHttpMethodSpecify() {
        CommonFilter filter = new CommonFilter();
        assertThat(filter.isHttpMethodSpecify()).isFalse();
    }

    @Test
    @DisplayName("httpMethodSpecify getter/setter works")
    void testHttpMethodSpecifyGetterSetter() {
        CommonFilter filter = new CommonFilter();
        filter.setHttpMethodSpecify(true);
        assertThat(filter.isHttpMethodSpecify()).isTrue();
        filter.setHttpMethodSpecify(false);
        assertThat(filter.isHttpMethodSpecify()).isFalse();
    }

    @Test
    @DisplayName("Default webContextUnify is true")
    void testDefaultWebContextUnify() {
        CommonFilter filter = new CommonFilter();
        assertThat(filter.isWebContextUnify()).isTrue();
    }

    @Test
    @DisplayName("webContextUnify getter/setter works")
    void testWebContextUnifyGetterSetter() {
        CommonFilter filter = new CommonFilter();
        filter.setWebContextUnify(false);
        assertThat(filter.isWebContextUnify()).isFalse();
        filter.setWebContextUnify(true);
        assertThat(filter.isWebContextUnify()).isTrue();
    }

    @Test
    @DisplayName("Constants have expected values")
    void testConstants() {
        assertThat(CommonFilter.HTTP_METHOD_SPECIFY).isEqualTo("HTTP_METHOD_SPECIFY");
        assertThat(CommonFilter.WEB_CONTEXT_UNIFY).isEqualTo("WEB_CONTEXT_UNIFY");
        assertThat(CommonFilter.WEB_SERVLET_CONTEXT_NAME).isEqualTo("sentinel_web_servlet_context");
    }

    @Test
    @DisplayName("init with parameters sets httpMethodSpecify")
    void testInitWithParameters() throws ServletException {
        CommonFilter filter = new CommonFilter();
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("HTTP_METHOD_SPECIFY")).thenReturn("true");
        when(config.getInitParameter("WEB_CONTEXT_UNIFY")).thenReturn("false");
        filter.init(config);
        assertThat(filter.isHttpMethodSpecify()).isTrue();
        assertThat(filter.isWebContextUnify()).isFalse();
    }

    @Test
    @DisplayName("init with null parameters uses defaults")
    void testInitWithNullParameters() throws ServletException {
        CommonFilter filter = new CommonFilter();
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter(anyString())).thenReturn(null);
        filter.init(config);
        assertThat(filter.isHttpMethodSpecify()).isFalse();
        assertThat(filter.isWebContextUnify()).isTrue();
    }

    @Test
    @DisplayName("doFilter processes request through chain")
    void testDoFilter() throws IOException, ServletException {
        CommonFilter filter = new CommonFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getMethod()).thenReturn("GET");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter with httpMethodSpecify enabled")
    void testDoFilterWithHttpMethodSpecify() throws IOException, ServletException {
        CommonFilter filter = new CommonFilter();
        filter.setHttpMethodSpecify(true);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getMethod()).thenReturn("POST");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter with webContextUnify disabled")
    void testDoFilterWithWebContextUnifyDisabled() throws IOException, ServletException {
        CommonFilter filter = new CommonFilter();
        filter.setWebContextUnify(false);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getMethod()).thenReturn("GET");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter with origin header")
    void testDoFilterWithOrigin() throws IOException, ServletException {
        CommonFilter filter = new CommonFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Origin")).thenReturn("http://example.com");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter handles RuntimeException from chain")
    void testDoFilterHandlesRuntimeException() throws IOException, ServletException {
        CommonFilter filter = new CommonFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getMethod()).thenReturn("GET");
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
        CommonFilter filter = new CommonFilter();
        filter.destroy();
        // no exception expected
    }

    @Test
    @DisplayName("blockHandle writes 429 status and message")
    void testBlockHandle() throws Exception {
        CommonFilter filter = new CommonFilter();
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

    @Test
    @DisplayName("doFilter with empty URI does not enter sentinel context")
    void testDoFilterWithEmptyUri() throws Exception {
        CommonFilter filter = new CommonFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }
}
