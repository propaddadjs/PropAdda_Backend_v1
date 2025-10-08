package com.propadda.prop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.propadda.prop.model.HelpDetails;

@Repository
public interface HelpDetailsRepo extends JpaRepository<HelpDetails, Integer> {
    
}
