package com.msm.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.DtoSearch;
import com.msm.dto.PackingDto;
import com.msm.entity.Packing;
import com.msm.mapper.PackingMapper;
import com.msm.repository.RepositoryPacking;
import com.msm.service.ServiceHome;
import com.msm.service.ServicePacking;
import com.msm.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicePackingImpl implements ServicePacking {

    private static final Logger log = LoggerFactory.getLogger(ServicePackingImpl.class);

    private final RepositoryPacking repositoryPacking;

    private final ServiceHome serviceHome;

    @Autowired
    public ServicePackingImpl(RepositoryPacking repositoryPacking, ServiceHome serviceHome) {
        this.repositoryPacking = repositoryPacking;
        this.serviceHome = serviceHome;
    }

    @Override
    public CustomResponse saveUpdate(PackingDto packingDto) throws JsonProcessingException {

        long startTime = System.currentTimeMillis();
        long endTime = 0L;
        Packing packing = null;
        if (StringUtil.isNotNull(packingDto.getId()) && packingDto.getId() > 0) {
            packing = repositoryPacking.findByIdAndIsDeleted(packingDto.getId(), false);
            if (StringUtil.isNotNull(packing)) {
                packing = PackingMapper.getPackingFromPackingDto(packing, packingDto, serviceHome.getUserId());
/*                 // have to do this it is to late have to rest
                packing.setMedicineDetail( //todp=o
                        repositoryMedicineDetail.findByIdAndIsDeleted(packingDto.setMedicineDetail().getId(), false));
                */log.info("Packing Update date model:",
                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(packing));
                repositoryPacking.save(packing);
                endTime = System.currentTimeMillis();
                log.info("Packing Update successful:");
                return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED).message(AppConstant.DATA_UPDATE_SUCCESSFULL).data(null)
                        .requestTime(endTime - startTime).build();
            } else {
                endTime = System.currentTimeMillis();
                return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(packingDto)
                        .requestTime(endTime - startTime).build();
            }
        } else {

            packing = new Packing();
            packing = PackingMapper.getPackingFromPackingDto(packing, packingDto, serviceHome.getUserId());
            // to do
          /*  packing.setMedicineDetail(
                    repositoryMedicineDetail.findByIdAndIsDeleted(packingDto.getMedicineDetail.getId(), false));
           */ log.info("Packing save date model:",
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(packing));
            repositoryPacking.save(packing);
            log.info("Packing save successful:");
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
                    .status(HttpStatus.CREATED).message(AppConstant.DATA_SAVE_SUCCESSFULL).data(null)
                    .requestTime(endTime - startTime).build();
        }
    }

    @Override
    public CustomResponse deletePackingById(Integer packingId) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        long endTime = 0L;
        Packing packing = repositoryPacking.findByIdAndIsDeleted(packingId, false);
        log.info("Delete packing Model",
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(packing));
        if (StringUtil.isNotNull(packing)) {
            packing.setIsDeleted(true);
            repositoryPacking.save(packing);
            log.info("packing Deleted  Successful");
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(AppConstant.DATA_DELETED_SUCCESSFULL).data(null).requestTime(endTime - startTime).build();
        } else {
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_FOUND).data(packingId)
                    .requestTime(endTime - startTime).build();
        }
    }

    @Override
    public CustomResponse getPackingById(Integer packingId) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        long endTime = 0L;
        Packing packing = repositoryPacking.findByIdAndIsDeleted(packingId, false);
        log.info("get packing Model",
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(packing));
        PackingDto packingDto = new PackingDto();
        if (StringUtil.isNotNull(packing)) {
            packingDto = PackingMapper.getPackingDtoFromPacking(packing);
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
                    .message(AppConstant.DATA_FOUND).data(packingDto).requestTime(endTime - startTime).build();
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
        List<Packing> packing = repositoryPacking.findByIsDeleted(false);

        if (StringUtil.isNotNull(packing) && !packing.isEmpty()) {
            log.info("get packing For Dwop Down ");
            List<PackingDto> packingDtos = new ArrayList<PackingDto>();
            packing.stream().forEach(obj -> {
                PackingDto packingDto = new PackingDto();
                packingDto = PackingMapper.getPackingDtoFromPacking(obj);
                packingDtos.add(packingDto);
            });
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
                    .message(AppConstant.DATA_FOUND).data(packingDtos).requestTime(endTime - startTime).build();
        } else {
            log.info(" packing DATA NOT FOUND  For Dwop Down ");
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
                    .requestTime(endTime - startTime).build();
        }
    }

    @Override
    public CustomResponse searchPackingWithPaging(DtoSearch dtoSearch) throws JsonProcessingException {

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
            List<Packing> packing = null;
            dtoSearch.setTotalCount(repositoryPacking.predictivePackingSearchTotalCount("%" + searchWord + "%"));

            if (dtoSearch.getSortBy().equals("") && dtoSearch.getSortOn().equals("")) {
                packing = this.repositoryPacking.predictivePackingSearchWithPagination("%" + searchWord + "%",
                        new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
                                Sort.by(Sort.Direction.ASC, condition)));
            }
            if (dtoSearch.getSortBy().equals("ASC")) {
                packing = this.repositoryPacking.predictivePackingSearchWithPagination("%" + searchWord + "%",
                        new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
                                Sort.by(Sort.Direction.ASC, condition)));
            } else if (dtoSearch.getSortBy().equals("DESC")) {
                packing = this.repositoryPacking.predictivePackingSearchWithPagination("%" + searchWord + "%",
                        new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
                                Sort.by(Sort.Direction.DESC, condition)));
            }

            if (StringUtil.isNotNull(packing) && !packing.isEmpty()) {
                log.info("Found search packing Date With Paging ");
                List<PackingDto> packingDtos = new ArrayList<PackingDto>();
                packing.stream().forEach(obj -> {
                    PackingDto packingDto = new PackingDto();
                    packingDto = PackingMapper.getPackingDtoFromPacking(obj);
                    packingDtos.add(packingDto);
                });
                dtoSearch.setRecords(packingDtos);
                endTime = System.currentTimeMillis();
                return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
                        .message(AppConstant.DATA_FOUND).data(dtoSearch).requestTime(endTime - startTime).build();
            } else {
                log.info("Not Found search packing Date With Paging ");
                endTime = System.currentTimeMillis();
                return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
                        .requestTime(endTime - startTime).build();
            }
        }

        return null;
    }
}

