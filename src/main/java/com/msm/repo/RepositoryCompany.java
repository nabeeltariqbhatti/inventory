package com.msm.repo;

import com.msm.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Bhatti
 */

@Repository
public interface RepositoryCompany extends JpaRepository<Company,Integer> {

	Company findByIdAndIsDeleted(Integer id, boolean isDeleted);

}
