package com.findjob.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findjob.entity.RecruiterProfile;
import com.findjob.repository.RecruiterProfileRepository;

@Service
public class RecruiterProfileService {

	private final RecruiterProfileRepository recruiterProfileRepository;
	
	@Autowired
	public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository) {
		this.recruiterProfileRepository = recruiterProfileRepository;
	}
	
	public Optional<RecruiterProfile> getOne(Integer id){
		return recruiterProfileRepository.findById(id);
	}

	public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
		return recruiterProfileRepository.save(recruiterProfile);	
	}
}
