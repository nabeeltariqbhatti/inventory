package com.msm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.CustomerDto;
import com.msm.dto.DtoSearch;
import com.msm.service.ServiceCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final ServiceCustomer serviceCustomer;

    @Autowired
    public CustomerController(ServiceCustomer serviceCustomer) {
        this.serviceCustomer = serviceCustomer;
    }

    @PostMapping
    public CustomResponse saveCustomer(@RequestBody CustomerDto customerDto) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("Customer request create " + new ObjectMapper().writeValueAsString(customerDto));
        if (StringUtil.isNotNull(customerDto)) {
            return serviceCustomer.saveUpdate(customerDto);
        } else {
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(customerDto).requestTime(endTime - startTime).build();
        }
    }

    @PutMapping
    public CustomResponse updateCustomer(@RequestBody CustomerDto customerDto) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("Calling Customer saving",
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(customerDto));

        if (StringUtil.isNotNull(customerDto)) {
            return serviceCustomer.saveUpdate(customerDto);
        } else {
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(customerDto)
                    .requestTime(endTime - startTime).build();
        }
    }
    @GetMapping("/{customerId}")
    public CustomResponse getCustomerById(@PathVariable(required = true) Integer customerId) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("get Customer By id request " + customerId);
        if(StringUtil.isNotNull(customerId)) {
            return serviceCustomer.getCustomerById(customerId);
        }else {
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(customerId)
                    .requestTime(endTime - startTime).build();
        }
    }
    @DeleteMapping("/{customerId}")
    public CustomResponse deleteCustomerById(@PathVariable(required = true)Integer customerId) throws JsonProcessingException{
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info(("Customer has been deleted by Id request" + customerId));
        if(StringUtil.isNotNull(customerId)) {
            return serviceCustomer.deleteCustomerById(customerId);
        }else {
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message(AppConstant.BAD_REQUEST).data(customerId)
                    .requestTime(endTime - startTime).build();
        }
    }
    @GetMapping("")
    public CustomResponse getForDwopDown() throws JsonProcessingException {

        log.info("Customer get for drop down ");

        return serviceCustomer.getForDwopDown();
    }
    @GetMapping("/searchCustomerWithPaging")
    public CustomResponse searchCustomerWithPaging(@RequestBody DtoSearch dtoSearch) throws JsonProcessingException {
        log.info("search Customer With Paging .. "+new ObjectMapper().writeValueAsString(dtoSearch));
        return serviceCustomer.searchCustomerWithPaging(dtoSearch);
    }
}