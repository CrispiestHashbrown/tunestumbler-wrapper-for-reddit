package ca.tunestumbler.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import ca.tunestumbler.api.shared.SharedUtils;

public class GlobalHeadersFilter extends OncePerRequestFilter {

	@Autowired
	SharedUtils sharedUtils;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.addHeader("Cache-Control",
				"private, s-maxage=0, max-age=0, must-revalidate, max-age=0, must-revalidate");
		response.addHeader("Content-Type", "application/json; charset=UTF-8");
		response.addHeader("Expires", "0");
		response.addHeader("X-Content-Type-Options", "nosniff");
		response.addHeader("X-Frame-Options", "SAMEORIGIN");
		response.addHeader("X-XSS-Protection", "1; mode=block");
		response.addHeader("Strict-Transport-Security", "max-age=15552000; includeSubDomains; preload");
		response.addHeader("Date", sharedUtils.getCurrentTime());
	}

}
