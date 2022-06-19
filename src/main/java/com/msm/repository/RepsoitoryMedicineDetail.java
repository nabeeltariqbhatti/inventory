package com.msm.repository;

import com.msm.entity.Distribution;
import com.msm.entity.MedicineDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepsoitoryMedicineDetail {

    MedicineDetail findByIdAndIsDeleted(Integer id, boolean isDeleted);

    List<MedicineDetail> findByIsDeleted(boolean isDeleted);

    @Query("SELECT COUNT(*) FROM MedicineDetail p join p.packing p WHERE p.name LIKE :searchKeyWord OR s.contactNumber LIKE :searchKeyWord "
            + "OR s.email LIKE :searchKeyWord OR s.address LIKE :searchKeyWord OR "
            + "s.faxNo LIKE :searchKeyWord OR p.name LIKE :searchKeyWord AND s.isDeleted=false")
    Integer predictiveDistributionSearchTotalCount(@Param("searchKeyWord") String searchKeyWord);

}
