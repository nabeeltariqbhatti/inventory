package com.msm.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.MedicineDto;
import com.msm.entity.Medicine;
import com.msm.mapper.MapperMedicine;
import com.msm.repository.RepositoryMedicine;
import com.msm.service.ServiceHome;
import com.msm.service.ServiceMedicine;
import com.msm.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ServiceMedicineImpl implements ServiceMedicine {

	private final RepositoryMedicine repositoryMedicine;
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMedicineImpl.class);

	private final ServiceHome serviceHome;

	@Autowired private ObjectMapper objectMapper;

	@Autowired
	public ServiceMedicineImpl(RepositoryMedicine repositoryMedicine, ServiceHome serviceHome) {
		this.repositoryMedicine = repositoryMedicine;
		this.serviceHome = serviceHome;

	}

	@Override
	public CustomResponse saveUpdate(MedicineDto medicineDto) {
		long startTime = System.currentTimeMillis();
		long endTime;
		Medicine medicine;

		if (StringUtil.isNotNull(medicineDto.getId()) && medicineDto.getId() > 0) {

			medicine = repositoryMedicine.findByIdAndIsDeleted(medicineDto.getId(), false);

			if (StringUtil.isNotNull(medicine)) {

				LOGGER.info("medicine object found with id  {} " , medicineDto.getIsDeleted());
				try{
					LOGGER.info("medicine object is {}",objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(medicineDto) );
					medicine = MapperMedicine.medicineDtoToMedicineMapper(medicineDto,serviceHome.getUserId());
					repositoryMedicine.save(medicine);
				}catch (JsonProcessingException jsonParseException){
					LOGGER.info("exception while parsing or saving  {}", jsonParseException.getMessage());
				}


				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
						.status(HttpStatus.CREATED).message(AppConstant.DATA_UPDATE_SUCCESSFULL).data(null)
						.requestTime(endTime - startTime).build();
			} else {
				LOGGER.info("no medicine object found with id  {} " , medicineDto.getId());
				endTime = System.currentTimeMillis();
				return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(medicineDto)
						.requestTime(endTime - startTime).build();
			}
		} else {
			medicine = MapperMedicine.medicineDtoToMedicineMapper(medicineDto,serviceHome.getUserId());
			try{
				LOGGER.info("going to save medicine object  {}",objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(medicine) );
				repositoryMedicine.save(medicine);
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
	public CustomResponse getMedicineById(Integer medicineId) {

		long startTime = System.currentTimeMillis();
		long endTime;

		Medicine medicine = repositoryMedicine.findByIdAndIsDeleted(medicineId, false);
		if (StringUtil.isNotNull(medicine)) {
			MedicineDto medicineDto = MapperMedicine.medicineToMedicineDtoMapper(medicine,serviceHome.getUserId());
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
					.message(AppConstant.DATA_FOUND).data(medicineDto).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}

	@Override
	public CustomResponse deleteMedicineById(Integer medicineId) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;

		LOGGER.info("request received to delete the medicine with id {}", medicineId);
		Medicine medicine = repositoryMedicine.findByIdAndIsDeleted(medicineId, false);
		if (StringUtil.isNotNull(medicine)) {
			medicine.setIsDeleted(true);
			try{
				LOGGER.info("going to save medicine object  {}",objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(medicine) );
				repositoryMedicine.save(medicine);
			}catch (JsonProcessingException jsonParseException){
				LOGGER.info("exception while parsing  or saving medicine{}", jsonParseException.getMessage());
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
		List<Medicine> medicineList = repositoryMedicine.findByIsDeleted(false);
		List<MedicineDto> medicineDtos = new ArrayList<MedicineDto>();
		if (StringUtil.isNotNull(medicineList) && !medicineList.isEmpty()) {
			medicineList.forEach(obj -> {
				MedicineDto medicineDto = MapperMedicine.medicineToMedicineDtoMapper(obj,serviceHome.getUserId());
				medicineDtos.add(medicineDto);
			});
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
					.message(AppConstant.DATA_FOUND).data(medicineDtos).requestTime(endTime - startTime).build();
		} else {
			endTime = System.currentTimeMillis();
			return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
					.requestTime(endTime - startTime).build();
		}
	}



	@Override
	public CustomResponse searchMedicineWithPaging(String searchKeyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOn, Integer totalCount, String fromDate, String toDate) {

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
			List<Medicine> medicine = null;
			totalCount= repositoryMedicine.predictiveMedicineSearchTotalCount("%" + searchWord + "%");

			if (sortBy.equals("") && sortOn.equals("")) {
				medicine = this.repositoryMedicine.predictiveMedicineSearchWithPagination("%" + searchWord + "%",
						 new Pagination(pageNumber, pageSize,
								Sort.by(Sort.Direction.ASC, condition)));
			}
			if (sortBy.equals("ASC")) {
				medicine = this.repositoryMedicine.predictiveMedicineSearchWithPagination("%" + searchWord + "%",
						 new Pagination(pageNumber, pageSize,
								Sort.by(Sort.Direction.ASC, condition)));
			} else if (sortBy.equals("DESC")) {
				medicine = this.repositoryMedicine.predictiveMedicineSearchWithPagination("%" + searchWord + "%",
						 new Pagination(pageNumber, pageSize,
								Sort.by(Sort.Direction.DESC, condition)));
			}
			if (StringUtil.isNotNull(medicine) && !medicine.isEmpty()) {
				List<MedicineDto> listMedicineDtos = new ArrayList<>();
				medicine.stream().forEach(obj -> {
					MedicineDto medicineDto = MapperMedicine.medicineToMedicineDtoMapper(obj,serviceHome.getUserId());
					listMedicineDtos.add(medicineDto);
				});
				Map<Object,Object> data = new HashMap<>();
				data.put("search-keyword",searchKeyword );
				data.put("total-count", totalCount);
				data.put("page-number", pageNumber);
				data.put("page-size",pageSize);
				data.put("records",listMedicineDtos);
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
