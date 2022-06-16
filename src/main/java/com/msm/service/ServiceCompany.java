package com.msm.service;

import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.CompanyDto;
import com.msm.dto.DtoSearch;


/**
 * @author Nabeel Bhatti
 */



public interface ServiceCompany {

	CustomResponse getCompanyById(Integer companyId);

	CustomResponse saveUpdate(CompanyDto companyDto);

	CustomResponse deleteCompanyById(Integer companyId);

	CustomResponse getForDwopDown();

	CustomResponse searchCompanyWithPaging(DtoSearch dtoSearch);
}
