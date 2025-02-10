package com.findjob.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.findjob.dto.RecruiterJobsDto;
import com.findjob.entity.IRecruiterJobs;
import com.findjob.entity.JobCompany;
import com.findjob.entity.JobLocation;
import com.findjob.entity.JobPostActivity;
import com.findjob.repository.JobPostActivityRepository;

@Service
public class JobPostActivityService {
	
	private final JobPostActivityRepository activityRepository;
	
	public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
		this.activityRepository = jobPostActivityRepository;
	}
	
	public JobPostActivity addNew(JobPostActivity jobPostActivity) {
		return activityRepository.save(jobPostActivity);
	}

	public List<RecruiterJobsDto> getRecruiterJobs(int recruiter){
		List<IRecruiterJobs> recruiterJobsDto = activityRepository.getRecruiterJobs(recruiter);
		List<RecruiterJobsDto> recruiterJobsDtosList = new ArrayList<>();
		for(IRecruiterJobs rec: recruiterJobsDto) {
			JobLocation jobLocation = new JobLocation(rec.getLocationId(), rec.getCity(), rec.getState(), rec.getCountry());
			JobCompany jobCompany = new JobCompany(rec.getCompanyId(), rec.getName(), "");
			recruiterJobsDtosList.add(new RecruiterJobsDto(rec.getTotalCandidates(), rec.getJob_post_id(), rec.getJob_title(), jobLocation, jobCompany));
		}
		return recruiterJobsDtosList;
	}
	
	public JobPostActivity getOne(int id) {
		return activityRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
	}
	}
