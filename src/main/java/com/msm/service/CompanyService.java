package com.msm.service;

import com.msm.dto.CompanyDto;

/**
 * @author Nabeel Bhatti
 */



public interface CompanyService {

    CompanyDto getCompanyById(Integer companyId);
}
