// Author-Hemant Arora
package com.propadda.prop.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.propadda.prop.model.FavoriteListingsDetails;
import com.propadda.prop.model.Users;

@Repository
public interface FavoriteListingsDetailsRepo extends JpaRepository<FavoriteListingsDetails, Integer> {
    
    @Modifying
    @Query("DELETE FROM FavoriteListingsDetails f WHERE f.propertyId = :propertyId AND f.propertyCategory = :propertyCategory")
    void deleteByPropertyIdAndPropertyType(@Param("propertyId") Integer propertyId, @Param("propertyCategory") String propertyCategory);

    List<FavoriteListingsDetails> findByFavoritesOfBuyer(Users favoritesOfBuyer);

    Integer countByFavoritesOfBuyer(Users buyer);

    Optional<FavoriteListingsDetails> findByFavoritesOfBuyerAndPropertyIdAndPropertyCategory(Users users, Integer propertyId, String propertyCategory);

}
