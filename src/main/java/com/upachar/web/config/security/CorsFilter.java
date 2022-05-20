package com.upachar.web.config.security;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		response.addHeader("Access-Control-Allow-Origin", "*");
		    response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
		    response.addHeader("Access-Control-Allow-Headers", 
		    		"Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
		    response.addHeader("Access-Control-Max-Age", "3600");
		    response.addHeader("Access-Control-Allow-Credentials", "true");
  
		if (request.getMethod().equals("OPTIONS")) {
			response.setStatus(200);
			response.getWriter().print("OK");
			response.getWriter().flush();
		} else {
			chain.doFilter(req, response);
		}
	}
	

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}

}

