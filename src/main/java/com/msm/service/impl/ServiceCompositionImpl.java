package com.msm.service.impl;

import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.CompositionDto;
import com.msm.dto.DtoSearch;
import com.msm.entity.Composition;
import com.msm.mapper.MapperComposition;
import com.msm.repo.RepositoryComposition;
import com.msm.service.ServiceComposition;
import com.msm.service.ServiceHome;
import com.msm.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nabeel Bhatti
 */

@Service
public class ServiceCompositionImpl implements ServiceComposition {

	private final RepositoryComposition repositoryComposition;

	private final ServiceHome serviceHome;

	@Autowired
	public ServiceCompositionImpl(RepositoryComposition repositoryComposition, ServiceHome serviceHome) {
		this.repositoryComposition = repositoryComposition;
		this.serviceHome = serviceHome;

	}

	@Override
	public CustomResponse saveUpdate(CompositionDto compositionDto) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		Composition composition = null;

		if (StringUtil.isNotNull(compositionDto.getId()) && compositionDto.getId() > 0) {

			composition = repositoryComposition.findByIdAndIsDeleted(compositionDto.getId(), false);

			if (StringUtil.isNotNull(composition)) {

				composition = MapperComposition.compositionDtoToCompositionMapper(compositionDto,serviceHome.getUserId());
				repositoryComposition.save(composition);
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
						.status(HttpStatus.CREATED).message(AppConstant.DATA_UPDATE_SUCCESSFULL).data(null)
						.requestTime(endTime - startTime).build();
			} else {
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(compositionDto)
						.requestTime(endTime - startTime).build();
			}
		} else {

			composition = new Composition();
			composition = MapperComposition.compositionDtoToCompositionMapper(compositionDto,serviceHome.getUserId());
			repositoryComposition.save(composition);
			endTime = System.currentTimeMillis();
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
					.status(HttpStatus.CREATED).message(AppConstant.DATA_SAVE_SUCCESSFULL).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse getCompositionById(Integer compositionId) {

		long startTime = System.currentTimeMillis();
		long endTime = 0;

		Composition composition = repositoryComposition.findByIdAndIsDeleted(compositionId, false);
		if (StringUtil.isNotNull(composition)) {
			CompositionDto compositionDto = MapperComposition.compositionToCompositionDtoMapper(composition,serviceHome.getUserId());
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
					.message(AppConstant.DATA_FOUND).data(compositionDto).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse deleteCompositionById(Integer compositionId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		Composition composition = repositoryComposition.findByIdAndIsDeleted(compositionId, false);
		if (StringUtil.isNotNull(composition)) {
			composition.setIsDeleted(true);
			repositoryComposition.save(composition);
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
		List<Composition> compositionList = repositoryComposition.findByIsDeleted(false);
		List<CompositionDto> compositionDtos = new ArrayList<CompositionDto>();
		if (StringUtil.isNotNull(compositionList) && !compositionList.isEmpty()) {
			compositionList.forEach(obj -> {
				CompositionDto compositionDto = MapperComposition.compositionToCompositionDtoMapper(obj,serviceHome.getUserId());
				compositionDtos.add(compositionDto);
			});
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
					.message(AppConstant.DATA_FOUND).data(compositionDtos).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}



	@Override
	public CustomResponse searchCompositionWithPaging(String searchKeyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOn, Integer totalCount, String fromDate, String toDate) {

		long startTime = System.currentTimeMillis();
		long endTime = 0;

			String condition = "";
		    String searchWord = null;
			if(searchKeyword !=null && !searchKeyword.isEmpty()){
					searchWord = searchKeyword;
			}else {
				searchWord = "";
			}

			if (sortOn != null || sortBy != null) {
				if (sortOn.equals("name")) {
					condition = sortOn;
				} else {
					condition = "id";
				}
			} else {
				condition += "id";
				sortOn = "";
				sortBy = "";
			}
			List<Composition> compositions = null;
			totalCount= repositoryComposition.predictiveCompositionSearchTotalCount("%" + searchWord + "%");

			if (sortBy.equals("") && sortOn.equals("")) {
				compositions = this.repositoryComposition.predictiveCompositionSearchWithPagination("%" + searchWord + "%",
						new Pagination(pageNumber, pageSize,
								Sort.by(Sort.Direction.ASC, condition)));
			}
			if (sortBy.equals("ASC")) {
				compositions = this.repositoryComposition.predictiveCompositionSearchWithPagination("%" + searchWord + "%",
						new Pagination(pageNumber, pageSize,
								Sort.by(Sort.Direction.ASC, condition)));
			} else if (sortBy.equals("DESC")) {
				compositions = this.repositoryComposition.predictiveCompositionSearchWithPagination("%" + searchWord + "%",
						new Pagination(pageNumber, pageSize,
								Sort.by(Sort.Direction.DESC, condition)));
			}
			if (StringUtil.isNotNull(compositions) && !compositions.isEmpty()) {
				List<CompositionDto> listCompositionDtos = new ArrayList<>();
				compositions.stream().forEach(obj -> {
					CompositionDto compositionDto = MapperComposition.compositionToCompositionDtoMapper(obj,serviceHome.getUserId());
					listCompositionDtos.add(compositionDto);
				});
				Map<Object,Object> data = new HashMap<>();
				data.put("compositions",listCompositionDtos );
				data.put("total-count", totalCount);
				data.put("page-no", pageNumber);
				data.put("page-size",pageSize);
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
						.message(AppConstant.DATA_FOUND).data(data).requestTime(endTime - startTime)
						.build();
			} else {
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
						.requestTime(endTime - startTime).build();
			}

	}
}
