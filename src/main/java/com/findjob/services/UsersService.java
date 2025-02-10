package com.findjob.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.findjob.entity.JobSeekerProfile;
import com.findjob.entity.RecruiterProfile;
import com.findjob.entity.Users;
import com.findjob.repository.JobSeekerProfileRepository;
import com.findjob.repository.RecruiterProfileRepository;
import com.findjob.repository.UsersRepository;

@Service
public class UsersService {
	
	private final UsersRepository usersRepository;
	private final RecruiterProfileRepository recruiterProfileRepository;
	private final JobSeekerProfileRepository jobSeekerProfileRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UsersService(UsersRepository usersRepository, RecruiterProfileRepository recruiterProfileRepository, JobSeekerProfileRepository jobSeekerProfileRepository, PasswordEncoder passwordEncoder) {
		this.usersRepository = usersRepository;
		this.recruiterProfileRepository = recruiterProfileRepository;
		this.jobSeekerProfileRepository = jobSeekerProfileRepository;
//		it will inject the BCryptPasswordEncoder that we have configured in WebSecurityConfig class.
		this.passwordEncoder = passwordEncoder;
	}
		
	public Users addNewUser(Users user) {
		user.setActive(true);
		user.setRegistrationDate(new Date(System.currentTimeMillis()));
//		setting password in encoded format:
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Users savedUser= usersRepository.save(user);
		int userTypeId = user.getUserTypeId().getUserTypeId();
		if(userTypeId == 1) {
			recruiterProfileRepository.save(new RecruiterProfile(savedUser));
		} else {
			jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser));
		}
		return savedUser;
	}
	
	public Object getCurrentUserProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String username = authentication.getName();
			Users users = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found "+ username + " user."));
			int userId = users.getUserId();
			if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
				RecruiterProfile recruiterProfile = recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
				return recruiterProfile;
			} else {
				JobSeekerProfile jobSeekerProfile = jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
				return jobSeekerProfile;
			}
		}
		return null;
	}
	
	public Optional<Users> getUserByEmail(String email){
		return usersRepository.findByEmail(email);
	}

	public Users getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String userName = authentication.getName();
			Users user = usersRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("Could not found "+ userName + " user."));
			return user;
		}
		return null;
	}

}
