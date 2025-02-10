package com.findjob.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.findjob.entity.JobSeekerProfile;
import com.findjob.entity.Skills;
import com.findjob.entity.Users;
import com.findjob.repository.UsersRepository;
import com.findjob.services.JobSeekerProfileService;
import com.findjob.services.UsersService;
import com.findjob.util.FileUploadUtil;

@Controller
@RequestMapping("job-seeker-profile")
public class JobSeekerProfileController {

	private JobSeekerProfileService jobSeekerProfileService;
	private UsersRepository usersRepository;
	
	@Autowired
	public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UsersRepository usersService) {
		this.jobSeekerProfileService = jobSeekerProfileService;
		this.usersRepository = usersService;
	}
	
	@GetMapping("/")
	public String jobSeekerProfile(Model model) {
		JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<Skills> skills = new ArrayList<>();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
			Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
			if(seekerProfile.isPresent()) {
				jobSeekerProfile = seekerProfile.get();
				if(jobSeekerProfile.getSkills().isEmpty()) {
					skills.add(new Skills());
					jobSeekerProfile.setSkills(skills);
				}
			}
			model.addAttribute("profile", jobSeekerProfile);
			model.addAttribute("skills", skills);
		}
		return "job-seeker-profile";
	}
	
	@PostMapping("/addNew")
	public String addNew(JobSeekerProfile jobSeekerProfile,@RequestParam("image") MultipartFile image, @RequestParam("pdf") MultipartFile pdf, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
			jobSeekerProfile.setUserId(user);
			jobSeekerProfile.setUserAccountId(user.getUserId());
		}
		List<Skills> skills = new ArrayList<>();
		model.addAttribute("profile", jobSeekerProfile);
		model.addAttribute("skills", skills);
		for(Skills skill: jobSeekerProfile.getSkills()) {
			skill.setJobSeekerProfile(jobSeekerProfile);
		}
		String imageName = "";
		String resumeName = "";
		if(!Objects.equals(image.getOriginalFilename(), "")) {
			imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
			jobSeekerProfile.setProfilePhoto(imageName);
		}
		if(!Objects.equals(pdf.getOriginalFilename(), "")) {
			resumeName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
			jobSeekerProfile.setResume(resumeName);
		}
		JobSeekerProfile savedSeeker = jobSeekerProfileService.addNew(jobSeekerProfile);
		
		try {
			String uploadDir = "photos/candidate/" + jobSeekerProfile.getUserAccountId();
			if(!Objects.equals(image.getOriginalFilename(), "")) {
				FileUploadUtil.saveFile(uploadDir, imageName, image);
			}
			if(!Objects.equals(pdf.getOriginalFilename(), "")) {
				FileUploadUtil.saveFile(uploadDir, resumeName, pdf);
			}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		
		return "redirect:/dashboard/";
	}
}
