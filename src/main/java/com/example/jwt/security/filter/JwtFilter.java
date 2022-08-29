package com.example.jwt.security.filter;

import com.example.jwt.service.MyUserService;
import com.example.jwt.util.MyJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private MyJwtUtil jwtUtil;
	@Autowired
	private MyUserService myUserService;

	@Override
	protected void doFilterInternal (
	 HttpServletRequest request,
	 HttpServletResponse response,
	 FilterChain filterChain)
	 throws ServletException, IOException {
		// get Bearer token from header
		final String header = request.getHeader("Authorization");
		String jwtToken = null;
		String username = null;

		if(header != null && header.startsWith("Bearer ")) {
			jwtToken = header.substring(7);
			username = jwtUtil.getUsernameFromToken(jwtToken);
		}

		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = myUserService.loadUserByUsername(username);

			if(jwtUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken token = new
				 UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());

				token.setDetails(
				 new WebAuthenticationDetailsSource().buildDetails(request)
				);

				SecurityContextHolder.getContext().setAuthentication(token);
			}
		}

		filterChain.doFilter(request, response);
	}
}
