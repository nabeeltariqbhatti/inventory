package com.msm.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.DistributionDto;
import com.msm.dto.DtoSearch;
import com.msm.service.ServiceDistribution;

import net.sf.jasperreports.engine.JRException;

/**
 * 
 * @author IbrarHussain
 *
 */
@RestController
@RequestMapping("/api/distirution")
public class DistributionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DistributionController.class);
	private final ServiceDistribution serviceDistribution;

	@Autowired
	public DistributionController(ServiceDistribution serviceDistribution) {
		this.serviceDistribution = serviceDistribution;
	}

	@PostMapping
	public CustomResponse saveDistribution(DistributionDto distributionDto) throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		LOGGER.info("Calling Distribution saving",
				new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(distributionDto));
		if (StringUtil.isNotNull(distributionDto)) {
			return serviceDistribution.saveDistribution(distributionDto);
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(distributionDto)
					.requestTime(endTime - startTime).build();
		}
	}

	@PutMapping
	public CustomResponse updateDistribution(@RequestBody DistributionDto distributionDto)
			throws IOException, JRException {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		LOGGER.info("Calling Distribution Updating",
				new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(distributionDto));
		if (StringUtil.isNotNull(distributionDto)) {
			return serviceDistribution.saveDistribution(distributionDto);
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(distributionDto)
					.requestTime(endTime - startTime).build();
		}

	}

	@GetMapping("/{distributionId}")
	public CustomResponse getDistributionById(@PathVariable(required = true) Integer distributionId)
			throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		LOGGER.info("get Distribution By id request " + distributionId);
		if (StringUtil.isNotNull(distributionId)) {

			return serviceDistribution.getDistributionById(distributionId);
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(distributionId)
					.requestTime(endTime - startTime).build();
		}
	}

	@DeleteMapping("/{distributionId}")
	public CustomResponse deleteDistributionById(@PathVariable(required = true) Integer distributionId)
			throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		LOGGER.info("Distribution delete  By id request " + distributionId);
		if (StringUtil.isNotNull(distributionId)) {

			return serviceDistribution.deleteDistributionById(distributionId);
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(distributionId)
					.requestTime(endTime - startTime).build();
		}
	}

	@GetMapping
	public CustomResponse getForDwopDown() throws JsonProcessingException {

		LOGGER.info("Distribution get for drop down ");

		return serviceDistribution.getForDwopDown();
	}

	@GetMapping("/searchDistributionWithPaging")
	public CustomResponse searchDistributionWithPaging(@RequestBody DtoSearch dtoSearch)
			throws JsonProcessingException {
		LOGGER.info("search Distribution With Paging .. "
				+ new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dtoSearch));
		return serviceDistribution.searchDistributionWithPaging(dtoSearch);
	}

}
