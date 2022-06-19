package com.msm.service;




import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.DistributionDto;
import com.msm.dto.DtoSearch;


/**
 * 
 * @author IbrarHussain
 *
 */
public interface ServiceDistribution {

	CustomResponse saveDistribution(DistributionDto distributionDto);

	CustomResponse getDistributionById(Integer distributionId);

	CustomResponse deleteDistributionById(Integer distributionId);

	CustomResponse getForDwopDown();

	CustomResponse searchDistributionWithPaging(DtoSearch dtoSearch);

}
