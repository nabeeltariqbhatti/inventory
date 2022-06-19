package com.msm.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.msm.entity.Distribution;

/**
 * 
 * @author IbrarHussain
 *
*/

@Repository
public interface RepositoryDistribution extends JpaRepository<Distribution, Integer>{

	Distribution findByIdAndIsDeleted(Integer id, boolean isDeleted);

	List<Distribution> findByIsDeleted(boolean isDeleted);

	@Query("SELECT COUNT(*) FROM Distribution s join s.supplier p WHERE s.name LIKE :searchKeyWord OR s.contactNumber LIKE :searchKeyWord "
			+ "OR s.email LIKE :searchKeyWord OR s.address LIKE :searchKeyWord OR "
			+ "s.faxNo LIKE :searchKeyWord OR p.name LIKE :searchKeyWord AND s.isDeleted=false")
	Integer predictiveDistributionSearchTotalCount(@Param("searchKeyWord") String searchKeyWord);

	@Query("SELECT s FROM Distribution s join s.supplier p WHERE s.name LIKE :searchKeyWord OR s.contactNumber LIKE :searchKeyWord "
			+ "OR s.email LIKE :searchKeyWord OR s.address LIKE :searchKeyWord OR "
			+ "s.faxNo LIKE :searchKeyWord OR p.name LIKE :searchKeyWord AND s.isDeleted=false")
	List<Distribution> predictiveDistributionSearchWithPagination(@Param("searchKeyWord")String searchKeyWord, Pageable pageRequest);

}
