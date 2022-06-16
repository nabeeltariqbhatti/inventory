package com.msm.repository;

import com.msm.entity.Company;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
/**
 * @author NabeelBhatti
 */

@Repository
public interface RepositoryCompany extends JpaRepository<Company,Integer> {

	Company findByIdAndIsDeleted(Integer id, boolean isDeleted);

	List<Company> findByIsDeleted(boolean b);

	@Query("SELECT c FROM Company c WHERE c.name LIKE :searchKeyWord AND c.isDeleted=false")
	List<Company> predictiveCompanySearchWithPagination(@Param("searchKeyWord")String searchKeyWord, Pageable pageRequest);

	@Query("SELECT COUNT(*) FROM Company c WHERE c.name LIKE :searchKeyWord AND c.isDeleted=false")
	Integer predictiveCompanySearchTotalCount(@Param("searchKeyWord")String searchKeyWord);

}
