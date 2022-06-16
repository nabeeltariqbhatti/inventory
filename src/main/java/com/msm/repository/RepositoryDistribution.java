package com.msm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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

}
