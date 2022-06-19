package com.msm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.DistributionDto;
import com.msm.dto.DtoSearch;
import com.msm.dto.SupplierDto;
import com.msm.entity.Distribution;
import com.msm.entity.Supplier;
import com.msm.mapper.MapperDistribution;
import com.msm.mapper.MapperSupplier;
import com.msm.repository.RepositoryDistribution;
import com.msm.service.ServiceDistribution;
import com.msm.service.ServiceHome;
import com.msm.util.Pagination;

/**
 * 
 * @author IbrarHussain
 *
 */
@Service
public class ServiceDistributionImpl implements ServiceDistribution {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDistributionImpl.class);

	private final RepositoryDistribution repositoryDistribution;
	private final ServiceHome serviceHome;

	public ServiceDistributionImpl(RepositoryDistribution repositoryDistribution, ServiceHome serviceHome) {
		this.repositoryDistribution = repositoryDistribution;
		this.serviceHome = serviceHome;
	}

	@Override
	public CustomResponse saveDistribution(DistributionDto distributionDto) {

		long startTime = System.currentTimeMillis();
		long endTime = 0L;
		Distribution distribution = null;
		if (StringUtil.isNotNull(distributionDto.getId()) && distributionDto.getId() > 0) {
			distribution = repositoryDistribution.findByIdAndIsDeleted(distributionDto.getId(), false);
			if (StringUtil.isNotNull(distribution)) {
				distribution = MapperDistribution.getDistributionFromDistributionDto(distribution, distributionDto,
						serviceHome.getUserId());
				repositoryDistribution.save(distribution);
				endTime = System.currentTimeMillis();
				LOGGER.info("Distribution Update successful:");
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
						.status(HttpStatus.CREATED).message(AppConstant.DATA_UPDATE_SUCCESSFULL).data(null)
						.requestTime(endTime - startTime).build();
			} else {
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(distributionDto)
						.requestTime(endTime - startTime).build();
			}
		} else {
			distribution = new Distribution();
			distribution = MapperDistribution.getDistributionFromDistributionDto(distribution, distributionDto,
					serviceHome.getUserId());
			repositoryDistribution.save(distribution);
			LOGGER.info("Distribution save successful:");
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
					.status(HttpStatus.CREATED).message(AppConstant.DATA_SAVE_SUCCESSFULL).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse getDistributionById(Integer distributionId) throws JsonProcessingException {
		long startTime = System.currentTimeMillis();
		long endTime = 0L;
		Distribution distribution = repositoryDistribution.findByIdAndIsDeleted(distributionId, false);
		LOGGER.info("Distribution get Model :", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(distribution));
		if (StringUtil.isNotNull(distribution)) {
			DistributionDto distributionDto = MapperDistribution.getDistributionDtoFromDistribution(distribution);
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
					.message(AppConstant.DATA_FOUND).data(distributionDto).requestTime(endTime - startTime).build();
		} else {
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse deleteDistributionById(Integer distributionId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0L;
		Distribution distribution = repositoryDistribution.findByIdAndIsDeleted(distributionId, false);
		
		if (StringUtil.isNotNull(distribution)) {
			distribution.setIsDeleted(true);
			repositoryDistribution.save(distribution);
			LOGGER.info("Distribution get Model For Deleting:");
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
					.message(AppConstant.DATA_DELETED_SUCCESSFULL).data(null).requestTime(endTime - startTime).build();
		} else {
			LOGGER.info("Distribution get Model Data Not Found:");
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse getForDwopDown() {
		long startTime = System.currentTimeMillis();
		long endTime = 0L;
		List<Distribution> distributions = repositoryDistribution.findByIsDeleted(false);
		if (StringUtil.isNotNull(distributions) && !distributions.isEmpty()) {
			LOGGER.info("Distribution get Model Data FOUND:");
			List<DistributionDto> distributionDtos = new ArrayList<DistributionDto>();
			distributions.stream().forEach(obj -> {
				DistributionDto distributionDto = MapperDistribution.getDistributionDtoFromDistribution(obj);
				distributionDtos.add(distributionDto);
			});
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
					.message(AppConstant.DATA_FOUND).data(distributionDtos).requestTime(endTime - startTime).build();
		} else {
			LOGGER.info("Distribution get Model Data NOTFOUND:");
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse searchDistributionWithPaging(DtoSearch dtoSearch) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		if (dtoSearch != null) {
			String condition = "";
			String searchWord = dtoSearch.getSearchKeyword();
			if (dtoSearch.getSortOn() != null || dtoSearch.getSortBy() != null) {
				if (dtoSearch.getSortOn().equals("name") || dtoSearch.getSortOn().equals("contactNumber")
						|| dtoSearch.getSortOn().equals("email") || dtoSearch.getSortOn().equals("address")
						|| dtoSearch.getSortOn().equals("faxNo") || dtoSearch.getSortOn().equals("supplierName")) {
					condition = dtoSearch.getSortOn();
				} else {
					condition = "id";
				}
			} else {
				condition += "id";
				dtoSearch.setSortOn("");
				dtoSearch.setSortBy("");
			}
			List<Distribution> distributions = null;
			dtoSearch.setTotalCount(
					repositoryDistribution.predictiveDistributionSearchTotalCount("%" + searchWord + "%"));

			if (dtoSearch.getSortBy().equals("") && dtoSearch.getSortOn().equals("")) {
				distributions = this.repositoryDistribution.predictiveDistributionSearchWithPagination(
						"%" + searchWord + "%", new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
								Sort.by(Sort.Direction.ASC, condition)));
			}
			if (dtoSearch.getSortBy().equals("ASC")) {
				distributions = this.repositoryDistribution.predictiveDistributionSearchWithPagination(
						"%" + searchWord + "%", new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
								Sort.by(Sort.Direction.ASC, condition)));
			} else if (dtoSearch.getSortBy().equals("DESC")) {
				distributions = this.repositoryDistribution.predictiveDistributionSearchWithPagination(
						"%" + searchWord + "%", new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
								Sort.by(Sort.Direction.DESC, condition)));
			}

			if (StringUtil.isNotNull(distributions) && !distributions.isEmpty()) {
				LOGGER.info("Distribution get Model Data FOUND:");
				List<DistributionDto> distributionDtos = new ArrayList<DistributionDto>();
				distributions.stream().forEach(obj -> {
					DistributionDto distributionDto = MapperDistribution.getDistributionDtoFromDistribution(obj);
					distributionDtos.add(distributionDto);
				});
				dtoSearch.setRecords(distributionDtos);
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value())
						.status(HttpStatus.FOUND).message(AppConstant.DATA_FOUND).data(dtoSearch)
						.requestTime(endTime - startTime).build();
			} else {
				LOGGER.info("Distribution get Model Data Not FOUND:");
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_FOUND).data(null)
						.requestTime(endTime - startTime).build();
			}
		}

		return null;
	}
}
