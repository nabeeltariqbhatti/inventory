package com.msm.service;




import com.fasterxml.jackson.core.JsonProcessingException;
import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.DistributionDto;
import com.msm.dto.DtoSearch;


/**
 * 
 * @author IbrarHussain
 *
 */
public interface ServiceDistribution {

	CustomResponse saveDistribution(DistributionDto distributionDto)throws JsonProcessingException;

	CustomResponse getDistributionById(Integer distributionId)throws JsonProcessingException;

	CustomResponse deleteDistributionById(Integer distributionId)throws JsonProcessingException;

	CustomResponse getForDwopDown()throws JsonProcessingException;

	CustomResponse searchDistributionWithPaging(DtoSearch dtoSearch)throws JsonProcessingException;

}
