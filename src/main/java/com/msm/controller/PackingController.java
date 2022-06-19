package com.msm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.DtoSearch;
import com.msm.dto.PackingDto;
import com.msm.service.ServicePacking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/packing")
public class PackingController {

    private static final Logger log = LoggerFactory.getLogger(SupplierController.class);

    private final ServicePacking servicePacking;

    @Autowired
    public PackingController(ServicePacking servicePacking) {
        this.servicePacking = servicePacking;
    }

    public CustomResponse savePacking(@RequestBody PackingDto packingDto) throws JsonProcessingException {

        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("Calling Packing saving",
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(packingDto));
        if (StringUtil.isNotNull(packingDto)) {
            return servicePacking.saveUpdate(packingDto);
        } else {
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(packingDto)
                    .requestTime(endTime - startTime).build();
        }
    }

    @PutMapping
    public CustomResponse updatePacking(@RequestBody PackingDto packingDto) throws JsonProcessingException {

        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("Calling Packing saving",
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(packingDto));
        if (StringUtil.isNotNull(packingDto)) {
            return servicePacking.saveUpdate(packingDto);
        } else {
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(packingDto)
                    .requestTime(endTime - startTime).build();
        }
    }

    @GetMapping("/{packingId}")
    public CustomResponse getPackingById(@PathVariable(required = true) Integer packingId) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("get Supplier By id request " + packingId);
        if (StringUtil.isNotNull(packingId)) {
            return servicePacking.getPackingById(packingId);
        } else {
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(packingId)
                    .requestTime(endTime - startTime).build();
        }
    }

    @DeleteMapping("/{packingId}")
    public CustomResponse deletedPackingById(@PathVariable(required = true) Integer packingId) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("Supplier delete  By id request " + packingId);
        if (StringUtil.isNotNull(packingId)) {

            return servicePacking.deletePackingById(packingId);
        } else {
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(packingId)
                    .requestTime(endTime - startTime).build();
        }
    }

    @GetMapping("")
    public CustomResponse getForDwopDown() throws JsonProcessingException {

        log.info("Supplier get for drop down ");

        return servicePacking.getForDwopDown();
    }

    @GetMapping("/searchCompanyWithPaging")
    public CustomResponse searchSupplierWithPaging(@RequestBody DtoSearch dtoSearch) throws JsonProcessingException {
        log.info("search Supplier With Paging .. " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dtoSearch));
        return servicePacking.searchPackingWithPaging(dtoSearch);

    }
}