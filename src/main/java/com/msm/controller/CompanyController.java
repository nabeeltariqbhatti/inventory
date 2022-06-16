package com.msm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;

import com.msm.dto.CompanyDto;
import com.msm.dto.DtoSearch;
import com.msm.service.ServiceCompany;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Nabeel Bhatti
 */

@RestController
@RequestMapping("/api/company")
public class CompanyController {

	private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
	private final ServiceCompany serviceCompany;

	@Autowired
	public CompanyController(ServiceCompany serviceCompany) {
		this.serviceCompany = serviceCompany;
	}

	@PostMapping
	public CustomResponse saveCompany(@RequestBody CompanyDto companyDto) throws JsonProcessingException {

		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Company request create " + new ObjectMapper().writeValueAsString(companyDto));
		if (StringUtil.isNotNull(companyDto)) {

			return serviceCompany.saveUpdate(companyDto);
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(companyDto).requestTime(endTime - startTime).build();
		}
	}

	@PutMapping
	public CustomResponse updateCompany(@RequestBody CompanyDto companyDto) throws JsonProcessingException {

		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Company update request " + new ObjectMapper().writeValueAsString(companyDto));
		if (StringUtil.isNotNull(companyDto)) {

			return serviceCompany.saveUpdate(companyDto);
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(companyDto)
					.requestTime(endTime - startTime).build();
		}
	}

	@GetMapping("/{companyId}")
	public CustomResponse getCompanyById(@PathVariable(required = true) Integer companyId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Company get  By id request " + companyId);
		if (StringUtil.isNotNull(companyId)) {

			return serviceCompany.getCompanyById(companyId);
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(companyId)
					.requestTime(endTime - startTime).build();
		}
	}
	
	@DeleteMapping("/{companyId}")
	public CustomResponse deleteCompanyById(@PathVariable(required = true) Integer companyId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Company delete  By id request " + companyId);
		if (StringUtil.isNotNull(companyId)) {

			return serviceCompany.deleteCompanyById(companyId);
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(companyId)
					.requestTime(endTime - startTime).build();
		}
	}
	
	@GetMapping("")
	public CustomResponse getForDwopDown() {

		log.info("Company get for drop down ");

		return serviceCompany.getForDwopDown();
	}
	
	@GetMapping("/searchCompanyWithPaging")
	public CustomResponse searchCompanyWithPaging(@RequestBody DtoSearch dtoSearch) throws JsonProcessingException {
		log.info("search Company With Paging .. "+new ObjectMapper().writeValueAsString(dtoSearch));
		return serviceCompany.searchCompanyWithPaging(dtoSearch);
	}
}
