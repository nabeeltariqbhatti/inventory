package com.msm.service.impl;



import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.SalesDto;
import com.msm.entity.Sales;
import com.msm.mapper.MapperSales;
import com.msm.repository.RepositorySales;
import com.msm.service.ServiceSales;
import com.msm.service.ServiceHome;
import com.msm.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nabeel Bhatti
 */

@Service
public class ServiceSalesImpl implements ServiceSales {

	private final RepositorySales repositorySales;
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSalesImpl.class);

	private final ServiceHome serviceHome;

	@Autowired private ObjectMapper objectMapper;

	@Autowired
	public ServiceSalesImpl(RepositorySales repositorySales, ServiceHome serviceHome) {
		this.repositorySales = repositorySales;
		this.serviceHome = serviceHome;

	}

	@Override
	public CustomResponse saveUpdate(SalesDto saleDto) {
		long startTime = System.currentTimeMillis();
		long endTime;
		Sales sale;

		if (StringUtil.isNotNull(saleDto.getId()) && saleDto.getId() > 0) {

			sale = repositorySales.findByIdAndIsDeleted(saleDto.getId(), false);

			if (StringUtil.isNotNull(sale)) {

				LOGGER.info("sales object found with id  {} " , saleDto.getIsDeleted());
				try{
					LOGGER.info("sales object is {}",objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(saleDto) );
					sale = MapperSales.salesDtoToSalesMapper(saleDto,serviceHome.getUserId());
					repositorySales.save(sale);
				}catch (JsonProcessingException jsonParseException){
					LOGGER.info("exception while parsing or saving  {}", jsonParseException.getMessage());
				}


				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
						.status(HttpStatus.CREATED).message(AppConstant.DATA_UPDATE_SUCCESSFULL).data(null)
						.requestTime(endTime - startTime).build();
			} else {
				LOGGER.info("no sales object found with id  {} " , saleDto.getId());
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(saleDto)
						.requestTime(endTime - startTime).build();
			}
		} else {
			sale = MapperSales.salesDtoToSalesMapper(saleDto,serviceHome.getUserId());
			try{
				LOGGER.info("going to save sales object  {}",objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sale) );
				repositorySales.save(sale);
			}catch (JsonProcessingException jsonParseException){
				LOGGER.info("exception while parsing  " +jsonParseException.getMessage());
			}

			endTime = System.currentTimeMillis();
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
					.status(HttpStatus.CREATED).message(AppConstant.DATA_SAVE_SUCCESSFULL).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse getSalesById(Integer saleId) {

		long startTime = System.currentTimeMillis();
		long endTime;

		Sales sale = repositorySales.findByIdAndIsDeleted(saleId, false);
		if (StringUtil.isNotNull(sale)) {
			SalesDto saleDto = MapperSales.salesToSalesDtoMapper(sale,serviceHome.getUserId());
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
					.message(AppConstant.DATA_FOUND).data(saleDto).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse deleteSalesById(Integer saleId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;

		LOGGER.info("request received to delete the sales with id {}", saleId);
		Sales sale = repositorySales.findByIdAndIsDeleted(saleId, false);
		if (StringUtil.isNotNull(sale)) {
			sale.setIsDeleted(true);
			try{
				LOGGER.info("going to save sales object  {}",objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sale) );
				repositorySales.save(sale);
			}catch (JsonProcessingException jsonParseException){
				LOGGER.info("exception while parsing  or saving sales{}", jsonParseException.getMessage());
			}
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
		LOGGER.info("inside of the {}", "getForDwopDown()" );

		long startTime = System.currentTimeMillis();
		long endTime = 0;
		List<Sales> saleList = repositorySales.findByIsDeleted(false);
		List<SalesDto> saleDtos = new ArrayList<SalesDto>();
		if (StringUtil.isNotNull(saleList) && !saleList.isEmpty()) {
			saleList.forEach(obj -> {
				SalesDto saleDto = MapperSales.salesToSalesDtoMapper(obj,serviceHome.getUserId());
				saleDtos.add(saleDto);
			});
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
					.message(AppConstant.DATA_FOUND).data(saleDtos).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}



	@Override
	public CustomResponse searchSalesWithPaging(String searchKeyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOn, Integer totalCount, String fromDate, String toDate) {

		LOGGER.info("request received for the pagination  with the following parameters   " + searchKeyword);
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
			List<Sales> sales = null;
			totalCount= repositorySales.predictiveSalesSearchTotalCount("%" + searchWord + "%");

			if (sortBy.equals("") && sortOn.equals("")) {
				sales = this.repositorySales.predictiveSalesSearchWithPagination("%" + searchWord + "%",
						new Pagination(pageNumber, pageSize,
								Sort.by(Sort.Direction.ASC, condition)));
			}
			if (sortBy.equals("ASC")) {
				sales = this.repositorySales.predictiveSalesSearchWithPagination("%" + searchWord + "%",
						 new Pagination(pageNumber, pageSize,
								Sort.by(Sort.Direction.ASC, condition)));
			} else if (sortBy.equals("DESC")) {
				sales = this.repositorySales.predictiveSalesSearchWithPagination("%" + searchWord + "%",
						 new Pagination(pageNumber, pageSize,
								Sort.by(Sort.Direction.DESC, condition)));
			}
			if (StringUtil.isNotNull(sales) && !sales.isEmpty()) {
				List<SalesDto> listSalesDtos = new ArrayList<>();
				sales.stream().forEach(obj -> {
					SalesDto saleDto = MapperSales.salesToSalesDtoMapper(obj,serviceHome.getUserId());
					listSalesDtos.add(saleDto);
				});
				Map<Object,Object> data = new HashMap<>();
				data.put("search-keyword",searchKeyword );
				data.put("total-count", totalCount);
				data.put("page-number", pageNumber);
				data.put("page-size",pageSize);
				data.put("records",listSalesDtos);
				data.put("sort-on",sortOn);
				data.put("sort-by",sortBy);
				data.put("from-date",fromDate);
				data.put("to-date",toDate);

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
