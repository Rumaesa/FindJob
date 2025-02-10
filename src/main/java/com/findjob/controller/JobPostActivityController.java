package com.findjob.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.findjob.dto.RecruiterJobsDto;
import com.findjob.entity.JobPostActivity;
import com.findjob.entity.RecruiterProfile;
import com.findjob.entity.Users;
import com.findjob.services.JobPostActivityService;
import com.findjob.services.UsersService;

@Controller
public class JobPostActivityController {
	
	private final UsersService usersService;
	private final JobPostActivityService activityService;
	@Autowired
	public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService) {
		this.usersService = usersService;
		this.activityService = jobPostActivityService;
	}
	
	@GetMapping("/dashboard/")
	public String searchJobs(Model model) {
		Object currentUserProfile = usersService.getCurrentUserProfile();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName =  authentication.getName();
			model.addAttribute("userName", currentUserName);
			if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
				List<RecruiterJobsDto> recruiterJobs = activityService.getRecruiterJobs(((RecruiterProfile) currentUserProfile).getUserAccountId());
				model.addAttribute("jobPost", recruiterJobs);
			}
		}
		model.addAttribute("user", currentUserProfile);
		
		return "dashboard";
	}
	
	@GetMapping("/dashboard/add")
	public String addJob(Model model) {
		model.addAttribute("jobPostActivity", new JobPostActivity());
		model.addAttribute("user", usersService.getCurrentUserProfile());
		return "add-jobs";
	}
	
	@PostMapping("/dashboard/addNew")
	public String addNew(JobPostActivity jobPostActivity, Model model) {
		Users user = usersService.getCurrentUser();
		if (user!=null) {
			jobPostActivity.setPostedById(user);
		}
		jobPostActivity.setPostedDate(new Date());
		model.addAttribute("jobPostActivity", jobPostActivity);
		JobPostActivity savedJob = activityService.addNew(jobPostActivity);
		return "redirect:/dashboard/";
	}

	@PostMapping("dashboard/edit/{id}")
	public String editJob(@PathVariable("id") int id, Model model) {
		JobPostActivity jobPostActivity = activityService.getOne(id);
		model.addAttribute("jobPostActivity", jobPostActivity);
		model.addAttribute("user", usersService.getCurrentUserProfile());
		return "add-jobs";
	}
}
