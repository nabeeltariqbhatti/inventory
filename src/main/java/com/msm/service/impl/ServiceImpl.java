package com.msm.service.impl;

import com.msm.dto.CompanyDto;
import com.msm.entity.Company;
import com.msm.mapper.CompanyMapper;
import com.msm.repo.CompanyRepo;
import com.msm.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Nabeel Bhatti
 */

@Service
public class ServiceImpl implements CompanyService {

    private final CompanyRepo companyRepo;

    private CompanyMapper companyMappe= new CompanyMapper();

    @Autowired
    public ServiceImpl(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;

    }


    @Override
    public CompanyDto getCompanyById(Integer companyId) {
        CompanyDto companyDTO = null;
        try{
            Company company = companyRepo.getById(companyId);
            if(company != null){

                companyDTO = companyMappe.companyToCompanyDtoMapper(company);
            }

        }catch (NullPointerException nullPointerException){
            System.out.println(nullPointerException);
        }

        return  companyDTO;

    }
}
