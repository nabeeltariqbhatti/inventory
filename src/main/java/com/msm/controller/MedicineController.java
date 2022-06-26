package com.msm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.MedicineDto;
import com.msm.service.ServiceMedicine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * @author Nabeel Bhatti
 */

@RestController
@RequestMapping("/api/medicine")
public class MedicineController {

	private static final Logger log = LoggerFactory.getLogger(MedicineController.class);
	private final ServiceMedicine serviceMedicine;

	@Autowired private ObjectMapper objectMapper;
	@Autowired
	public MedicineController(ServiceMedicine serviceMedicine) {
		this.serviceMedicine = serviceMedicine;
	}

	@PostMapping
	public CustomResponse saveMedicine(@RequestBody MedicineDto medicineDto) throws JsonProcessingException {

		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Medicine request create " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(medicineDto));
		if (StringUtil.isNotNull(medicineDto)) {
			return serviceMedicine.saveUpdate(medicineDto);
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(medicineDto).requestTime(endTime - startTime).build();
		}
	}

	@PutMapping
	public CustomResponse updateMedicine(@RequestBody MedicineDto medicineDto) throws JsonProcessingException {

		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Medicine update request " + new ObjectMapper().writeValueAsString(medicineDto));
		if (StringUtil.isNotNull(medicineDto)) {

			return serviceMedicine.saveUpdate(medicineDto);
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(medicineDto)
					.requestTime(endTime - startTime).build();
		}
	}

	@GetMapping("/{medicineId}")
	public CustomResponse getMedicineById(@PathVariable(required = true) Integer medicineId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Medicine get  By id request " + medicineId);
		if (StringUtil.isNotNull(medicineId)) {

			return serviceMedicine.getMedicineById(medicineId);
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(medicineId)
					.requestTime(endTime - startTime).build();
		}
	}
	
	@DeleteMapping("/{medicineId}")
	public CustomResponse deleteMedicineById(@PathVariable(required = true) Integer medicineId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		log.info("Medicine delete  By id request " + medicineId);
		if (StringUtil.isNotNull(medicineId)) {

			return serviceMedicine.deleteMedicineById(medicineId);
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(medicineId)
					.requestTime(endTime - startTime).build();
		}
	}
	
	@GetMapping("")
	public CustomResponse getForDwopDown() {

		log.info("Medicine get for drop down ");

		return serviceMedicine.getForDwopDown();
	}

	@GetMapping("/searchMedicineWithPaging")
	public CustomResponse searchMedicineWithPaging(@RequestParam(required = false) String searchKeyword,
													  @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
													  @RequestParam(required = false, defaultValue = "10") Integer pageSize,
													  @RequestParam(required = false) Integer totalCount, @RequestParam(required = false) String sortOn,
													  @RequestParam(required = false) String sortBy, @RequestParam(required = false) String fromDate,
													  @RequestParam(required = false) String toDate) throws JsonProcessingException {

		log.info("search Medicine With Paging .. {}  {} {} {} {} {} {} {}", searchKeyword, pageNumber, pageSize,
				sortBy, sortOn, totalCount, fromDate, toDate);
		return serviceMedicine.searchMedicineWithPaging(searchKeyword, pageNumber, pageSize, sortBy, sortOn,
				totalCount, fromDate, toDate);
	}
}
