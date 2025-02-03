package com.findjob.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

//	Once logged in successfully:
// 	1. retrieve the user object.
//	2. check the role for the user.
//	if role is jobSeeker or recruiter then send them to the dashboard page.
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		System.out.println("The user " + username + " is logged in");
		boolean hasJobSeekerRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("Job Seeker"));
		boolean hasJobRecruiterRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("Recruiter"));
		if(hasJobRecruiterRole || hasJobSeekerRole) {
			response.sendRedirect("/dashboard/");
		}
	}

}
