package com.pizzashop.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestFilter implements Filter {
	
	public RequestFilter() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		
		res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		res.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		res.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		
		if (!(req.getMethod().equalsIgnoreCase("OPTIONS"))) {
			try {
				log.info("Not pre-flight request, passing to next filter");
				chain.doFilter(req, res); // go to next filter
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else {
			log.info("Pre-flight request");
			res.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE");
            res.setHeader("Access-Control-Max-Age", "3600");
            res.setHeader("Access-Control-Allow-Headers", "authorization, content-type," +
                    "access-control-request-headers,access-control-request-method,accept,origin,authorization,x-requested-with");
            res.setStatus(HttpServletResponse.SC_OK);
		}

	}

}
