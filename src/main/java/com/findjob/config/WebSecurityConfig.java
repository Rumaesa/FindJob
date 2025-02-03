package com.findjob.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;

import com.findjob.services.CustomUserDetailService;

@Configuration
public class WebSecurityConfig {
	
	private final CustomUserDetailService customUserDetailService;
	private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired	
	public WebSecurityConfig(CustomUserDetailService customUserDetailService, CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
		this.customUserDetailService = customUserDetailService;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	private String[] publicUrl = {"/",
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**", "/favicon.ico", "/resources/**", "/error"};
	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.authenticationProvider(AuthenticationProvider());
		
		httpSecurity.authorizeHttpRequests(auth -> {
			auth.requestMatchers(publicUrl).permitAll();
			auth.anyRequest().authenticated();
		});
		
//		Configuration for login form:
		httpSecurity.formLogin(form -> 
//					anyone can access /login request.
						form.loginPage("/login").permitAll()
						.successHandler(authenticationSuccessHandler))
					.logout(logout -> {
						logout.logoutUrl("/logout");
						logout.logoutSuccessUrl("/");
					}).cors(Customizer.withDefaults())
					.csrf(csrf -> csrf.disable());
		
		return httpSecurity.build();
	}

//	Custom authentication provider: 
//	Tell spring security how to find our users and how to authenticate password.
	@Bean
	public AuthenticationProvider AuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEcoder());
//		tells spring security how to retrieve the user from the DB:
		authenticationProvider.setUserDetailsService(customUserDetailService);
		return authenticationProvider;
	}

//	Custom password encoder:
//	Tells spring security how to authenticate the password( plain text or encryption)
	@Bean
	public PasswordEncoder passwordEcoder() {
		return new BCryptPasswordEncoder();
	}

}
