package com.propadda.prop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.propadda.prop.model.RejectionDetails;

@Repository
public interface RejectionDetailsRepository extends JpaRepository<RejectionDetails, Integer> {
    // optionally add findById convenience etc (provided by JpaRepository)
}