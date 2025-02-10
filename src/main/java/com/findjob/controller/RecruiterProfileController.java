package com.findjob.controller;

import java.util.Objects;
import java.util.Optional;

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

import com.findjob.entity.RecruiterProfile;
import com.findjob.entity.Users;
import com.findjob.repository.UsersRepository;
import com.findjob.services.RecruiterProfileService;
import com.findjob.util.FileUploadUtil;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

	private final UsersRepository usersRepository;
	private final RecruiterProfileService recruiterProfileService;
	
	public RecruiterProfileController(UsersRepository usersRepository, RecruiterProfileService recruiterProfileService) {
		this.usersRepository = usersRepository;
		this.recruiterProfileService = recruiterProfileService;
	}
	
	@GetMapping("/")
	public String recruiterProfile(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentLoggedInUser = authentication.getName();
			Users users = usersRepository.findByEmail(currentLoggedInUser).orElseThrow(() -> new UsernameNotFoundException("Could not find user"));
			Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());
			if(recruiterProfile.isPresent()) {
				model.addAttribute("profile", recruiterProfile.get());
			}
		}
		return "recruiter_profile";
	}
	
//	Creates a new recruiter profile (in memory based on form data):
	@PostMapping("/addNew")
	public String addNew(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentLoggedInUser = authentication.getName();
			Users users = usersRepository.findByEmail(currentLoggedInUser).orElseThrow(() -> new UsernameNotFoundException("Could not find user"));
			recruiterProfile.setUserId(users);
			recruiterProfile.setUserAccountId(users.getUserId());
		}
		model.addAttribute("profile", recruiterProfile);
		String fileName = "";
		if(!multipartFile.getOriginalFilename().equals("")) {
			fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
			recruiterProfile.setProfilePhoto(fileName);
		}
		RecruiterProfile savedRecruiter = recruiterProfileService.addNew(recruiterProfile);
		String uploadDir = "photos/recruiter/"+savedRecruiter.getUserAccountId();
		try {
//			Read profile image from request - multipartfile
//			Save image on the server in directory: photos/recruiter
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/dashboard/";
	}
}
