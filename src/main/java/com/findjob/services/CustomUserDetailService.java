package com.findjob.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.findjob.entity.Users;
import com.findjob.repository.UsersRepository;
import com.findjob.util.CustomUserDetails;

@Service
public class CustomUserDetailService implements UserDetailsService{

	private final UsersRepository usersRepository;
	
	@Autowired
	public CustomUserDetailService(UsersRepository usersRepository) {
			this.usersRepository = usersRepository;
		}



//	Tells spring security how to retrieve the users from the DB:
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		since we are not using Optional, we used orElseThrow
		Users users = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found the user"));
		return new CustomUserDetails(users);
	}

}
