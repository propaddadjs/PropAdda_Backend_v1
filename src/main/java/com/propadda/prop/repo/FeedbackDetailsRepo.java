package com.propadda.prop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.propadda.prop.model.FeedbackDetails;

@Repository
public interface FeedbackDetailsRepo extends JpaRepository<FeedbackDetails, Integer> {
    
}
