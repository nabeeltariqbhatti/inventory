package com.msm.service;

import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.CompositionDto;


/**
 * @author Nabeel Bhatti
 */



public interface ServiceComposition {

	CustomResponse getCompositionById(Integer compositionId);

	CustomResponse saveUpdate(CompositionDto compositionDto);

	CustomResponse deleteCompositionById(Integer compositionId);

	CustomResponse getForDwopDown();


	CustomResponse searchCompositionWithPaging(String searchKeyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOn, Integer totalCount, String fromDate, String toDate);
}
