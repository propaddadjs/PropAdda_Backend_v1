package com.propadda.prop.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.propadda.prop.dto.AgentResponse;
import com.propadda.prop.dto.DetailedFilterRequest;
import com.propadda.prop.dto.FilterRequest;
import com.propadda.prop.dto.PasswordUpdateRequest;
import com.propadda.prop.dto.UserRequest;
import com.propadda.prop.model.FeedbackDetails;
import com.propadda.prop.model.HelpDetails;
import com.propadda.prop.security.CustomUserDetails;
import com.propadda.prop.service.UserService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PreAuthorize("permitAll()")
    @GetMapping("/getPropertyDetails/{category}/{listingId}")  
    public ResponseEntity<?> getPropertyDetails(@PathVariable String category, @PathVariable Integer listingId) {
        return ResponseEntity.ok(userService.getPropertyDetails(category, listingId));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/getAllProperties")
    public ResponseEntity<?> getAllProperties() {
        return ResponseEntity.ok(userService.getAllProperties());
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/getFilteredProperties")
    public ResponseEntity<?> getFilteredProperties(@RequestBody FilterRequest filters) {
        return ResponseEntity.ok(userService.getFilteredProperties(filters));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/getDetailedFilteredProperties")
    public ResponseEntity<?> getDetailedFilteredProperties(@RequestBody DetailedFilterRequest filters) {
        return ResponseEntity.ok(userService.getDetailedFilteredProperties(filters));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/filterByPreferenceAndLocation")
    public ResponseEntity<?> filterByPreferenceAndLocation(@RequestParam String preference, @RequestParam String state, @RequestParam String city, @RequestParam String locality) {
        return ResponseEntity.ok(userService.filterByPreferenceAndLocation(preference, state, city, locality));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/filterByPlotAndLocation")
    public ResponseEntity<?> filterByPlotAndLocation(@RequestParam String state, @RequestParam String city, @RequestParam String locality) {
        return ResponseEntity.ok(userService.filterByPlotAndLocation(state, city, locality));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/getCountByCity")
    public ResponseEntity<?> getCountByCity() {
        return ResponseEntity.ok(userService.getCountByCity());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/filterByCity/{city}")
    public ResponseEntity<?> filterByCity(@PathVariable String city) {
        return ResponseEntity.ok(userService.filterByCity(city));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/getVipFilterByPropertyType/{propertyType}")
    public ResponseEntity<?> getVipFilterByPropertyType(@PathVariable String propertyType) {
        return ResponseEntity.ok(userService.getVipFilterByPropertyType(propertyType));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/getVipFilterByPG")
    public ResponseEntity<?> getVipFilterByPG() {
        return ResponseEntity.ok(userService.getVipFilterByPG());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/getNewlyLaunchedProperties")
    public ResponseEntity<?> getNewlyLaunchedProperties() {
        return ResponseEntity.ok(userService.getNewlyLaunchedProperties());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getUserDashboardMetrics")
    public ResponseEntity<?> getUserDashboardMetrics(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer userId = cud.getUser().getUserId();
        return ResponseEntity.ok(userService.getUserDashboardMetrics(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getUserDetails")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer userId = cud.getUser().getUserId();
        return ResponseEntity.ok(userService.getUserDetails(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value="/updateUserDetails", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserDetails(@AuthenticationPrincipal CustomUserDetails cud, @RequestPart("userDetails") UserRequest userDetails, @RequestPart(value="profileImage", required = false) MultipartFile profileImage) throws IOException {
        Integer userId = cud.getUser().getUserId();
        return ResponseEntity.ok(userService.updateUserDetails(userId, userDetails, profileImage));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/updateUserPassword")
    public ResponseEntity<?> updateUserPassword(@AuthenticationPrincipal CustomUserDetails cud, @RequestBody PasswordUpdateRequest passwordRequest) {
        Integer userId = cud.getUser().getUserId();
        return ResponseEntity.ok(userService.updateUserPassword(userId,passwordRequest));
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addFeedbackFromUser")
    public ResponseEntity<?> addFeedbackFromUser(@AuthenticationPrincipal CustomUserDetails cud, @RequestBody FeedbackDetails feedbackRequest) {
        Integer userId = cud.getUser().getUserId();
        return ResponseEntity.ok(userService.addFeedbackFromUser(feedbackRequest,userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addHelpRequestFromUser")
    public ResponseEntity<?> addHelpRequestFromUser(@AuthenticationPrincipal CustomUserDetails cud, @RequestBody HelpDetails helpRequest) {
        Integer userId = cud.getUser().getUserId();
        return ResponseEntity.ok(userService.addHelpRequestFromUser(helpRequest,userId));
    }

    // Authenticated route (BUYER or AGENT), submit KYC
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/initiateKyc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> initiate(
            @RequestParam String address,                       // required
            @RequestParam(required = false) String reraNumber,  // optional
            @RequestPart(name = "profileImage", required = false) MultipartFile profileImage, // optional
            @RequestPart(name = "aadhar", required = true) MultipartFile aadhar,              // required
            @AuthenticationPrincipal CustomUserDetails cud
    ) throws IOException, MessagingException {
        userService.initiateKyc(cud.getUsername(), address, reraNumber, profileImage, aadhar);
        return ResponseEntity.ok(Map.of(
                "message", "KYC initiated and pending for admin approval",
                "status", "PENDING"
        ));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/kycStatus")
    public ResponseEntity<?> status(@AuthenticationPrincipal CustomUserDetails cud) {
        AgentResponse res = userService.getStatus(cud.getUsername());
        return ResponseEntity.ok(res);
    }

@PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updateKycDetails", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> updateKycDetails(
            @RequestParam String address,                       // required
            @RequestParam(required = false) String reraNumber,  // optional
            @RequestPart(name = "profileImage", required = false) MultipartFile profileImage, // optional
            @RequestPart(name = "aadhar", required = true) MultipartFile aadhar,              // required
            @AuthenticationPrincipal CustomUserDetails cud
    ) throws IOException {
        userService.updateKycDetails(cud.getUsername(), address, reraNumber, profileImage, aadhar);
        return ResponseEntity.ok(Map.of(
                "message", "KYC initiated and pending for admin approval",
                "status", "PENDING"
        ));
    }


}
