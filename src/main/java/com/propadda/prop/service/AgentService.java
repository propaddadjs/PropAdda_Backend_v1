package com.propadda.prop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.propadda.prop.dto.AgentResponse;
import com.propadda.prop.dto.AgentUpdateRequest;
import com.propadda.prop.dto.CommercialPropertyResponse;
import com.propadda.prop.dto.PasswordUpdateRequest;
import com.propadda.prop.dto.ResidentialPropertyResponse;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.exceptions.ResourceNotFoundException;
import com.propadda.prop.mappers.AgentMapper;
import com.propadda.prop.mappers.CommercialPropertyMapper;
import com.propadda.prop.mappers.ResidentialPropertyMapper;
import com.propadda.prop.model.CommercialPropertyDetails;
import com.propadda.prop.model.FeedbackDetails;
import com.propadda.prop.model.HelpDetails;
import com.propadda.prop.model.NotificationDetails;
import com.propadda.prop.model.ResidentialPropertyDetails;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.CommercialPropertyDetailsRepo;
import com.propadda.prop.repo.FeedbackDetailsRepo;
import com.propadda.prop.repo.HelpDetailsRepo;
import com.propadda.prop.repo.NotificationDetailsRepository;
import com.propadda.prop.repo.ResidentialPropertyDetailsRepo;
import com.propadda.prop.repo.UsersRepo;

import jakarta.transaction.Transactional;

@Service
public class AgentService {

    @Autowired
    private UsersRepo userRepo;

    @Autowired
    private CommercialPropertyDetailsRepo cRepo;

    @Autowired
    private ResidentialPropertyDetailsRepo rRepo;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @Autowired
    private FeedbackDetailsRepo feedbackRepo;

    @Autowired
    private HelpDetailsRepo helpRepo;

    @Autowired
    private NotificationDetailsRepository notifRepo;

    @Autowired
    private GcsService gcsService;

    public Map<String,List<?>> getAllPropertiesByAgent(Integer agentId) {
        Users owner = userRepo.findById(agentId).isPresent() ? userRepo.findById(agentId).get() : null;
        List<CommercialPropertyResponse> cres = new ArrayList<>();
         List<ResidentialPropertyResponse> rres = new ArrayList<>();
        if(owner!=null){
            List<CommercialPropertyDetails> clist = cRepo.findByCommercialOwnerAndAdminApprovedAndExpiredAndSold(owner,"Approved",false,false);
            if(!clist.isEmpty()){
                cres = CommercialPropertyMapper.toDtoList(clist);
            }
            List<ResidentialPropertyDetails> rlist = rRepo.findByResidentialOwnerAndAdminApprovedAndExpiredAndSold(owner,"Approved",false,false);
            if(!rlist.isEmpty()){
                rres = ResidentialPropertyMapper.toDtoList(rlist);
            }
        }
        Map<String,List<?>> res = new HashMap<>();
        res.put("Commercial",cres);
        res.put("Residential", rres);
        return res;
    }

    public Map<String,List<?>> pendingApprovalPropertiesForAgent(Integer agentId) {
        Users owner = userRepo.findById(agentId).isPresent() ? userRepo.findById(agentId).get() : null;
        List<CommercialPropertyResponse> cres = new ArrayList<>();
        List<ResidentialPropertyResponse> rres = new ArrayList<>();
        if(owner!=null){
            List<CommercialPropertyDetails> clist = cRepo.findByCommercialOwnerAndAdminApproved(owner,"Pending");
            if(!clist.isEmpty()){
                cres = CommercialPropertyMapper.toDtoList(clist);
            }
            List<ResidentialPropertyDetails> rlist = rRepo.findByResidentialOwnerAndAdminApproved(owner,"Pending");
            if(!rlist.isEmpty()){
                rres = ResidentialPropertyMapper.toDtoList(rlist);
            }
        }
        Map<String,List<?>> res = new HashMap<>();
        res.put("Commercial",cres);
        res.put("Residential", rres);
        return res;
    }

    public Map<String,List<?>> getExpiredProperties(Integer agentId) {
        Users owner = userRepo.findById(agentId).isPresent() ? userRepo.findById(agentId).get() : null;
        List<CommercialPropertyResponse> cres = new ArrayList<>();
         List<ResidentialPropertyResponse> rres = new ArrayList<>();
        if(owner!=null){
            List<CommercialPropertyDetails> clist = cRepo.findByCommercialOwnerAndExpired(owner,true);
            if(!clist.isEmpty()){
                cres = CommercialPropertyMapper.toDtoList(clist);
            }
            List<ResidentialPropertyDetails> rlist = rRepo.findByResidentialOwnerAndExpired(owner,true);
            if(!rlist.isEmpty()){
                rres = ResidentialPropertyMapper.toDtoList(rlist);
            }
        }
        System.out.println("Com: exp: "+cres);
        System.out.println("Res: exp: "+rres);
        Map<String,List<?>> res = new HashMap<>();
        res.put("Commercial",cres);
        res.put("Residential", rres);
        return res;
    }

    public Map<String,List<?>> getSoldProperties(Integer agentId) {
        Users owner = userRepo.findById(agentId).isPresent() ? userRepo.findById(agentId).get() : null;
        List<CommercialPropertyResponse> cres = new ArrayList<>();
         List<ResidentialPropertyResponse> rres = new ArrayList<>();
        if(owner!=null){
            List<CommercialPropertyDetails> clist = cRepo.findByCommercialOwnerAndSold(owner,true);
            if(!clist.isEmpty()){
                cres = CommercialPropertyMapper.toDtoList(clist);
            }
            List<ResidentialPropertyDetails> rlist = rRepo.findByResidentialOwnerAndSold(owner,true);
            if(!rlist.isEmpty()){
                rres = ResidentialPropertyMapper.toDtoList(rlist);
            }
        }
        Map<String,List<?>> res = new HashMap<>();
        res.put("Commercial",cres);
        res.put("Residential", rres);
        return res;
    }

    public AgentResponse getAgentDetails(Integer agentId) {
        // Users agent = userRepo.findById(agentId).isPresent() ? userRepo.findById(agentId).get() : null;
        AgentResponse agent = new AgentResponse();
        if(userRepo.findById(agentId).isPresent()){
            agent = AgentMapper.toDto(userRepo.findById(agentId).get());
        } 
        return agent;
    }

    @Transactional
    public AgentResponse updateAgentDetails(AgentUpdateRequest updatedAgentDetails, MultipartFile profileImage, Integer agentId)  throws java.io.IOException {
        Users agent = userRepo.findById(agentId).isPresent() ? userRepo.findById(agentId).get() : null;
        if(agent!=null){
        if (updatedAgentDetails.getFirstName() != null) {
            agent.setFirstName(updatedAgentDetails.getFirstName());
        }
        if (updatedAgentDetails.getLastName() != null) {
            agent.setLastName(updatedAgentDetails.getLastName());
        }
        if (updatedAgentDetails.getEmail() != null) {
            agent.setEmail(updatedAgentDetails.getEmail());
        }
        if (updatedAgentDetails.getPhoneNumber() != null) {
            agent.setPhoneNumber(updatedAgentDetails.getPhoneNumber());
        }
        if (updatedAgentDetails.getState() != null) {
            agent.setState(updatedAgentDetails.getState());
        }
        if (updatedAgentDetails.getCity() != null) {
            agent.setCity(updatedAgentDetails.getCity());
        }
        if (!profileImage.isEmpty()) {
            agent.setProfileImageUrl(gcsService.uploadKYCFiles(profileImage, "profileImage"));
        }
        if (updatedAgentDetails.getAadharUrl() != null) {
            agent.setAadharUrl(updatedAgentDetails.getAadharUrl());
        }
        if (updatedAgentDetails.getAddress() != null) {
            agent.setAddress(updatedAgentDetails.getAddress());
        }
    }   
        Users updatedUser = userRepo.save(agent);
        return AgentMapper.toDto(updatedUser);
    }

    public Map<String, Integer> getAgentDashboardMetrics(Integer agentId) {
        Map<String, Integer> metrics = new HashMap<>();
        metrics.put("totalPropertiesListed",cRepo.findByCommercialOwner(userRepo.findById(agentId).get()).size() +  rRepo.findByResidentialOwner(userRepo.findById(agentId).get()).size());
        metrics.put("activeProperties",getAllPropertiesByAgent(agentId).get("Commercial").size() +  getAllPropertiesByAgent(agentId).get("Residential").size());
        metrics.put("totalPropertiesPendingApproval",pendingApprovalPropertiesForAgent(agentId).get("Commercial").size() +  pendingApprovalPropertiesForAgent(agentId).get("Residential").size());
        metrics.put("totalPropertiesSold",getSoldProperties(agentId).get("Commercial").size() +  getSoldProperties(agentId).get("Residential").size());
        return metrics; 
    }

    @Transactional
    public AgentResponse updateAgentPassword(Integer agentId, PasswordUpdateRequest passwordRequest) {
        Users agent = userRepo.findById(agentId).isPresent() ? userRepo.findById(agentId).get() : null;
        if(agent!=null){
            if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New passwords do not match.");
            }
             if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), agent.getPassword())) {
            throw new BadCredentialsException("Invalid current password provided.");
            }
            String newHashedPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
            agent.setPassword(newHashedPassword);
            Users updatedUser = userRepo.save(agent);
            return AgentMapper.toDto(updatedUser);
        } else {
            return null;
        }
    }

    @Transactional
    public FeedbackDetails addFeedbackFromAgent(FeedbackDetails feedbackRequest, Integer agentId) {
        Users agent = userRepo.findById(agentId).isPresent() ? userRepo.findById(agentId).get() : null;
        if(agent!=null){
        feedbackRequest.setFeedbackUser(userRepo.findById(agentId).get());
        return feedbackRepo.save(feedbackRequest);
        }
        else {
            return null;
        }
    }

    @Transactional
    public HelpDetails addHelpRequestFromAgent(HelpDetails helpRequest, Integer agentId) {
        Users agent = userRepo.findById(agentId).isPresent() ? userRepo.findById(agentId).get() : null;
        if(agent!=null){
        helpRequest.setHelpUser(userRepo.findById(agentId).get());
        return helpRepo.save(helpRequest);
        }
        else {
            return null;
        }
    }

    public List<NotificationDetails> allNotificationsForAgent(Integer agentId) {
        return notifRepo.findByNotificationReceiverId(agentId);
    }

    public Integer getUnreadNotificationCountForAgent(Integer agentId) {
        return notifRepo.countUnreadNotifications(Role.AGENT,agentId);
    }

    @Transactional
    public Object markNotificationViewedForAgent(Integer agentId, Integer notificationId) {
        if(notifRepo.findById(notificationId).isPresent()){
            NotificationDetails n = notifRepo.findById(notificationId).get();
            if(n.getNotificationReceiverId().equals(agentId)){
                n.setNotificationViewed(true);
                notifRepo.save(n);
            }
        }
        return notifRepo.findByNotificationReceiverId(agentId);
    }

    @Transactional
    public Object markAllNotificationViewedForAgent(Integer agentId) {
        List<NotificationDetails> notifications = notifRepo.findByNotificationReceiverId(agentId);
        if(!notifications.isEmpty()){
            for(NotificationDetails n : notifications){
                n.setNotificationViewed(true);
                notifRepo.save(n);
            }
        }
        return notifRepo.findByNotificationReceiverId(agentId);
    }

    public Object propertyByIdForAgent(Integer agentId, String category, Integer listingId) {
        if ("Commercial".equalsIgnoreCase(category)) {
            return cRepo.findByListingIdAndCommercialOwner(listingId,userRepo.findById(agentId).get())
                          .map(CommercialPropertyMapper::toDto) 
                          .orElseThrow(() -> new ResourceNotFoundException("Commercial Property not found with ID: " + listingId));
        }
        
        if ("Residential".equalsIgnoreCase(category)) {
            return rRepo.findByListingIdAndResidentialOwner(listingId,userRepo.findById(agentId).get())
                          .map(ResidentialPropertyMapper::toDto)
                          .orElseThrow(() -> new ResourceNotFoundException("Residential Property not found with ID: " + listingId));
        }
        throw new IllegalArgumentException("Invalid property category: " + category); 
    }

    @Transactional
    public Object markPropertyAsSoldForAgent(Integer agentId, String category, Integer listingId) {
         if ("Residential".equalsIgnoreCase(category)) {
                Optional<ResidentialPropertyDetails> opt = rRepo.findByListingIdAndResidentialOwner(listingId,userRepo.findById(agentId).get());
                if (opt.isPresent()) {
                    ResidentialPropertyDetails p = opt.get();
                    p.setSold(true);
                    rRepo.save(p);
                    return true;
                }
                return false;
            } else if ("Commercial".equalsIgnoreCase(category)) {
                Optional<CommercialPropertyDetails> opt = cRepo.findByListingIdAndCommercialOwner(listingId,userRepo.findById(agentId).get());
                if (opt.isPresent()) {
                    CommercialPropertyDetails p = opt.get();
                    p.setSold(true);
                    cRepo.save(p);
                    return true;
                }
                return false;
            }
            return false;
    }

    @Transactional
    public Object renewProperty(Integer listingId, String category, Integer agentId) {
        if(category.equalsIgnoreCase("Commercial")){
            CommercialPropertyDetails cpd = cRepo.findByListingIdAndCommercialOwner_UserId(listingId, agentId).orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));
            cpd.setExpired(false);
            cpd.setAdminApproved("Pending");
            cRepo.save(cpd);
            // NotificationDetails notification = new NotificationDetails();
            // String message = "Your listing titled- "+cpd.getTitle()+" has been renewed.";
            // notification.setNotificationType(NotificationType.RenewedListing);
            // notification.setNotificationMessage(message);
            // notification.setNotificationReceiverId(cpd.getCommercialOwner().getUserId());
            // notification.setNotificationReceiverRole(Role.AGENT);
            // notification.setNotificationSenderId(1);
            // notification.setNotificationSenderRole(Role.ADMIN);
            // notificationRepo.save(notification);
            return cpd;
        }
        else
        if(category.equalsIgnoreCase("Residential")){
            ResidentialPropertyDetails rpd = rRepo.findByListingIdAndResidentialOwner_UserId(listingId, agentId)
            .orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));
            rpd.setExpired(false);
            rpd.setAdminApproved("Pending");
            rRepo.save(rpd);
            // NotificationDetails notification = new NotificationDetails();
            // String message = "Your listing titled- "+rpd.getTitle()+" has been renewed.";
            // notification.setNotificationType(NotificationType.RenewedListing);
            // notification.setNotificationMessage(message);
            // notification.setNotificationReceiverId(rpd.getResidentialOwner().getUserId());
            // notification.setNotificationReceiverRole(Role.AGENT);
            // notification.setNotificationSenderId(1);
            // notification.setNotificationSenderRole(Role.ADMIN);
            // notificationRepo.save(notification);
            return rpd;
        }
        else {
            return null;
        }
    }
    
}
