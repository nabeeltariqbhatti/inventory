package com.msm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.SalesDto;
import com.msm.dto.DtoSearch;
import com.msm.service.ServiceSales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * @author Nabeel Bhatti
 */

@RestController
@RequestMapping("/api/sales")
public class SalesController {

	private static final Logger log = LoggerFactory.getLogger(SalesController.class);
	private final ServiceSales serviceSales;

	@Autowired private ObjectMapper objectMapper;
	@Autowired
	public SalesController(ServiceSales serviceSales) {
		this.serviceSales = serviceSales;
	}

	@PostMapping
	public CustomResponse saveSales(@RequestBody SalesDto salesDto) throws JsonProcessingException {

		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Sales request create " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(salesDto));
		if (StringUtil.isNotNull(salesDto)) {
			return serviceSales.saveUpdate(salesDto);
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(salesDto).requestTime(endTime - startTime).build();
		}
	}

	@PutMapping
	public CustomResponse updateSales(@RequestBody SalesDto salesDto) throws JsonProcessingException {

		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Sales update request " + new ObjectMapper().writeValueAsString(salesDto));
		if (StringUtil.isNotNull(salesDto)) {

			return serviceSales.saveUpdate(salesDto);
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(salesDto)
					.requestTime(endTime - startTime).build();
		}
	}

	@GetMapping("/{salesId}")
	public CustomResponse getSalesById(@PathVariable(required = true) Integer salesId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Sales get  By id request " + salesId);
		if (StringUtil.isNotNull(salesId)) {

			return serviceSales.getSalesById(salesId);
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(salesId)
					.requestTime(endTime - startTime).build();
		}
	}
	
	@DeleteMapping("/{salesId}")
	public CustomResponse deleteSalesById(@PathVariable(required = true) Integer salesId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Sales delete  By id request " + salesId);
		if (StringUtil.isNotNull(salesId)) {

			return serviceSales.deleteSalesById(salesId);
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(salesId)
					.requestTime(endTime - startTime).build();
		}
	}
	
	@GetMapping("")
	public CustomResponse getForDwopDown() {

		log.info("Sales get for drop down ");

		return serviceSales.getForDwopDown();
	}

	@GetMapping("/searchCompositionWithPaging")
	public CustomResponse searchCompositionWithPaging(@RequestParam(required = false) String searchKeyword,
													  @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
													  @RequestParam(required = false, defaultValue = "10") Integer pageSize,
													  @RequestParam(required = false) Integer totalCount, @RequestParam(required = false) String sortOn,
													  @RequestParam(required = false) String sortBy, @RequestParam(required = false) String fromDate,
													  @RequestParam(required = false) String toDate) throws JsonProcessingException {

		log.info("search Composition With Paging .. {}  {} {} {} {} {} {} {}", searchKeyword, pageNumber, pageSize,
				sortBy, sortOn, totalCount, fromDate, toDate);
		return serviceSales.searchSalesWithPaging(searchKeyword, pageNumber, pageSize, sortBy, sortOn,
				totalCount, fromDate, toDate);
	}
}
