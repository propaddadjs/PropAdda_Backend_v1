// Author-Hemant Arora
package com.propadda.prop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.propadda.prop.dto.CommercialPropertyResponse;
import com.propadda.prop.dto.ResidentialPropertyResponse;
import com.propadda.prop.model.NotificationDetails;
import com.propadda.prop.service.AdminService;

import jakarta.mail.MessagingException;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/allProperties")
    public ResponseEntity<?> getAllProperties(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllProperties(page,size));
    }

    @GetMapping("/pendingProperties")
    public ResponseEntity<?> getPendingProperties() {
        return ResponseEntity.ok(adminService.getPendingProperties());
    }

    @GetMapping("/expiredProperties")
    public ResponseEntity<?> getExpiredProperties() {
        return ResponseEntity.ok(adminService.getExpiredProperties());
    }

    @GetMapping("/vipProperties")
    public ResponseEntity<?> getVipProperties() {
        return ResponseEntity.ok(adminService.getVipProperties());
    }

    @GetMapping("/soldProperties")
    public ResponseEntity<?> getSoldProperties() {
        return ResponseEntity.ok(adminService.getSoldProperties());
    }

    @GetMapping("/filterAllProperties")
    public ResponseEntity<?> filterAllProperties(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String propertyTypes,
            @RequestParam(required = false) String preference,
            @RequestParam(required = false) Long priceMin,
            @RequestParam(required = false) Long priceMax,
            @RequestParam(required = false) String furnishing,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String amenities,
            @RequestParam(required = false) String availability,
            @RequestParam(required = false) Integer areaMin,
            @RequestParam(required = false) Integer areaMax,
            @RequestParam(required = false) String ageRanges,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        System.out.println("category: "+category);
        System.out.println("propertyTypes: "+propertyTypes);
        System.out.println("preference: "+preference);
        System.out.println("priceMin: "+priceMin);
        System.out.println("priceMax: "+priceMax);
        System.out.println("furnishing: "+furnishing);
        System.out.println("state: "+state);
        System.out.println("city: "+city);
        System.out.println("amenities: "+amenities);
        System.out.println("availability: "+availability);
        System.out.println("areaMin: "+areaMin);
        System.out.println("areaMax: "+areaMax);
        System.out.println("ageRanges: "+ageRanges);

    //     Map<String, List<?>> response = new HashMap<>();
    //     if(category==null){
    //         response = adminService.combinedFilteredAllPropList(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
    //         System.out.println("When category is null, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
    //     } else
    //     if(category.equalsIgnoreCase("Residential")){
    //         List<ResidentialPropertyResponse> resProp = adminService.filterAllResProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
    //         response.put("residential",resProp);
    //         response.put("commercial",null);
    //         System.out.println("When category is Residential, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
    //     }else
    //     if(category.equalsIgnoreCase("Commercial")){
    //         List<CommercialPropertyResponse> comProp = adminService.filterAllComProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
    //         response.put("residential",null);
    //         response.put("commercial",comProp);
    //         System.out.println("When category is Commercial, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
    //     }else{
    //         System.out.println("Response set as null");
    //         response = null;
    //     }

    // return ResponseEntity.ok(response);
    return ResponseEntity.ok(
        adminService.filterAllPropertiesPaged(
            category, propertyTypes, preference,
            priceMin, priceMax, furnishing,
            state, city, amenities, availability,
            areaMin, areaMax, ageRanges,
            page, size
        )
    );
    }

    @GetMapping("/filterPendingProperties")
    public ResponseEntity<?> filterPendingProperties(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String propertyTypes,
            @RequestParam(required = false) String preference,
            @RequestParam(required = false) Long priceMin,
            @RequestParam(required = false) Long priceMax,
            @RequestParam(required = false) String furnishing,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String amenities,
            @RequestParam(required = false) String availability,
            @RequestParam(required = false) Integer areaMin,
            @RequestParam(required = false) Integer areaMax,
            @RequestParam(required = false) String ageRanges,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        System.out.println("category: "+category);
        System.out.println("propertyTypes: "+propertyTypes);
        System.out.println("preference: "+preference);
        System.out.println("priceMin: "+priceMin);
        System.out.println("priceMax: "+priceMax);
        System.out.println("furnishing: "+furnishing);
        System.out.println("state: "+state);
        System.out.println("city: "+city);
        System.out.println("amenities: "+amenities);
        System.out.println("availability: "+availability);
        System.out.println("areaMin: "+areaMin);
        System.out.println("areaMax: "+areaMax);
        System.out.println("ageRanges: "+ageRanges);

        Map<String, List<?>> response = new HashMap<>();
        if(category==null){
            response = adminService.combinedFilteredPendingPropList(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
            System.out.println("When category is null, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
        } else
        if(category.equalsIgnoreCase("Residential")){
            List<ResidentialPropertyResponse> resProp = adminService.filterPendingResProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
            response.put("residential",resProp);
            response.put("commercial",null);
            System.out.println("When category is Residential, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
        }else
        if(category.equalsIgnoreCase("Commercial")){
            List<CommercialPropertyResponse> comProp = adminService.filterPendingComProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
            response.put("residential",null);
            response.put("commercial",comProp);
            System.out.println("When category is Commercial, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
        }else{
            System.out.println("Response set as null");
            response = null;
        }

    return ResponseEntity.ok(response);
    }

    @GetMapping("/filterExpiredProperties")
    public ResponseEntity<?> filterExpiredProperties(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String propertyTypes,
            @RequestParam(required = false) String preference,
            @RequestParam(required = false) Long priceMin,
            @RequestParam(required = false) Long priceMax,
            @RequestParam(required = false) String furnishing,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String amenities,
            @RequestParam(required = false) String availability,
            @RequestParam(required = false) Integer areaMin,
            @RequestParam(required = false) Integer areaMax,
            @RequestParam(required = false) String ageRanges,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        System.out.println("category: "+category);
        System.out.println("propertyTypes: "+propertyTypes);
        System.out.println("preference: "+preference);
        System.out.println("priceMin: "+priceMin);
        System.out.println("priceMax: "+priceMax);
        System.out.println("furnishing: "+furnishing);
        System.out.println("state: "+state);
        System.out.println("city: "+city);
        System.out.println("amenities: "+amenities);
        System.out.println("availability: "+availability);
        System.out.println("areaMin: "+areaMin);
        System.out.println("areaMax: "+areaMax);
        System.out.println("ageRanges: "+ageRanges);

        Map<String, List<?>> response = new HashMap<>();
        if(category==null){
            response = adminService.combinedFilteredExpiredPropList(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
            System.out.println("When category is null, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
        } else
        if(category.equalsIgnoreCase("Residential")){
            List<ResidentialPropertyResponse> resProp = adminService.filterExpiredResProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
            response.put("residential",resProp);
            response.put("commercial",null);
            System.out.println("When category is Residential, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
        }else
        if(category.equalsIgnoreCase("Commercial")){
            List<CommercialPropertyResponse> comProp = adminService.filterExpiredComProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
            response.put("residential",null);
            response.put("commercial",comProp);
            System.out.println("When category is Commercial, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
        }else{
            System.out.println("Response set as null");
            response = null;
        }

    return ResponseEntity.ok(response);
    }
    
    @GetMapping("/filterVipProperties")
    public ResponseEntity<?> filterVipProperties(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String propertyTypes,
            @RequestParam(required = false) String preference,
            @RequestParam(required = false) Long priceMin,
            @RequestParam(required = false) Long priceMax,
            @RequestParam(required = false) String furnishing,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String amenities,
            @RequestParam(required = false) String availability,
            @RequestParam(required = false) Integer areaMin,
            @RequestParam(required = false) Integer areaMax,
            @RequestParam(required = false) String ageRanges,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        System.out.println("category: "+category);
        System.out.println("propertyTypes: "+propertyTypes);
        System.out.println("preference: "+preference);
        System.out.println("priceMin: "+priceMin);
        System.out.println("priceMax: "+priceMax);
        System.out.println("furnishing: "+furnishing);
        System.out.println("state: "+state);
        System.out.println("city: "+city);
        System.out.println("amenities: "+amenities);
        System.out.println("availability: "+availability);
        System.out.println("areaMin: "+areaMin);
        System.out.println("areaMax: "+areaMax);
        System.out.println("ageRanges: "+ageRanges);

        Map<String, List<?>> response = new HashMap<>();
        if(category==null){
            response = adminService.combinedFilteredVipPropList(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
            System.out.println("When category is null, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
        } else
        if(category.equalsIgnoreCase("Residential")){
            List<ResidentialPropertyResponse> resProp = adminService.filterVipResProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
            response.put("residential",resProp);
            response.put("commercial",null);
            System.out.println("When category is Residential, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
        }else
        if(category.equalsIgnoreCase("Commercial")){
            List<CommercialPropertyResponse> comProp = adminService.filterVipComProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
            response.put("residential",null);
            response.put("commercial",comProp);
            System.out.println("When category is Commercial, response for filters: Residential::: "+ response.get("residential")+" Commercial::: "+ response.get("commercial"));
        }else{
            System.out.println("Response set as null");
            response = null;
        }

    return ResponseEntity.ok(response);
    }

    @GetMapping("/property/{category}/{listingId}")
    public ResponseEntity<?> getPropertyById(@PathVariable Integer listingId, @PathVariable String category) {
        Object prop = adminService.getPropertyById(listingId, category);
        System.out.println("In controller getbyid response::::::"+ResponseEntity.ok(prop));
        return ResponseEntity.ok(prop);
    }

    @PatchMapping("/properties/approve/{category}/{listingId}")
    public ResponseEntity<?> approveProperty(@PathVariable String category, @PathVariable Integer listingId) throws MessagingException {
        boolean ok = adminService.approveProperty(listingId, category);
        if (ok) return ResponseEntity.ok().build();
        return ResponseEntity.status(404).body("Property not found");
    }

    @PatchMapping("/properties/reject/{category}/{listingId}")
    public ResponseEntity<?> rejectProperty(@PathVariable String category, @PathVariable Integer listingId,
                                            @RequestBody(required = false) Map<String, String> body) throws MessagingException {
        String reason = body != null ? body.getOrDefault("reason", "") : "";
        boolean ok = adminService.rejectProperty(listingId, category, reason);
        if (ok) return ResponseEntity.ok().build();
        return ResponseEntity.status(404).body("Property not found");
    }

    @PatchMapping("/properties/markPropertyAsSold/{category}/{listingId}")
    public ResponseEntity<?> markPropertyAsSold(@PathVariable String category, @PathVariable Integer listingId) {
        boolean ok = adminService.markPropertyAsSold(listingId, category);
        if (ok) return ResponseEntity.ok().build();
        return ResponseEntity.status(404).body("Property not found");
    }

    @PatchMapping("/toggleExpired/{category}/{listingId}")
    public ResponseEntity<?> toggleExpired(@PathVariable Integer listingId, @PathVariable String category) {
        Object propObject = adminService.toggleExpired(listingId, category);
        return ResponseEntity.ok(propObject);
    }

    @PatchMapping("/renewProperty/{category}/{listingId}")
    public ResponseEntity<?> renewProperty(@PathVariable Integer listingId, @PathVariable String category) throws MessagingException {
        Object propObject = adminService.renewProperty(listingId, category);
        return ResponseEntity.ok(propObject);
    }

    @PatchMapping("/notifyDealer/{category}/{listingId}")
    public ResponseEntity<?> notifyDealer(@PathVariable Integer listingId, @PathVariable String category) throws MessagingException {
        Object propObject = adminService.notifyDealer(listingId, category);
        return ResponseEntity.ok(propObject);
    }

    @PatchMapping("/toggleVip/{category}/{listingId}")
    public ResponseEntity<?> toggleVip(@PathVariable Integer listingId, @PathVariable(value="category") String category) {
        Object propObject = adminService.toggleVip(listingId, category);
        return ResponseEntity.ok(propObject);
    }

    @PatchMapping("/toggleReraVerified/{category}/{listingId}")
    public ResponseEntity<?> toggleReraVerified(@PathVariable Integer listingId, @PathVariable String category) {
        Object propObject = adminService.toggleReraVerified(listingId, category);
        return ResponseEntity.ok(propObject);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/allSellers")
    public ResponseEntity<?> getAllSellers() {
        return ResponseEntity.ok(adminService.getAllSellers());
    }

    @PatchMapping("/togglePropaddaVerified/{userId}")
    public ResponseEntity<?> togglePropaddaVerified(@PathVariable Integer userId) {
        Object propObject = adminService.togglePropaddaVerified(userId);
        return ResponseEntity.ok(propObject);
    }

    @GetMapping("/pendingKycUsers")
    public ResponseEntity<?> pendingKycUsers() {
        return ResponseEntity.ok(adminService.pendingKycUsers());
    }

    @PatchMapping("/sellerKyc/approve/{userId}")
    public ResponseEntity<?> approveKyc(@PathVariable Integer userId) throws MessagingException {
        boolean ok = adminService.approveKyc(userId);
        if (ok) return ResponseEntity.ok().build();
        return ResponseEntity.status(404).body("User not found");
    }

    @PatchMapping("/sellerKyc/reject/{userId}")
    public ResponseEntity<?> rejectKyc(@PathVariable Integer userId,
                                        @RequestBody(required = false) Map<String, String> body) throws MessagingException {
                                            String reason = body != null ? body.getOrDefault("reason", "") : "";
        boolean ok = adminService.rejectKyc(userId, reason);
        if (ok) return ResponseEntity.ok().build();
        return ResponseEntity.status(404).body("User not found");
    }

    @GetMapping("/dashboardMetrics")
    public ResponseEntity<?> dashboardMetrics(){
        return ResponseEntity.ok(adminService.dashboardMetrics());
    }

    @PostMapping("/addNotification")
    public ResponseEntity<?> addNotification(@RequestBody NotificationDetails notification){
        return ResponseEntity.ok(adminService.addNotification(notification));
    }

    @GetMapping("/allNotifications")
    public ResponseEntity<?> getAllNotificationsForAdmin(){
        return ResponseEntity.ok(adminService.getAllNotificationsForAdmin());
    }

    @GetMapping("/newNotifications")
    public ResponseEntity<?> getNewNotificationsForAdmin(){
        return ResponseEntity.ok(adminService.getNewNotificationsForAdmin());
    }

    @GetMapping("/getNotificationCount")
    public ResponseEntity<?> getNotificationCount(){
        return ResponseEntity.ok(adminService.getNotificationCount());
    }

    @PatchMapping("/markNotificationViewed/{notificationId}")
    public ResponseEntity<?> markNotificationViewed(@PathVariable Integer notificationId){
        return ResponseEntity.ok(adminService.markNotificationViewed(notificationId));
    }

    @PatchMapping("/markAllNotificationViewedForAdmin")
    public ResponseEntity<?> markAllNotificationViewedForAdmin(){
        return ResponseEntity.ok(adminService.markAllNotificationViewedForAdmin());
    }
    
    @GetMapping("/getCreatedLeads")
    public ResponseEntity<?> getCreatedLeads(){
        return ResponseEntity.ok(adminService.getCreatedLeads());
    }

    @PatchMapping("/markLeadAsAssigned/{enquiryId}")
    public ResponseEntity<?> markLeadAsAssigned(@PathVariable Integer enquiryId) {
        return ResponseEntity.ok(adminService.markLeadAsAssigned(enquiryId));
    }

    @PatchMapping("/markLeadAsRemoved/{enquiryId}")
    public ResponseEntity<?> markLeadAsRemoved(@PathVariable Integer enquiryId) {
        return ResponseEntity.ok(adminService.markLeadAsRemoved(enquiryId));
    }

    @GetMapping("/getAssignedLeads")
    public ResponseEntity<?> getAssignedLeads(){
        return ResponseEntity.ok(adminService.getAssignedLeads());
    }

    @PatchMapping("/markLeadAsSold/{enquiryId}")
    public ResponseEntity<?> markLeadAsSold(@PathVariable Integer enquiryId) {
        return ResponseEntity.ok(adminService.markLeadAsSold(enquiryId));
    }

    @PatchMapping("/markLeadAsNotInterested/{enquiryId}")
    public ResponseEntity<?> markLeadAsNotInterested(@PathVariable Integer enquiryId) {
        return ResponseEntity.ok(adminService.markLeadAsNotInterested(enquiryId));
    }

    @GetMapping("/getMediaProductionRequests")
    public ResponseEntity<?> getMediaProductionRequests(){
        return ResponseEntity.ok(adminService.getMediaProductionRequests());
    }
}
