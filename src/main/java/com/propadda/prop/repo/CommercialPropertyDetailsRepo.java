// Author-Hemant Arora
package com.propadda.prop.repo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.propadda.prop.dto.ExpiredPropertyView;
import com.propadda.prop.model.CommercialPropertyDetails;
import com.propadda.prop.model.Users;

@Repository
public interface CommercialPropertyDetailsRepo extends JpaRepository<CommercialPropertyDetails, Integer>, JpaSpecificationExecutor<CommercialPropertyDetails> {

    List<CommercialPropertyDetails> findByPropertyType(String propertyType);

    List<CommercialPropertyDetails> findByPreference(String preference);

    List<CommercialPropertyDetails> findByPriceLessThan(Long price);

    List<CommercialPropertyDetails> findByAreaGreaterThan(Double area);

    List<CommercialPropertyDetails> findByCabinsGreaterThanEqual(Integer cabins);

    List<CommercialPropertyDetails> findByCommercialOwner(Users owner);

    @Query("SELECT COUNT(c) FROM CommercialPropertyDetails c WHERE c.commercialOwner = :owner and adminApproved <> 'Rejected'")
    int findTotalPropertiesPostedByCommercialOwner(Users owner);

    List<CommercialPropertyDetails> findByCommercialOwnerAndAdminApprovedAndExpired(Users owner, String adminApproved, Boolean expired);

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

    @Query("SELECT COUNT(c) FROM CommercialPropertyDetails c WHERE c.state LIKE CONCAT('%', :state, '%') AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    Integer countByState(@Param("state") String state);

    @Query("SELECT c FROM CommercialPropertyDetails c WHERE c.city LIKE CONCAT('%', :city, '%') AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    List<CommercialPropertyDetails> filterByCity(@Param("city") String city);

    @Query("SELECT c FROM CommercialPropertyDetails c WHERE c.state LIKE CONCAT('%', :state, '%') AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    List<CommercialPropertyDetails> filterByState(@Param("state") String state);

    @Query("SELECT c FROM CommercialPropertyDetails c WHERE LOWER(c.propertyType) = :propertyType AND c.vip = true AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    List<CommercialPropertyDetails> getVipFilterByPropertyType(@Param("propertyType") String propertyType);

    List<CommercialPropertyDetails> findByApprovedAtAfterAndSold(OffsetDateTime cutoff, Boolean sold);

    @Query("SELECT c FROM CommercialPropertyDetails c WHERE LOWER(c.preference) = 'pg' AND c.vip= true AND c.adminApproved = 'Approved' AND c.expired = false AND c.sold = false")
    List<CommercialPropertyDetails> getVipFilterByPG();

    @Query(value = """
        SELECT *
        FROM commercial_property_details c
        WHERE c.expired = false
        AND c.sold = false
        AND c.admin_approved = 'Approved'
        AND c.approved_at IS NOT NULL
        AND (CURRENT_DATE - c.approved_at::date) IN (76, 83, 87, 89)
        AND NOT EXISTS (
            SELECT 1
            FROM property_expiry_email_log l
            WHERE l.category = 'Commercial'
                AND l.listing_id = c.listing_id
                AND l.approved_at::date = c.approved_at::date
                AND l.reminder_day = (CURRENT_DATE - c.approved_at::date)
        )
        """, nativeQuery = true)
    List<CommercialPropertyDetails> findForExpiryReminders();

    // @Modifying
    // @Query(value = """
    //     UPDATE commercial_property_details
    //     SET expired = true,
    //         approved_at = NULL,
    //         admin_approved = 'Pending'
    //     WHERE approved_at IS NOT NULL
    //     AND approved_at::date <= CURRENT_DATE - 90
    //     AND expired = false
    //     AND sold = false
    //     AND admin_approved = 'Approved'
    //     """, nativeQuery = true)
    // int expireOldApprovedProperties();
    @Query(value = """
        UPDATE residential_property_details
        SET expired = true,
            approved_at = NULL,
            admin_approved = 'Pending'
        WHERE approved_at IS NOT NULL
          AND approved_at::date <= CURRENT_DATE - 90
          AND expired = false
          AND sold = false
          AND admin_approved = 'Approved'
        RETURNING listing_id AS listingId,
                  title,
                  user_id AS userId
        """, nativeQuery = true)
    List<ExpiredPropertyView> expireAndFetchExpired();

}
