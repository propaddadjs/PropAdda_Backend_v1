// Author-Hemant Arora
package com.propadda.prop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.propadda.prop.model.EnquiredListingsDetails;
import com.propadda.prop.model.FavoriteListingsDetails;
import com.propadda.prop.security.CustomUserDetails;
import com.propadda.prop.service.BuyerService;

import jakarta.mail.MessagingException;

@RestController
@PreAuthorize("hasAnyRole('BUYER','AGENT','ADMIN')")
@RequestMapping("/buyer")
public class BuyerController {
    
    @Autowired
    private BuyerService buyerService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allFavoritePropertiesByBuyer")
    public ResponseEntity<?> allFavoritePropertiesByBuyer(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer buyerId = cud.getUser().getUserId();
        return ResponseEntity.ok(buyerService.allFavoritePropertiesByBuyer(buyerId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allEnquiriesByBuyer")
    public ResponseEntity<?> allEnquiriesByBuyer(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer buyerId = cud.getUser().getUserId();
        return ResponseEntity.ok(buyerService.allEnquiriesByBuyer(buyerId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addPropertyToFavoritesForBuyer/{category}/{listingId}")
    public ResponseEntity<?> addPropertyToFavoritesForBuyer(@AuthenticationPrincipal CustomUserDetails cud, @PathVariable String category, @PathVariable Integer listingId) {
        Integer buyerId = cud.getUser().getUserId();
        FavoriteListingsDetails f = buyerService.addPropertyToFavoritesForBuyer(category,listingId,buyerId);
        if(f==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(f);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/removePropertyFromFavoritesForBuyer/{category}/{listingId}")
    public ResponseEntity<?> removePropertyFromFavoritesForBuyer(@AuthenticationPrincipal CustomUserDetails cud,
                                                                @PathVariable String category,
                                                                @PathVariable Integer listingId) {
        Integer buyerId = cud.getUser().getUserId();
        boolean removed = buyerService.removePropertyFromFavoritesForBuyer(category, listingId, buyerId);
        if (removed) {
            // 204 No Content is fine, or 200 with a body
            return ResponseEntity.noContent().build();
        } else {
            // return 404 if it wasn't found; client can treat missing as already-removed
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/checkFavorite/{category}/{listingId}")
    public ResponseEntity<?> checkFavorite(@AuthenticationPrincipal CustomUserDetails cud, @PathVariable String category, @PathVariable Integer listingId) {
        Integer buyerId = cud.getUser().getUserId();
        Boolean f = buyerService.checkFavorite(category,listingId,buyerId);
        return ResponseEntity.ok(f);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sendEnquiriesFromBuyer/{category}/{listingId}")
    public ResponseEntity<?> sendEnquiriesFromBuyer(@AuthenticationPrincipal CustomUserDetails cud, @RequestBody EnquiredListingsDetails enquiry, @PathVariable String category, @PathVariable Integer listingId) throws MessagingException {
        Integer buyerId = cud.getUser().getUserId();
        EnquiredListingsDetails e = buyerService.sendEnquiriesFromBuyer(enquiry,category,listingId,buyerId);
        if(e==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(e);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/checkEnquiry/{category}/{listingId}")
    public ResponseEntity<?> checkEnquiry(@AuthenticationPrincipal CustomUserDetails cud, @RequestBody EnquiredListingsDetails enquiry, @PathVariable String category, @PathVariable Integer listingId) {
        Integer buyerId = cud.getUser().getUserId();
        Boolean e = buyerService.checkEnquiry(enquiry,category,listingId,buyerId);
        return ResponseEntity.ok(e);
    }
}
