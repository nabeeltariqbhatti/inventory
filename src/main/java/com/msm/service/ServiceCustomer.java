package com.msm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.CompositionDto;
import com.msm.dto.CustomerDto;
import com.msm.dto.DtoSearch;

public interface ServiceCustomer {

    CustomResponse getCustomerById(Integer customerId)throws JsonProcessingException;

    CustomResponse saveUpdate(CustomerDto customerDtoDto)throws JsonProcessingException;

    CustomResponse deleteCustomerById(Integer CustomerId)throws JsonProcessingException;

    CustomResponse getForDwopDown();


    CustomResponse searchCustomerWithPaging(DtoSearch dtoSearch);

}
