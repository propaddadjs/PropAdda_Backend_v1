package com.propadda.prop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.propadda.prop.model.AllPropertyViewFilter;

public interface AllPropertyViewFilterRepository
        extends JpaRepository<AllPropertyViewFilter, String>,
                JpaSpecificationExecutor<AllPropertyViewFilter> {
}
