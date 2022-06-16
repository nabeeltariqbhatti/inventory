package com.msm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.DtoSearch;
import com.msm.dto.SupplierDto;
import com.msm.entity.Company;
import com.msm.entity.Supplier;
import com.msm.mapper.MapperSupplier;
import com.msm.repository.ReposiotrySupplier;
import com.msm.repository.RepositoryDistribution;
import com.msm.service.ServiceHome;
import com.msm.service.ServiceSupplier;
import com.msm.util.Pagination;

/**
 * 
 * @author IbrarHussain
 *
 */
@Service
public class ServiceSupplierImpl implements ServiceSupplier {

	private static final Logger log = LoggerFactory.getLogger(ServiceSupplierImpl.class);

	private final ReposiotrySupplier reposiotrySupplier;

	private final ServiceHome serviceHome;

	private final RepositoryDistribution repositoryDistribution;

	@Autowired
	public ServiceSupplierImpl(ReposiotrySupplier reposiotrySupplie, ServiceHome serviceHome,
			RepositoryDistribution repositoryDistribution) {

		this.reposiotrySupplier = reposiotrySupplie;
		this.serviceHome = serviceHome;
		this.repositoryDistribution = repositoryDistribution;
	}

	@Override
	public CustomResponse saveUpdate(SupplierDto supplierDto) throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0L;
		Supplier supplier = null;
		if (StringUtil.isNotNull(supplierDto.getId()) && supplierDto.getId() > 0) {
			supplier = reposiotrySupplier.findByIdAndIsDeleted(supplierDto.getId(), false);
			if (StringUtil.isNotNull(supplier)) {
				supplier = MapperSupplier.getSupplierFromSupplierDto(supplier, supplierDto, serviceHome.getUserId());

				supplier.setDistribution(
						repositoryDistribution.findByIdAndIsDeleted(supplierDto.getDistribution().getId(), false));
				log.info("Supplier Update date model:",
						new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(supplier));
				reposiotrySupplier.save(supplier);
				endTime = System.currentTimeMillis();
				log.info("Supplier Update successful:");
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
						.status(HttpStatus.CREATED).message(AppConstant.DATA_UPDATE_SUCCESSFULL).data(null)
						.requestTime(endTime - startTime).build();
			} else {
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(supplierDto)
						.requestTime(endTime - startTime).build();
			}
		} else {

			supplier = MapperSupplier.getSupplierFromSupplierDto(supplier, supplierDto, serviceHome.getUserId());
			supplier.setDistribution(
					repositoryDistribution.findByIdAndIsDeleted(supplierDto.getDistribution().getId(), false));
			log.info("Supplier save date model:",
					new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(supplier));
			reposiotrySupplier.save(supplier);
			log.info("Supplier save successful:");
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
					.status(HttpStatus.CREATED).message(AppConstant.DATA_SAVE_SUCCESSFULL).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse deleteSupplierById(Integer supplierId) throws JsonProcessingException {

		long startTime = System.currentTimeMillis();
		long endTime = 0L;
		Supplier supplier = reposiotrySupplier.findByIdAndIsDeleted(supplierId, false);
		log.info("Delete supplier Model",
				new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(supplier));
		if (StringUtil.isNotNull(supplier)) {
			supplier.setIsDeleted(true);
			reposiotrySupplier.save(supplier);
			log.info("supplier Deleted  Successful");
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
					.message(AppConstant.DATA_DELETED_SUCCESSFULL).data(null).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_FOUND).data(supplierId)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse getSupplierById(Integer supplierId) throws JsonProcessingException {

		long startTime = System.currentTimeMillis();
		long endTime = 0L;
		Supplier supplier = reposiotrySupplier.findByIdAndIsDeleted(supplierId, false);
		log.info("get supplier Model",
				new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(supplier));
		SupplierDto supplierDto = new SupplierDto();
		if (StringUtil.isNotNull(supplier)) {
			supplierDto = MapperSupplier.getSupplierDtoFromSupplier(supplier);
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
					.message(AppConstant.DATA_FOUND).data(supplierDto).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse getForDwopDown() throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0L;
		List<Supplier> suppliers = reposiotrySupplier.findByIsDeleted(false);
		
		if (StringUtil.isNotNull(suppliers) && !suppliers.isEmpty()) {
			log.info("get supplier For Dwop Down ");
			List<SupplierDto> suppliersDtos = new ArrayList<SupplierDto>();
			suppliers.stream().forEach(obj -> {
				SupplierDto supplierDto = new SupplierDto();
				supplierDto = MapperSupplier.getSupplierDtoFromSupplier(obj);
				suppliersDtos.add(supplierDto);
			});
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
					.message(AppConstant.DATA_FOUND).data(suppliersDtos).requestTime(endTime - startTime).build();
		} else {
			log.info(" supplier DATA NOT FOUND  For Dwop Down ");
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse searchSupplierWithPaging(DtoSearch dtoSearch) throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		if (dtoSearch != null) {
			String condition = "";
			String searchWord = dtoSearch.getSearchKeyword();
			if (dtoSearch.getSortOn() != null || dtoSearch.getSortBy() != null) {
				if (dtoSearch.getSortOn().equals("name") || dtoSearch.getSortOn().equals("contactNumber")
						|| dtoSearch.getSortOn().equals("email") || dtoSearch.getSortOn().equals("address")) {
					condition = dtoSearch.getSortOn();
				} else {
					condition = "id";
				}
			} else {
				condition += "id";
				dtoSearch.setSortOn("");
				dtoSearch.setSortBy("");
			}
			List<Supplier> suppliers = null;
			dtoSearch.setTotalCount(reposiotrySupplier.predictiveSupplieSearchTotalCount("%" + searchWord + "%"));

			if (dtoSearch.getSortBy().equals("") && dtoSearch.getSortOn().equals("")) {
				suppliers = this.reposiotrySupplier.predictiveSupplieSearchWithPagination("%" + searchWord + "%",
						new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
								Sort.by(Sort.Direction.ASC, condition)));
			}
			if (dtoSearch.getSortBy().equals("ASC")) {
				suppliers = this.reposiotrySupplier.predictiveSupplieSearchWithPagination("%" + searchWord + "%",
						new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
								Sort.by(Sort.Direction.ASC, condition)));
			} else if (dtoSearch.getSortBy().equals("DESC")) {
				suppliers = this.reposiotrySupplier.predictiveSupplieSearchWithPagination("%" + searchWord + "%",
						new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
								Sort.by(Sort.Direction.DESC, condition)));
			}
			
			if (StringUtil.isNotNull(suppliers) && !suppliers.isEmpty()) {
				log.info("Found search Supplie Date With Paging ");
				List<SupplierDto> suppliersDtos = new ArrayList<SupplierDto>();
				suppliers.stream().forEach(obj -> {
					SupplierDto supplierDto = new SupplierDto();
					supplierDto = MapperSupplier.getSupplierDtoFromSupplier(obj);
					suppliersDtos.add(supplierDto);
				});
				dtoSearch.setRecords(suppliersDtos);
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
						.message(AppConstant.DATA_FOUND).data(dtoSearch).requestTime(endTime - startTime).build();
			} else {
				log.info("Not Found search Supplie Date With Paging ");
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
						.requestTime(endTime - startTime).build();
			}
		}
		
		return null;
	}

}
