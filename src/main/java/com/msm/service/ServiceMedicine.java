package com.msm.service;

import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.MedicineDto;


/**
 * @author Nabeel Bhatti
 */



public interface ServiceMedicine {

	CustomResponse getMedicineById(Integer medicineId);

	CustomResponse saveUpdate(MedicineDto medicineDto);

	CustomResponse deleteMedicineById(Integer medicineId);

	CustomResponse getForDwopDown();


	CustomResponse searchMedicineWithPaging(String searchKeyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOn, Integer totalCount, String fromDate, String toDate);
}
