package com.msm.repo;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.msm.entity.*;

import java.util.List;

/**
 * @author Nabeel Bhatti
 */
@Repository
public interface RepositoryComposition extends JpaRepository<Composition,Integer> {

    Composition findByIdAndIsDeleted(Integer id, boolean isDeleted);

    List<Composition> findByIsDeleted(boolean b);

    @Query("SELECT c FROM Composition c WHERE c.name LIKE :searchKeyWord AND c.isDeleted=false")
    List<Composition> predictiveCompositionSearchWithPagination(@Param("searchKeyWord") String searchKeyWord, Pageable pageRequest);

    @Query("SELECT COUNT(*) FROM Composition c WHERE c.name LIKE :searchKeyWord AND c.isDeleted=false")
    Integer predictiveCompositionSearchTotalCount(@Param("searchKeyWord")String searchKeyWord);
}
