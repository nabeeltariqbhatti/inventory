package com.msm.repository;

import com.msm.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryCustomer extends JpaRepository<Customer, Integer> {

    Customer findByIdAndIsDeleted(Integer id, boolean isDeleted);

    List<Customer> findByIsDeleted(boolean b);

    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:searchKeyWord% AND c.isDeleted=false")
    List<Customer> predictiveCustomerSearchWithPagination(@Param("searchKeyWord") String searchKeyWord, Pageable pageRequest);

    @Query("SELECT COUNT(*) FROM Customer c WHERE c.name LIKE %:searchKeyWord% AND c.isDeleted=false")
    Integer predictiveCustomerSearchTotalCount(@Param("searchKeyWord")String searchKeyWord);


}
