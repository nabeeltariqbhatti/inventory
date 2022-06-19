package com.msm.repository;




import com.msm.entity.Medicine;
import com.msm.entity.Medicine;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Nabeel Bhatti
 */
@Repository
public interface RepositoryMedicine extends JpaRepository<Medicine,Integer> {

    Medicine findByIdAndIsDeleted(Integer id, boolean isDeleted);

    List<Medicine> findByIsDeleted(boolean b);

    @Query("SELECT m FROM Medicine m WHERE m.name LIKE :searchKeyWord OR m.company.name LIKE :searchKeyWord AND m.isDeleted=false")
    List<Medicine> predictiveMedicineSearchWithPagination(@Param("searchKeyWord") String searchKeyWord, Pageable pageRequest);

    @Query("SELECT COUNT(*) FROM Medicine m WHERE m.name LIKE :searchKeyWord OR m.company.name LIKE :searchKeyWord AND m.isDeleted=false")
    Integer predictiveMedicineSearchTotalCount(@Param("searchKeyWord")String searchKeyWord);
}
