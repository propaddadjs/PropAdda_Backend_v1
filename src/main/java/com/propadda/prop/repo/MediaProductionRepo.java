package com.propadda.prop.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.propadda.prop.model.MediaProduction;

@Repository
public interface MediaProductionRepo extends JpaRepository<MediaProduction, Integer>  {

    List<MediaProduction> findByRequesterUserIdAndGraphics(Integer agentId, Boolean graphics);
    List<MediaProduction> findByRequesterUserIdAndPhotoshoot(Integer agentId, Boolean photoshoot);
    List<MediaProduction> findByRequesterUserId(Integer agentId);
    Optional<MediaProduction> findByRequesterUserIdAndPropertyCategoryAndPropertyId(Integer agentId, String propertyCategory, Integer propertyId);
    
}
