package com.findjob.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.findjob.entity.Users;
import com.findjob.entity.UsersType;
import com.findjob.services.UserTypeService;
import com.findjob.services.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UsersController {
	
	private final UserTypeService userTypeService;
	private final UsersService usersService;
	
	public UsersController(UserTypeService userTypeService, UsersService usersService) {
		this.userTypeService = userTypeService;
		this.usersService = usersService;
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		List<UsersType> usersTypes = userTypeService.getAll();
		model.addAttribute("getAllTypes", usersTypes);
		model.addAttribute("user", new Users());
		return "register";
	}

	@PostMapping("/register/new")
	public String userRegistration(@Valid Users user, Model model) {
//		System.out.println("User :: " +user);
		Optional<Users> optionalUser =  usersService.getUserByEmail(user.getEmail());
		if(optionalUser.isPresent()) {
			List<UsersType> usersTypes = userTypeService.getAll();
			model.addAttribute("getAllTypes", usersTypes);
			model.addAttribute("user", new Users());
			model.addAttribute("error", "Email already exists, Try to use another email.");
			return "register";
		}
		usersService.addNewUser(user);
		return "login";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		return "redirect:/";
	}
}
