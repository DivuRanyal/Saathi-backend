package controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogginFilters implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if necessary
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Set CORS headers
        res.setHeader("Access-Control-Allow-Credentials", "true");

        String origin = req.getHeader("Origin");
        System.out.println("Request Origin: " + origin);
        if (origin != null) {
            // Set the Origin header to allow cross-origin requests
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Methods", "DELETE, HEAD, GET, OPTIONS, POST, PUT");

            String headers = req.getHeader("Access-Control-Request-Headers");
            if (headers != null) {
                res.setHeader("Access-Control-Allow-Headers", headers);
            }

            // Set cache duration for preflight responses
            res.setHeader("Access-Control-Max-Age", "3600"); // Cache for 1 hour
        }

        // Handle preflight OPTIONS request
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Pass the request along the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup if necessary
    }
}
