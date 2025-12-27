package com.propadda.prop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.propadda.prop.model.PropertyExpiryEmailLog;

@Repository
public interface PropertyExpiryEmailLogRepository
        extends JpaRepository<PropertyExpiryEmailLog, Long> {
}
