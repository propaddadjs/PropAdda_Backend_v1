package com.propadda.prop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.propadda.prop.model.ResidentialPropertyDetails;
import com.propadda.prop.model.ResidentialPropertyMedia;

@Repository
public interface ResidentialPropertyMediaRepo  extends JpaRepository<ResidentialPropertyMedia, Long>  {
    List<ResidentialPropertyMedia> findByProperty(ResidentialPropertyDetails property);
    //void deleteByResidentialPropertyDetails(ResidentialPropertyDetails property);
}
