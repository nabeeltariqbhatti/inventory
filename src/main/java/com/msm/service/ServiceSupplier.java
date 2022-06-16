package com.msm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.DtoSearch;
import com.msm.dto.SupplierDto;

/**
 * 
 * @author IbrarHussain
 *
 */
public interface ServiceSupplier {

	CustomResponse saveUpdate(SupplierDto supplierDto)throws JsonProcessingException;

	CustomResponse deleteSupplierById(Integer supplierId)throws JsonProcessingException;

	CustomResponse getSupplierById(Integer supplierId)throws JsonProcessingException;

	CustomResponse getForDwopDown()throws JsonProcessingException;

	CustomResponse searchSupplierWithPaging(DtoSearch dtoSearch)throws JsonProcessingException;

}
