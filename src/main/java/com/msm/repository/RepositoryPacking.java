package com.msm.repository;

import com.msm.entity.Packing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryPacking extends JpaRepository<Packing, Integer> {

    Packing findByIdAndIsDeleted(Integer id, boolean isDeleted);

    List<Packing> findByIsDeleted(boolean isDeleted);

    @Query("SELECT COUNT(*) FROM Packing p WHERE p.name LIKE :searchKeyWord "
            + "p.MedicineDetail.name LIKE :searchKeyWord AND s.isDeleted=false")
    Integer predictivePackingSearchTotalCount(@Param("searchKeyWord") String searchKeyWord);

    @Query("SELECT COUNT(*) FROM Packing p WHERE p.name LIKE :searchKeyWord "
            + "p.MedicineDetail.name LIKE :searchKeyWord AND s.isDeleted=false")
    List<Packing> predictivePackingSearchWithPagination(@Param("searchKeyWord")String searchKeyWord, Pageable pageRequest);


}
