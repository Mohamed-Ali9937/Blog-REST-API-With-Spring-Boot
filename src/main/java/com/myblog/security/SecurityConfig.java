package com.myblog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.myblog.exception.JwtAuthenticatoinEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	private UserDetailsService userDetailsSerivce;
	private JwtTokenUtils jwtTokenUtils;
	
	@Autowired
	public SecurityConfig(UserDetailsService userDetailsSerivce, JwtTokenUtils jwtTokenUtils) {
		this.userDetailsSerivce = userDetailsSerivce; 
		this.jwtTokenUtils = jwtTokenUtils;
	}
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(userDetailsSerivce);
		
		return authenticationProvider;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
		.authorizeHttpRequests(authorize -> 
		authorize.requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
		.requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
		.requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
		.requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
		.requestMatchers("/api/login").permitAll()
		.requestMatchers("/api/register").permitAll()
		.anyRequest().authenticated())
		
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		
		.addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtils),
				UsernamePasswordAuthenticationFilter.class)
		
		.exceptionHandling(exception -> 
					exception.authenticationEntryPoint(new JwtAuthenticatoinEntryPoint()));
		
		return http.build();
	}
	
}
