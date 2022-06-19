package com.msm.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msm.common.library.constant.AppConstant;
import com.msm.common.library.dto.CustomResponse;
import com.msm.common.library.util.StringUtil;
import com.msm.dto.CustomerDto;
import com.msm.dto.DtoSearch;
import com.msm.entity.Customer;
import com.msm.mapper.CustomerMapper;
import com.msm.repository.RepositoryCustomer;
import com.msm.service.ServiceCustomer;
import com.msm.service.ServiceHome;
import com.msm.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Rashid
 *
 */

@Service
public class ServiceCustomerImpl implements ServiceCustomer  {

    private static final Logger log = LoggerFactory.getLogger(ServiceCustomerImpl.class);

    private final RepositoryCustomer repositoryCustomer;

    private final ServiceHome serviceHome;

    @Autowired
    public ServiceCustomerImpl(RepositoryCustomer repositoryCustomer, ServiceHome serviceHome) {
        this.repositoryCustomer = repositoryCustomer;
        this.serviceHome = serviceHome;
    }

    @Override
    public CustomResponse saveUpdate(CustomerDto customerDto) throws JsonProcessingException  {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        Customer customer = null;

        if (StringUtil.isNotNull(customerDto.getId()) && customerDto.getId() > 0) {
            customer = repositoryCustomer.findByIdAndIsDeleted(customerDto.getId(), false);

            if (StringUtil.isNotNull(customer)) {
                customer = CustomerMapper.customerDtoToCustomerMapper(customerDto, customer, serviceHome.getUserId());

                repositoryCustomer.save(customer);
                log.info("customer updated successfully");
                endTime = System.currentTimeMillis();
                return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED).message(AppConstant.DATA_UPDATE_SUCCESSFULL).data(null)
                        .requestTime(endTime - startTime).build();
            } else {
                log.info("customer not found");
                endTime = System.currentTimeMillis();
                return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(customerDto)
                        .requestTime(endTime - startTime).build();
            }
        } else {
            customer = CustomerMapper.customerDtoToCustomerMapper(customerDto, customer, serviceHome.getUserId());
            repositoryCustomer.save(customer);
            log.info("Customer save successful:");
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.CREATED.value())
                    .status(HttpStatus.CREATED).message(AppConstant.DATA_SAVE_SUCCESSFULL).data(null)
                    .requestTime(endTime - startTime).build();
        }
    }

    @Override
    public CustomResponse getCustomerById(Integer customerId) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        long endTime = 0;

        Customer customer = repositoryCustomer.findByIdAndIsDeleted(customerId, false);
        if (StringUtil.isNotNull(customer)) {
            log.info("customer data" , new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(customer));
            CustomerDto customerDto = CustomerMapper.customerToCustomerDtoMapper(customer);
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
                    .message(AppConstant.DATA_FOUND).data(customerDto).requestTime(endTime - startTime).build();
        } else {
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
                    .requestTime(endTime - startTime).build();
        }
    }

    @Override
    public CustomResponse deleteCustomerById(Integer customerId) {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        Customer customer = repositoryCustomer.findByIdAndIsDeleted(customerId, false);
        if (StringUtil.isNotNull(customer)) {
            customer.setIsDeleted(true);
            repositoryCustomer.save(customer);
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(AppConstant.DATA_DELETED_SUCCESSFULL).data(null).requestTime(endTime - startTime).build();
        } else {
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR).message(AppConstant.INTERNAL_SERVER_ERROR).data(null)
                    .requestTime(endTime - startTime).build();
        }
    }

    @Override
    public CustomResponse getForDwopDown() {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        List<Customer> customerList = repositoryCustomer.findByIsDeleted(false);
        List<CustomerDto> customerDtos = new ArrayList<>();
        if (StringUtil.isNotNull(customerList) && !customerList.isEmpty()) {
            customerList.forEach(obj -> {
                CustomerDto customerDto = new CustomerDto();
                customerDto = CustomerMapper.customerToCustomerDtoMapper(obj);
                customerDtos.add(customerDto);
            });

            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
                    .message(AppConstant.DATA_FOUND).data(customerDtos).requestTime(endTime - startTime).build();
        } else {
            log.info(" Customer DATA NOT FOUND  For Dwop Down ");
            endTime = System.currentTimeMillis();
            return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
                    .requestTime(endTime - startTime).build();
        }
    }

    @Override
    public CustomResponse searchCustomerWithPaging(DtoSearch dtoSearch) {

        long startTime = System.currentTimeMillis();
        long endTime = 0;
        if (dtoSearch != null) {
            String condition = "";
            String searchWord = dtoSearch.getSearchKeyword();
            if (dtoSearch.getSortOn() != null || dtoSearch.getSortBy() != null) {
                if (dtoSearch.getSortOn().equals("name")) {
                    condition = dtoSearch.getSortOn();
                } else {
                    condition = "id";
                }
            } else {
                condition += "id";
                dtoSearch.setSortOn("");
                dtoSearch.setSortBy("");
            }
            List<Customer> customers = null;
            dtoSearch.setTotalCount(repositoryCustomer.predictiveCustomerSearchTotalCount("%" + searchWord + "%"));

            if (dtoSearch.getSortBy().equals("") && dtoSearch.getSortOn().equals("")) {
                customers = this.repositoryCustomer.predictiveCustomerSearchWithPagination("%" + searchWord + "%",
                        new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
                                Sort.by(Sort.Direction.ASC, condition)));
            }
            if (dtoSearch.getSortBy().equals("ASC")) {
                customers = this.repositoryCustomer.predictiveCustomerSearchWithPagination("%" + searchWord + "%",
                        new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
                                Sort.by(Sort.Direction.ASC, condition)));
            } else if (dtoSearch.getSortBy().equals("DESC")) {
                customers = this.repositoryCustomer.predictiveCustomerSearchWithPagination("%" + searchWord + "%",
                        new Pagination(dtoSearch.getPageNumber(), dtoSearch.getPageSize(),
                                Sort.by(Sort.Direction.DESC, condition)));
            }

            if (StringUtil.isNotNull(customers) && !customers.isEmpty()) {
                log.info("Found search Customer Date With Paging ");
                List<CustomerDto> customerDtos = new ArrayList<CustomerDto>();
                customers.stream().forEach(obj -> {
                    CustomerDto customerDto = CustomerMapper.customerToCustomerDtoMapper(obj);
                    customerDtos.add(customerDto);
                });
                dtoSearch.setRecords(customerDtos);
                endTime = System.currentTimeMillis();
                return new CustomResponse.CustomResponseBuilder().code(HttpStatus.FOUND.value()).status(HttpStatus.FOUND)
                        .message(AppConstant.DATA_FOUND).data(dtoSearch).requestTime(endTime - startTime).build();
            } else {
                log.info("Not Found search customer Date With Paging ");
                endTime = System.currentTimeMillis();
                return new CustomResponse.CustomResponseBuilder().code(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND).message(AppConstant.DATA_NOT_FOUND).data(null)
                        .requestTime(endTime - startTime).build();
            }
        }

        return null;
    }
}

