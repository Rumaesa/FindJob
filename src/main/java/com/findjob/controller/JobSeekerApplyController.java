package com.findjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.findjob.entity.JobPostActivity;
import com.findjob.services.JobPostActivityService;
import com.findjob.services.UsersService;

@Controller
public class JobSeekerApplyController {

	private final JobPostActivityService jobPostActivityService;
	private final UsersService usersService;
	
	public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UsersService usersService) {
		this.jobPostActivityService = jobPostActivityService;
		this.usersService = usersService;
	}
	
	@GetMapping("job-details-apply/{id}")
	public String display(@PathVariable("id") int id, Model model) {
		JobPostActivity jobDetail = jobPostActivityService.getOne(id);
		model.addAttribute("jobDetails", jobDetail);
		model.addAttribute("user", usersService.getCurrentUserProfile());
		return "job-details";
	}
}
