package com.findjob.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findjob.entity.UsersType;
import com.findjob.repository.UsersTypeRepository;

@Service
public class UserTypeService {
	
	private final UsersTypeRepository usersTypeRepository;
	
//	Constructor Injection:
	@Autowired
	public UserTypeService(UsersTypeRepository usersTypeRepository) {
		this.usersTypeRepository = usersTypeRepository;
	}
	
	public List<UsersType> getAll(){
//		findAll() is given by JPARepository Interface
		return usersTypeRepository.findAll();
	}

}
