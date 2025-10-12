package com.propadda.prop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.propadda.prop.dto.AgentUpdateRequest;
import com.propadda.prop.dto.MediaProductionGraphicsRequest;
import com.propadda.prop.dto.MediaProductionPhotoshootRequest;
import com.propadda.prop.dto.PasswordUpdateRequest;
import com.propadda.prop.model.FeedbackDetails;
import com.propadda.prop.model.HelpDetails;
import com.propadda.prop.security.CustomUserDetails;
import com.propadda.prop.service.AgentService;


@RestController
@PreAuthorize("hasAnyRole('AGENT','ADMIN')")
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;
    
    @GetMapping("/allPropertiesByAgent")
    public ResponseEntity<?> getAllPropertiesByAgent(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.getAllPropertiesByAgent(agentId)!=null)
        return ResponseEntity.ok(agentService.getAllPropertiesByAgent(agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/propertyByIdForAgent/{category}/{listingId}")
    public ResponseEntity<?> propertyByIdForAgent(@AuthenticationPrincipal CustomUserDetails cud, @PathVariable String category, @PathVariable Integer listingId) {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.propertyByIdForAgent(agentId,category,listingId)!=null)
        return ResponseEntity.ok(agentService.propertyByIdForAgent(agentId,category,listingId));
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/pendingApprovalPropertiesForAgent")
    public ResponseEntity<?> pendingApprovalPropertiesForAgent(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.pendingApprovalPropertiesForAgent(agentId)!=null)
        return ResponseEntity.ok(agentService.pendingApprovalPropertiesForAgent(agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/expiredPropertiesByAgent")
    public ResponseEntity<?> getExpiredProperties(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.getExpiredProperties(agentId)!=null)
        return ResponseEntity.ok(agentService.getExpiredProperties(agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/soldPropertiesByAgent")
    public ResponseEntity<?> getSoldProperties(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.getSoldProperties(agentId)!=null)
        return ResponseEntity.ok(agentService.getSoldProperties(agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getAgentDetails")
    public ResponseEntity<?> getAgentDetails(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.getAgentDetails(agentId)!=null)
        return ResponseEntity.ok(agentService.getAgentDetails(agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/updateAgentDetails")
    public ResponseEntity<?> updateAgentDetails(@RequestPart("updatedAgentDetails") AgentUpdateRequest updatedAgentDetails, @RequestPart(value="profileImage", required=false) MultipartFile profileImage, @AuthenticationPrincipal CustomUserDetails cud)  throws IOException {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.updateAgentDetails(updatedAgentDetails,profileImage,agentId)!=null)
        return ResponseEntity.ok(agentService.updateAgentDetails(updatedAgentDetails,profileImage,agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getAgentDashboardMetrics")
    public ResponseEntity<?> getAgentDashboardMetrics(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.getAgentDashboardMetrics(agentId)!=null)
        return ResponseEntity.ok(agentService.getAgentDashboardMetrics(agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/updateAgentPassword")
    public ResponseEntity<?> updateAgentPassword(@AuthenticationPrincipal CustomUserDetails cud, @RequestBody PasswordUpdateRequest passwordRequest) {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.updateAgentPassword(agentId,passwordRequest)!=null)
        return ResponseEntity.ok(agentService.updateAgentPassword(agentId,passwordRequest));
        else
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/addFeedbackFromAgent")
    public ResponseEntity<?> addFeedbackFromAgent(@RequestBody FeedbackDetails feedbackRequest, @AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.addFeedbackFromAgent(feedbackRequest,agentId)!=null)
        return ResponseEntity.ok(agentService.addFeedbackFromAgent(feedbackRequest,agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/addHelpRequestFromAgent")
    public ResponseEntity<?> addHelpRequestFromAgent(@RequestBody HelpDetails helpRequest, @AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        if(agentService.addHelpRequestFromAgent(helpRequest,agentId)!=null)
        return ResponseEntity.ok(agentService.addHelpRequestFromAgent(helpRequest,agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/allNotificationsForAgent")
    public ResponseEntity<?> allNotificationsForAgent(@AuthenticationPrincipal CustomUserDetails cud){
        Integer agentId = cud.getUser().getUserId();
        if(agentService.allNotificationsForAgent(agentId)!=null)
        return ResponseEntity.ok(agentService.allNotificationsForAgent(agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/newNotificationsForAgent")
    public ResponseEntity<?> newNotificationsForAgent(@AuthenticationPrincipal CustomUserDetails cud){
        Integer agentId = cud.getUser().getUserId();
        if(agentService.allNotificationsForAgent(agentId)!=null)
        return ResponseEntity.ok(agentService.allNotificationsForAgent(agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getUnreadNotificationCountForAgent")
    public ResponseEntity<?> getUnreadNotificationCountForAgent(@AuthenticationPrincipal CustomUserDetails cud){
        Integer agentId = cud.getUser().getUserId();
        if(agentService.getUnreadNotificationCountForAgent(agentId)!=null)
        return ResponseEntity.ok(agentService.getUnreadNotificationCountForAgent(agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/markNotificationViewedForAgent/{notificationId}")
    public ResponseEntity<?> markNotificationViewedForAgent(@AuthenticationPrincipal CustomUserDetails cud, @PathVariable Integer notificationId){
        Integer agentId = cud.getUser().getUserId();
        if(agentService.markNotificationViewedForAgent(agentId, notificationId)!=null)
        return ResponseEntity.ok(agentService.markNotificationViewedForAgent(agentId, notificationId));
        else
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/markAllNotificationViewedForAgent")
    public ResponseEntity<?> markAllNotificationViewedForAgent(@AuthenticationPrincipal CustomUserDetails cud){
        Integer agentId = cud.getUser().getUserId();
        if(agentService.markAllNotificationViewedForAgent(agentId)!=null)
        return ResponseEntity.ok(agentService.markAllNotificationViewedForAgent(agentId));
        else
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/markPropertyAsSoldForAgent/{category}/{listingId}")
    public ResponseEntity<?> markPropertyAsSoldForAgent(@AuthenticationPrincipal CustomUserDetails cud, @PathVariable String category, @PathVariable Integer listingId){
        Integer agentId = cud.getUser().getUserId();
        if(agentService.markPropertyAsSoldForAgent(agentId,category,listingId)!=null)
        return ResponseEntity.ok(agentService.markPropertyAsSoldForAgent(agentId,category,listingId));
        else
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/renewProperty/{category}/{listingId}")
    public ResponseEntity<?> renewProperty(@PathVariable Integer listingId, @PathVariable String category, @AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        Object propObject = agentService.renewProperty(listingId, category,agentId);
        return ResponseEntity.ok(propObject);
    }

    @GetMapping("/getPropertiesToRequestGraphicShoot")
    public ResponseEntity<?> getPropertiesToRequestGraphicShoot(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        return ResponseEntity.ok(agentService.getPropertiesToRequestGraphicShoot(agentId));
    }

    @GetMapping("/getPropertiesToRequestPhotoshoot")
    public ResponseEntity<?> getPropertiesToRequestPhotoshoot(@AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        return ResponseEntity.ok(agentService.getPropertiesToRequestPhotoshoot(agentId));
    }

    @PostMapping("/addMediaProductionGraphicsRequestFromAgent")
    public ResponseEntity<?> addMediaProductionGraphicsRequestFromAgent(@RequestBody List<MediaProductionGraphicsRequest> reqList, @AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        return ResponseEntity.ok(agentService.addMediaProductionGraphicsRequestFromAgent(reqList,agentId));
    }

    @PostMapping("/addMediaProductionPhotoshootRequestFromAgent")
    public ResponseEntity<?> addMediaProductionPhotoshootRequestFromAgent(@RequestBody List<MediaProductionPhotoshootRequest> reqList, @AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        return ResponseEntity.ok(agentService.addMediaProductionPhotoshootRequestFromAgent(reqList,agentId));
    }

}
