package com.propadda.prop.repo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.propadda.prop.model.ResidentialPropertyDetails;
import com.propadda.prop.model.Users;

@Repository
public interface ResidentialPropertyDetailsRepo extends JpaRepository<ResidentialPropertyDetails, Integer>, JpaSpecificationExecutor<ResidentialPropertyDetails> {

    List<ResidentialPropertyDetails> findByResidentialOwner(Users owner);

    List<ResidentialPropertyDetails> findByResidentialOwnerAndAdminApproved(Users owner, String adminApproved);

    List<ResidentialPropertyDetails> findByResidentialOwnerAndExpired(Users owner, Boolean expired);

    List<ResidentialPropertyDetails> findByResidentialOwnerAndSold(Users owner, Boolean sold);

    List<ResidentialPropertyDetails> findByResidentialOwnerAndExpiredAndSold(Users owner, Boolean expired, Boolean sold);

    Optional<ResidentialPropertyDetails> findByListingIdAndResidentialOwnerAndExpiredAndSold(Integer listingId, Users owner, Boolean expired, Boolean sold);

    List<ResidentialPropertyDetails> findByAdminApprovedAndSoldAndExpired(String adminApproved, Boolean sold, Boolean expired);

    List<ResidentialPropertyDetails> findByAdminApprovedAndSoldAndExpiredAndVip(String adminApproved, Boolean sold, Boolean expired, Boolean vip);

    List<ResidentialPropertyDetails> findBySold(Boolean sold);

    // @Query("SELECT r FROM ResidentialPropertyDetails r where adminApproved= :adminApproved, sold=")
    // List<ResidentialPropertyDetails> getResPropToApplyFilter(String adminApproved, Boolean sold, Boolean expired);

    // String preference, String furnishing, String state, String city, String availability, Integer priceMin, Integer priceMax, Integer areaMin, Integer areaMax

    List<ResidentialPropertyDetails> findByAdminApprovedAndSoldAndExpiredAndPreferenceAndFurnishingAndStateAndCityAndAvailabilityAndPriceLessThanEqualAndPriceGreaterThanEqualAndAreaLessThanEqualAndAreaGreaterThanEqual(
    String adminApproved, 
    Boolean sold, 
    Boolean expired, 
    String preference, 
    String furnishing, 
    String state, 
    String city, 
    String availability, 
    Integer maxPrice,    // Maps to PriceLessThanEqual
    Integer minPrice,    // Maps to PriceGreaterThanEqual
    Double maxArea,      // Maps to AreaLessThanEqual (Use Double since 'area' is Double)
    Double minArea       // Maps to AreaGreaterThanEqual (Use Double since 'area' is Double)
);

    Optional<ResidentialPropertyDetails> findByListingIdAndResidentialOwner(Integer listingId, Users agent);

    List<ResidentialPropertyDetails> findByResidentialOwnerAndAdminApprovedAndExpiredAndSold(Users owner, String adminApproved, Boolean expired, Boolean sold);

    Optional<ResidentialPropertyDetails> findByListingIdAndResidentialOwner_UserId(Integer listingId, Integer userId);

    @Query("SELECT r FROM ResidentialPropertyDetails r WHERE r.preference = :preference AND r.state= :state AND r.city = :city AND r.locality LIKE CONCAT('%', :locality, '%') AND r.adminApproved = 'Approved' AND r.expired = false AND r.sold = false")
    List<ResidentialPropertyDetails> filterByPreferenceAndLocation(@Param("preference") String preference, @Param("state") String state, @Param("city") String city,
            @Param("locality") String locality);

    @Query("SELECT r FROM ResidentialPropertyDetails r WHERE r.propertyType = 'Plot' AND r.state= :state AND r.city = :city AND r.locality LIKE CONCAT('%', :locality, '%') AND r.adminApproved = 'Approved' AND r.expired = false AND r.sold = false")
    List<ResidentialPropertyDetails> filterByPlotAndLocation(@Param("state") String state, @Param("city") String city, @Param("locality") String locality);

    @Query("SELECT COUNT(r) FROM ResidentialPropertyDetails r WHERE r.city LIKE CONCAT('%', :city, '%') AND r.adminApproved = 'Approved' AND r.expired = false AND r.sold = false")
    Integer countByCity(@Param("city") String city);

    @Query("SELECT r FROM ResidentialPropertyDetails r WHERE r.city LIKE CONCAT('%', :city, '%') AND r.adminApproved = 'Approved' AND r.expired = false AND r.sold = false")
    List<ResidentialPropertyDetails> filterByCity(@Param("city") String city);

    @Query("SELECT r FROM ResidentialPropertyDetails r WHERE LOWER(r.propertyType) = :propertyType AND r.vip = true AND r.adminApproved = 'Approved' AND r.expired = false AND r.sold = false")
    List<ResidentialPropertyDetails> getVipFilterByPropertyType(@Param("propertyType") String propertyType);

    List<ResidentialPropertyDetails> findByApprovedAtAfterAndSold(OffsetDateTime cutoff, Boolean sold);

    @Query("SELECT r FROM ResidentialPropertyDetails r WHERE LOWER(r.preference) = 'pg' AND r.vip= true AND r.adminApproved = 'Approved' AND r.expired = false AND r.sold = false")
    List<ResidentialPropertyDetails> getVipFilterByPG();

}
