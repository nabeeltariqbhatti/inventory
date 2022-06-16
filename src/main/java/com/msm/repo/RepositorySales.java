package com.msm.repo;


import com.msm.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

/**
 * @author Nabeel Bhatti
 */
@Repository
public interface RepositorySales extends JpaRepository<Sales,Integer> {

    Sales findByIdAndIsDeleted(Integer id, boolean isDeleted);

    List<Sales> findByIsDeleted(boolean b);

    @Query("SELECT c FROM Sales c WHERE c.customerId.name LIKE :searchKeyWord   AND c.isDeleted=false")
    List<Sales> predictiveSalesSearchWithPagination(@Param("searchKeyWord") String searchKeyWord, Pageable pageRequest);

    @Query("SELECT COUNT(*) FROM Sales c WHERE c.customerId.name LIKE :searchKeyWord AND c.isDeleted=false")
    Integer predictiveSalesSearchTotalCount(@Param("searchKeyWord")String searchKeyWord);
}
