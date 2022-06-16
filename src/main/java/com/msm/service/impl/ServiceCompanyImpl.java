package com.msm.service.impl;

import com.msm.dto.CompanyDto;
import com.msm.dto.DtoSearch;
import com.msm.entity.Company;
import com.msm.mapper.MapperCompany;
import com.msm.repository.RepositoryCompany;
import com.msm.service.ServiceCompany;
import com.msm.service.ServiceHome;
import com.msm.util.Pagination;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author NabeelBhatti
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
						.status(HttpStatus.CREATED).message(AppConstant.DATA_UPDATE_SUCCESSFULL).data(null)
						.requestTime(endTime - startTime).build();
			} else {
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(companyDto)
						.requestTime(endTime - startTime).build();
			}
		} else {

			company = new Company();
			company = MapperCompany.companyDtoToCompanyMapper(companyDto, company, serviceHome.getUserId());
			repositoryCompany.save(company);
			endTime = System.currentTimeMillis();
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
					.status(HttpStatus.CREATED).message(AppConstant.DATA_SAVE_SUCCESSFULL).data(null)
					.requestTime(endTime - startTime).build();
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
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
					.message(AppConstant.DATA_FOUND).data(companyDTO).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
					.requestTime(endTime - startTime).build();
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
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
					.message(AppConstant.DATA_DELETED_SUCCESSFULL).data(null).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.status(HttpStatus.INTERNAL_SERVER_ERROR).message(AppConstant.INTERNAL_SERVER_ERROR).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse getForDwopDown() {

		long startTime = System.currentTimeMillis();
		long endTime = 0;
		List<Company> companyList = repositoryCompany.findByIsDeleted(false);
		List<CompanyDto> companyDtos = new ArrayList<CompanyDto>();
		if (StringUtil.isNotNull(companyList) && !companyList.isEmpty()) {
			companyList.forEach(obj -> {
				CompanyDto companyDTO = MapperCompany.companyToCompanyDtoMapper(obj);
				companyDtos.add(companyDTO);
			});
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
					.message(AppConstant.DATA_FOUND).data(companyDtos).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse searchCompanyWithPaging(DtoSearch dtoSearch) {

		long startTime = System.currentTimeMillis();
		long endTime = 0;
		if (dtoSearch != null) {
			String condition = "";
			String searchWord = dtoSearch.getSearchKeyword();
			if (dtoSearch.getSortOn() != null || dtoSearch.getSortBy() != null) {
				if (dtoSearch.getSortOn().equals("name")) {
					condition = dtoSearch.getSortOn();
				} else {
					condition = "id";
				}
			} else {
				condition += "id";
				dtoSearch.setSortOn("");
				dtoSearch.setSortBy("");
			}
			List<Company> companies = null;
			dtoSearch.setTotalCount(repositoryCompany.predictiveCompanySearchTotalCount("%" + searchWord + "%"));

			if (dtoSearch.getSortBy().equals("") && dtoSearch.getSortOn().equals("")) {
				companies = this.repositoryCompany.predictiveCompanySearchWithPagination("%" + searchWord + "%",
						new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
								Sort.by(Sort.Direction.ASC, condition)));
			}
			if (dtoSearch.getSortBy().equals("ASC")) {
				companies = this.repositoryCompany.predictiveCompanySearchWithPagination("%" + searchWord + "%",
						new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
								Sort.by(Sort.Direction.ASC, condition)));
			} else if (dtoSearch.getSortBy().equals("DESC")) {
				companies = this.repositoryCompany.predictiveCompanySearchWithPagination("%" + searchWord + "%",
						new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
								Sort.by(Sort.Direction.DESC, condition)));
			}
			if (StringUtil.isNotNull(companies) && !companies.isEmpty()) {
				List<CompanyDto> listCompanyDtos = new ArrayList<>();
				companies.stream().forEach(obj -> {
					CompanyDto companyDTO = MapperCompany.companyToCompanyDtoMapper(obj);
					listCompanyDtos.add(companyDTO);
				});
				dtoSearch.setRecords(listCompanyDtos);
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
						.message(AppConstant.DATA_FOUND).data(dtoSearch).requestTime(endTime - startTime)
						.build();
			} else {
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
						.requestTime(endTime - startTime).build();
			}
		}
		return null;
	}
}
