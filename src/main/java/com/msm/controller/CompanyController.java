package com.msm.controller;

import com.msm.dto.CompanyDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nabeel Bhatti
 */

@RestController
@RequestMapping("/api/company")
public class CompanyController {



    @GetMapping("/{companyId}")
    public CompanyDTO getCompany( @PathVariable(required = true) Integer companyId){
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(companyId);
//        System.out.println(companyService.getCompanyById(companyId));
        return  companyDTO;
    }
}
