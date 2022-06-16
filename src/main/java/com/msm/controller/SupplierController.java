package com.msm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.DtoSearch;
import com.msm.dto.SupplierDto;
import com.msm.service.ServiceSupplier;

/**
 * 
 * @author IbrarHussain
 *
 */

@RestController
@RequestMapping("api/supplier")
public class SupplierController {

	private static final Logger log = LoggerFactory.getLogger(SupplierController.class);

	private final ServiceSupplier serviceSupplier;

	@Autowired
	public SupplierController(ServiceSupplier serviceSupplier) {
		this.serviceSupplier = serviceSupplier;
	}

	@PostMapping
	public CustomResponse saveSupplier(@RequestBody SupplierDto supplierDto) throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Calling Supplier saving",
				new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(supplierDto));
		if (StringUtil.isNotNull(supplierDto)) {
			return serviceSupplier.saveUpdate(supplierDto);
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(supplierDto)
					.requestTime(endTime - startTime).build();
		}
	}

	@PutMapping
	public CustomResponse updateSupplier(@RequestBody SupplierDto supplierDto) throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Calling Supplier saving",
				new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(supplierDto));
		if (StringUtil.isNotNull(supplierDto)) {
			return serviceSupplier.saveUpdate(supplierDto);
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(supplierDto)
					.requestTime(endTime - startTime).build();
		}
	}
	@GetMapping("/{supplierId}")
	public CustomResponse getSupplierById(@PathVariable(required = true) Integer supplierId) throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("get Supplier By id request " + supplierId);
		if (StringUtil.isNotNull(supplierId)) {

			return serviceSupplier.getSupplierById(supplierId);
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(supplierId)
					.requestTime(endTime - startTime).build();
		}
	}
	
	@DeleteMapping("/{supplierId}")
	public CustomResponse deleteSupplierById(@PathVariable(required = true) Integer supplierId) throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Supplier delete  By id request " + supplierId);
		if (StringUtil.isNotNull(supplierId)) {

			return serviceSupplier.deleteSupplierById(supplierId);
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(supplierId)
					.requestTime(endTime - startTime).build();
		}
	}
	
	@GetMapping("")
	public CustomResponse getForDwopDown() throws JsonProcessingException {

		log.info("Supplier get for drop down ");

		return serviceSupplier.getForDwopDown();
	}
	
	@GetMapping("/searchCompanyWithPaging")
	public CustomResponse searchSupplierWithPaging(@RequestBody DtoSearch dtoSearch) throws JsonProcessingException {
		log.info("search Supplier With Paging .. "+new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dtoSearch));
		return serviceSupplier.searchSupplierWithPaging(dtoSearch);
	}
}
