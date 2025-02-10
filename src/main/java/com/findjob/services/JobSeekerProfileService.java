package com.findjob.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.findjob.entity.JobSeekerProfile;
import com.findjob.repository.JobSeekerProfileRepository;

@Service
public class JobSeekerProfileService {
	
	private JobSeekerProfileRepository jobSeekerProfileRepository;
	
	public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository) {
		this.jobSeekerProfileRepository = jobSeekerProfileRepository;
	}
	
	public Optional<JobSeekerProfile> getOne(Integer id){
		return jobSeekerProfileRepository.findById(id);
	}

	public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
		return jobSeekerProfileRepository.save(jobSeekerProfile);
	}

}
