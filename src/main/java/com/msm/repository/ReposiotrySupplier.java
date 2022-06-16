package com.msm.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.msm.entity.Supplier;

/**
 * 
 * @author IbrarHussain
 *
 */
@Repository
public interface ReposiotrySupplier extends JpaRepository<Supplier, Integer> {

	Supplier findByIdAndIsDeleted(Integer id, boolean isDeleted);

	List<Supplier> findByIsDeleted(boolean isDeleted);

	@Query("SELECT COUNT(*) FROM Supplier s WHERE s.name LIKE :searchKeyWord OR s.contactNumber LIKE :searchKeyWord "
			+ "OR s.email LIKE :searchKeyWord OR s.address LIKE :searchKeyWord OR "
			+ "s.distribution.name LIKE :searchKeyWord AND s.isDeleted=false")
	Integer predictiveSupplieSearchTotalCount(@Param("searchKeyWord") String searchKeyWord);

	@Query("SELECT s FROM Supplier s WHERE s.name LIKE :searchKeyWord OR s.contactNumber LIKE :searchKeyWord "
			+ "OR s.email LIKE :searchKeyWord OR s.address LIKE :searchKeyWord OR "
			+ "s.distribution.name LIKE :searchKeyWord AND s.isDeleted=false")
	List<Supplier> predictiveSupplieSearchWithPagination(@Param("searchKeyWord")String searchKeyWord, Pageable pageRequest);

}
