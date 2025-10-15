package com.propadda.prop.repo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.propadda.prop.model.CommercialPropertyDetails;
import com.propadda.prop.model.Users;

@Repository
public interface CommercialPropertyDetailsRepo extends JpaRepository<CommercialPropertyDetails, Integer>, JpaSpecificationExecutor<CommercialPropertyDetails> {

    List<CommercialPropertyDetails> findByPropertyType(String propertyType);

    List<CommercialPropertyDetails> findByPreference(String preference);

    List<CommercialPropertyDetails> findByPriceLessThan(Integer price);

    List<CommercialPropertyDetails> findByAreaGreaterThan(Double area);

    List<CommercialPropertyDetails> findByCabinsGreaterThanEqual(Integer cabins);

    List<CommercialPropertyDetails> findByCommercialOwner(Users owner);

    List<CommercialPropertyDetails> findByCommercialOwnerAndAdminApproved(Users owner, String adminApproved);

    List<CommercialPropertyDetails> findByCommercialOwnerAndExpired(Users owner, Boolean expired);

    List<CommercialPropertyDetails> findByCommercialOwnerAndSold(Users owner, Boolean sold);

    List<CommercialPropertyDetails> findByCommercialOwnerAndExpiredAndSold(Users owner, Boolean expired, Boolean sold);

    Optional<CommercialPropertyDetails> findByListingIdAndCommercialOwnerAndExpiredAndSold(Integer listingId, Users owner, Boolean expired, Boolean sold);

    List<CommercialPropertyDetails> findByAdminApprovedAndSoldAndExpired(String adminApproved, Boolean sold, Boolean expired);

    List<CommercialPropertyDetails> findByAdminApprovedAndSoldAndExpiredAndVip(String adminApproved, Boolean sold, Boolean expired, Boolean vip);

    List<CommercialPropertyDetails> findBySold(Boolean sold);

    Optional<CommercialPropertyDetails> findByListingIdAndCommercialOwner(Integer listingId, Users agent);

    List<CommercialPropertyDetails> findByCommercialOwnerAndAdminApprovedAndExpiredAndSold(Users owner, String adminApproved, Boolean expired, Boolean sold);

    Optional<CommercialPropertyDetails> findByListingIdAndCommercialOwner_UserId(Integer listingId, Integer userId);

    @Query("SELECT c FROM CommercialPropertyDetails c WHERE c.preference = :preference AND c.state= :state AND c.city = :city AND c.locality LIKE CONCAT('%', :locality, '%') AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    List<CommercialPropertyDetails> filterByPreferenceAndLocation(@Param("preference") String preference, @Param("state") String state, @Param("city") String city,
             @Param("locality") String locality);

    @Query("SELECT c FROM CommercialPropertyDetails c WHERE c.propertyType = 'Plot/Land' AND c.state= :state AND c.city = :city AND c.locality LIKE CONCAT('%', :locality, '%') AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    List<CommercialPropertyDetails> filterByPlotAndLocation(@Param("state") String state, @Param("city") String city, @Param("locality") String locality);

    @Query("SELECT COUNT(c) FROM CommercialPropertyDetails c WHERE c.city LIKE CONCAT('%', :city, '%') AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    Integer countByCity(@Param("city") String city);

    @Query("SELECT c FROM CommercialPropertyDetails c WHERE c.city LIKE CONCAT('%', :city, '%') AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    List<CommercialPropertyDetails> filterByCity(@Param("city") String city);

    @Query("SELECT c FROM CommercialPropertyDetails c WHERE LOWER(c.propertyType) = :propertyType AND c.vip = true AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    List<CommercialPropertyDetails> getVipFilterByPropertyType(@Param("propertyType") String propertyType);

    List<CommercialPropertyDetails> findByApprovedAtAfterAndSold(OffsetDateTime cutoff, Boolean sold);

    @Query("SELECT c FROM CommercialPropertyDetails c WHERE LOWER(c.preference) = 'pg' AND c.vip= true AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    List<CommercialPropertyDetails> getVipFilterByPG();

}
