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
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.StringUtil;

/**
 * Servlet filter that integrates with Sentinel for rate limiting.
 * This is a Jakarta Servlet compatible version of the Sentinel CommonFilter.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 * @see com.alibaba.csp.sentinel.adapter.servlet.CommonFilter
 */
public class CommonFilter implements Filter {

    /**
     * Specify whether the URL resource name should contain the HTTP method prefix (e.g. {@code POST:}).
     */
    public static final String HTTP_METHOD_SPECIFY = "HTTP_METHOD_SPECIFY";

    /**
     * If enabled, use the default context name, or else use the URL path as the context name.
     */
    public static final String WEB_CONTEXT_UNIFY = "WEB_CONTEXT_UNIFY";

    /**
     * Default context name for the web servlet.
     */
    public static final String WEB_SERVLET_CONTEXT_NAME = "sentinel_web_servlet_context";

    private static final String COLON = ":";
    private static final String EMPTY_ORIGIN = "";

    private boolean httpMethodSpecify = false;
    private boolean webContextUnify = true;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String httpMethodSpecifyParam = filterConfig.getInitParameter(HTTP_METHOD_SPECIFY);
        if (httpMethodSpecifyParam != null) {
            httpMethodSpecify = Boolean.parseBoolean(httpMethodSpecifyParam);
        }
        String webContextUnifyParam = filterConfig.getInitParameter(WEB_CONTEXT_UNIFY);
        if (webContextUnifyParam != null) {
            webContextUnify = Boolean.parseBoolean(webContextUnifyParam);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest sRequest = (HttpServletRequest) request;
        Entry urlEntry = null;

        try {
            String target = filterTarget(sRequest);

            if (!StringUtil.isEmpty(target)) {
                String origin = parseOrigin(sRequest);
                String contextName = webContextUnify ? WEB_SERVLET_CONTEXT_NAME : target;
                ContextUtil.enter(contextName, origin);

                if (httpMethodSpecify) {
                    String pathWithHttpMethod = sRequest.getMethod().toUpperCase() + COLON + target;
                    urlEntry = SphU.entry(pathWithHttpMethod, ResourceTypeConstants.COMMON_WEB, EntryType.IN);
                } else {
                    urlEntry = SphU.entry(target, ResourceTypeConstants.COMMON_WEB, EntryType.IN);
                }
            }
            chain.doFilter(request, response);
        } catch (BlockException e) {
            HttpServletResponse sResponse = (HttpServletResponse) response;
            blockHandle(sRequest, sResponse, e);
        } catch (IOException | ServletException | RuntimeException e2) {
            Tracer.traceEntry(e2, urlEntry);
            throw e2;
        } finally {
            if (urlEntry != null) {
                urlEntry.exit();
            }
            ContextUtil.exit();
        }
    }

    @Override
    public void destroy() {
        // no-op
    }

    /**
     * Filter and clean the target URL from the request.
     */
    private String filterTarget(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * Parse the request origin from the request.
     */
    private String parseOrigin(HttpServletRequest request) {
        String origin = EMPTY_ORIGIN;
        String originHeader = request.getHeader("Origin");
        if (originHeader != null && !originHeader.isEmpty()) {
            origin = originHeader;
        }
        if (StringUtil.isEmpty(origin)) {
            return EMPTY_ORIGIN;
        }
        return origin;
    }

    /**
     * Handle block exception by sending an error response.
     */
    void blockHandle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws IOException {
        response.setStatus(429);
        response.getWriter().write("Blocked by Sentinel: " + e.getRule().getResource());
    }

    public boolean isHttpMethodSpecify() {
        return httpMethodSpecify;
    }

    public void setHttpMethodSpecify(boolean httpMethodSpecify) {
        this.httpMethodSpecify = httpMethodSpecify;
    }

    public boolean isWebContextUnify() {
        return webContextUnify;
    }

    public void setWebContextUnify(boolean webContextUnify) {
        this.webContextUnify = webContextUnify;
    }

}
