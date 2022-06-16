package com.msm.service;

import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.CompositionDto;
import com.msm.dto.DtoSearch;


/**
 * @author Nabeel Bhatti
 */



public interface ServiceComposition {

	CustomResponse getCompositionById(Integer companyId);

	CustomResponse saveUpdate(CompositionDto companyDto);

	CustomResponse deleteCompositionById(Integer companyId);

	CustomResponse getForDwopDown();


	CustomResponse searchCompositionWithPaging(String searchKeyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOn, Integer totalCount, String fromDate, String toDate);
}
