package com.msm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.CompositionDto;
import com.msm.dto.DtoSearch;
import com.msm.service.ServiceComposition;
import com.msm.service.ServiceComposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Nabeel Bhatti
 */

@RestController
@RequestMapping("/api/composition")
public class CompositionController {

    private static final Logger log = LoggerFactory.getLogger(CompositionController.class);
    private final ServiceComposition serviceComposition;

    @Autowired
    public CompositionController(ServiceComposition serviceComposition) {
        this.serviceComposition = serviceComposition;
    }

    @PostMapping
    public CustomResponse saveComposition(@RequestBody CompositionDto compositionDto) throws JsonProcessingException {

        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("Composition request create " + new ObjectMapper().writeValueAsString(compositionDto));
        if (StringUtil.isNotNull(compositionDto)) {

            return serviceComposition.saveUpdate(compositionDto);
        } else {
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message("").data("").requestTime(endTime - startTime).build();
        }
    }

    @PutMapping
    public CustomResponse updateComposition(@RequestBody CompositionDto compositionDto) throws JsonProcessingException {

        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("Composition update request " + new ObjectMapper().writeValueAsString(compositionDto));
        if (StringUtil.isNotNull(compositionDto)) {

            return serviceComposition.saveUpdate(compositionDto);
        } else {
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(compositionDto)
                    .requestTime(endTime - startTime).build();
        }
    }

    @GetMapping("/{companyId}")
    public CustomResponse getCompositionById(@PathVariable(required = true) Integer companyId) {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("Composition get  By id request " + companyId);
        if (StringUtil.isNotNull(companyId)) {

            return serviceComposition.getCompositionById(companyId);
        } else {
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(companyId)
                    .requestTime(endTime - startTime).build();
        }
    }

    @DeleteMapping("/{companyId}")
    public CustomResponse deleteCompositionById(@PathVariable(required = true) Integer companyId) {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        log.info("Composition delete  By id request " + companyId);
        if (StringUtil.isNotNull(companyId)) {

            return serviceComposition.deleteCompositionById(companyId);
        } else {
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST).message("BAD REQUEST").data(companyId)
                    .requestTime(endTime - startTime).build();
        }
    }

    @GetMapping("")
    public CustomResponse getForDwopDown() {

        log.info("Composition get for drop down ");

        return serviceComposition.getForDwopDown();
    }


    private Integer pageNumber = 0;
    private Integer pageSize = 10;
    private Integer totalCount;
    private Object records;
    private String sortOn;
    private String sortBy;


    private String fromDate;
    private String toDate;
    @GetMapping("/searchCompositionWithPaging")
    public CustomResponse searchCompositionWithPaging(@RequestParam(required = false) String searchKeyword,
                                                      @RequestParam(required = false,defaultValue = "0") Integer pageNumber,
                                                      @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                                      @RequestParam(required = false) Integer totalCount,
                                                      @RequestParam(required = false) String sortOn,
                                                      @RequestParam(required = false) String sortBy,
                                                      @RequestParam(required = false) String fromDate,
                                                      @RequestParam(required = false) String toDate
                                                      ) throws JsonProcessingException {

        log.info("search Composition With Paging .. {}  {} {} {} {} {} {} {}",searchKeyword,pageNumber,pageSize,sortBy,sortOn,totalCount,fromDate,toDate );
        return serviceComposition.searchCompositionWithPaging(searchKeyword,pageNumber,pageSize,sortBy,sortOn,totalCount,fromDate,toDate);
    }

}
