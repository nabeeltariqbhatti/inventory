package com.msm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.DtoSearch;
import com.msm.dto.PackingDto;

public interface ServicePacking {

    CustomResponse saveUpdate(PackingDto packingDto) throws JsonProcessingException;
    CustomResponse deletePackingById(Integer supplierId)throws JsonProcessingException;
    CustomResponse getPackingById(Integer packingDto) throws  JsonProcessingException;

    CustomResponse getForDwopDown()throws JsonProcessingException;
    CustomResponse searchPackingWithPaging(DtoSearch dtoSearch)throws JsonProcessingException;

}
