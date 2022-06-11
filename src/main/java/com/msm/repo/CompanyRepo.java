package com.msm.repo;

import com.msm.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Bhatti
 */

@Repository
public interface CompanyRepo extends JpaRepository<Company,Integer> {

}
