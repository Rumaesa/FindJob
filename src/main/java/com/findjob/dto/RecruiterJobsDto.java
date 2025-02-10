package com.findjob.dto;

import com.findjob.entity.JobCompany;
import com.findjob.entity.JobLocation;

public class RecruiterJobsDto {

	private Long totalCandidates;
	private Integer jobPostId;
	private String jobTitle;
	private JobLocation jobLocationId;
	private JobCompany jobCompanyId;
	public RecruiterJobsDto(Long totalCandidates, Integer jobPostId, String jobTitle, JobLocation jobLocationId,
			JobCompany jobCompany) {
		super();
		this.totalCandidates = totalCandidates;
		this.jobPostId = jobPostId;
		this.jobTitle = jobTitle;
		this.jobLocationId = jobLocationId;
		this.jobCompanyId = jobCompany;
	}
	public Long getTotalCandidates() {
		return totalCandidates;
	}
	public void setTotalCandidates(Long totalCandidates) {
		this.totalCandidates = totalCandidates;
	}
	public Integer getJobPostId() {
		return jobPostId;
	}
	public void setJobPostId(Integer jobPostId) {
		this.jobPostId = jobPostId;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public JobLocation getJobLocationId() {
		return jobLocationId;
	}
	public void setJobLocationId(JobLocation jobLocationId) {
		this.jobLocationId = jobLocationId;
	}
	public JobCompany getJobCompanyId() {
		return jobCompanyId;
	}
	public void setJobCompanyId(JobCompany jobCompany) {
		this.jobCompanyId = jobCompany;
	}
	
	
}
