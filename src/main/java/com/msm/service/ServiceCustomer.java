package com.msm.service;

import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.CompositionDto;
import com.msm.dto.CustomerDto;
import com.msm.dto.DtoSearch;

public interface ServiceCustomer {

    CustomResponse getCustomerById(Integer customerId);

    CustomResponse saveUpdate(CustomerDto customerDtoDto);

    CustomResponse deleteCustomerById(Integer CustomerId);

    CustomResponse getForDwopDown();


    CustomResponse searchCustomerWithPaging(DtoSearch dtoSearch);

}
