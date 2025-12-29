package com.propadda.prop.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.propadda.prop.model.AllPropertyView;

public interface AllPropertyViewRepository
        extends JpaRepository<AllPropertyView, String>,
                JpaSpecificationExecutor<AllPropertyView> {

    Page<AllPropertyView> findByAdminApprovedAndExpiredAndSold(
        String adminApproved,
        Boolean expired,
        Boolean sold,
        Pageable pageable
    );

    Page<AllPropertyView> findByAdminApprovedAndExpiredAndSoldAndVip(
        String adminApproved,
        Boolean expired,
        Boolean sold,
        Boolean vip,
        Pageable pageable
    );
}