package com.msm.service;

import com.msm.dto.CompanyDto;
import com.msms.common.library.dto.CustomResponse;

/**
 * @author Nabeel Bhatti
 */



public interface ServiceCompany {

	CustomResponse getCompanyById(Integer companyId);

	CustomResponse saveUpdate(CompanyDto companyDto);

	CustomResponse deleteCompanyById(Integer companyId);
}
