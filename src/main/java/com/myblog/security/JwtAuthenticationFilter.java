package com.myblog.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.myblog.MyApplicationContext;
import com.myblog.entity.UserEntity;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtTokenUtils jwtTokenUtils;
	
	public JwtAuthenticationFilter(JwtTokenUtils jwtTokenUtils) {
		this.jwtTokenUtils = jwtTokenUtils;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain) throws ServletException, IOException {
		
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if(StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			
			String token = header.replace("Bearer ", "");
			
			if(!jwtTokenUtils.isTokenValid(token)) {
				filterChain.doFilter(request, response);
			}
			
			String username = jwtTokenUtils.getUsername(token);
						
			UserRepository userRepository = MyApplicationContext.getBean("userRepository", UserRepository.class);
			
			UserEntity userEntity = userRepository.findByEmail(username)
					.orElseThrow(()-> new ResourceNotFoundException("User", "Email", username));
			
			UserPrincipal userPrincipal = new UserPrincipal(userEntity);
			
			Authentication authenticationToken = 
					new UsernamePasswordAuthenticationToken(userPrincipal, null,
							userPrincipal.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		
		filterChain.doFilter(request, response);

	}

}
