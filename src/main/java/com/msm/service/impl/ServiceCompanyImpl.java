package com.msm.service.impl;

import com.msm.dto.CompanyDto;
import com.msm.entity.Company;
import com.msm.mapper.MapperCompany;
import com.msm.repo.RepositoryCompany;
import com.msm.service.ServiceCompany;
import com.msm.service.ServiceHome;
import com.msms.common.library.constant.AppConstant;
import com.msms.common.library.dto.CustomResponse;
import com.msms.common.library.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author Nabeel Bhatti
 */

@Service
public class ServiceCompanyImpl implements ServiceCompany {

	private final RepositoryCompany repositoryCompany;

	private final ServiceHome serviceHome;

	@Autowired
	public ServiceCompanyImpl(RepositoryCompany repositoryCompany, ServiceHome serviceHome) {
		this.repositoryCompany = repositoryCompany;
		this.serviceHome = serviceHome;

	}

	@Override
	public CustomResponse saveUpdate(CompanyDto companyDto) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		Company company = null;

		if (StringUtil.isNotNull(companyDto.getId()) && companyDto.getId() > 0) {

			company = repositoryCompany.findByIdAndIsDeleted(companyDto.getId(), false);

			if (StringUtil.isNotNull(company)) {

				company = MapperCompany.companyDtoToCompanyMapper(companyDto, company, serviceHome.getUserId());
				repositoryCompany.save(company);
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
						.status(HttpStatus.CREATED).message(AppConstant.DATA_UPDATE_SUCCESSFULL).data(null).requestTime(endTime - startTime).build();
			} else {
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(companyDto).requestTime(endTime - startTime).build();
			}
		} else {

			company = new Company();
			company = MapperCompany.companyDtoToCompanyMapper(companyDto, company, serviceHome.getUserId());
			repositoryCompany.save(company);
			endTime = System.currentTimeMillis();
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
					.status(HttpStatus.CREATED).message(AppConstant.DATA_SAVE_SUCCESSFULL).data(null).requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse getCompanyById(Integer companyId) {
		
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		
		Company company = repositoryCompany.findByIdAndIsDeleted(companyId, false);
		if (StringUtil.isNotNull(company)) {
			CompanyDto companyDTO = MapperCompany.companyToCompanyDtoMapper(company);
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value())
					.status(HttpStatus.FOUND).message(AppConstant.DATA_FOUND).data(companyDTO).requestTime(endTime - startTime).build();
		}else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null).requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse deleteCompanyById(Integer companyId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		Company company = repositoryCompany.findByIdAndIsDeleted(companyId, false);
		if (StringUtil.isNotNull(company)) {
			company.setIsDeleted(true);
			repositoryCompany.save(company);
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value())
					.status(HttpStatus.OK).message(AppConstant.DATA_DELETED_SUCCESSFULL).data(null).requestTime(endTime - startTime).build();
		}else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.status(HttpStatus.INTERNAL_SERVER_ERROR).message(AppConstant.INTERNAL_SERVER_ERROR).data(null).requestTime(endTime - startTime).build();
		}
	}

}
