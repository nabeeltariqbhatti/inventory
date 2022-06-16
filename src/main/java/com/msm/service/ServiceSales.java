package com.msm.service;

import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.SalesDto;


/**
 * @author Nabeel Bhatti
 */



public interface ServiceSales {

	CustomResponse getSalesById(Integer salesId);

	CustomResponse saveUpdate(SalesDto salesDto);

	CustomResponse deleteSalesById(Integer salesId);

	CustomResponse getForDwopDown();


	CustomResponse searchSalesWithPaging(String searchKeyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOn, Integer totalCount, String fromDate, String toDate);
}
