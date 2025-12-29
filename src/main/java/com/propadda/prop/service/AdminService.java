// Author-Hemant Arora
package com.propadda.prop.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.propadda.prop.dto.AgentResponse;
import com.propadda.prop.dto.AllPropertyViewResponse;
import com.propadda.prop.dto.DetailedFilterRequest;
import com.propadda.prop.dto.LeadsResponse;
import com.propadda.prop.dto.MediaProductionResponse;
import com.propadda.prop.dto.UserResponse;
import com.propadda.prop.enumerations.EnquiryStatus;
import com.propadda.prop.enumerations.Kyc;
import com.propadda.prop.enumerations.NotificationType;
import com.propadda.prop.enumerations.RejectionType;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.exceptions.ResourceNotFoundException;
import com.propadda.prop.mappers.AgentMapper;
import com.propadda.prop.mappers.AllPropertyViewMapper;
import com.propadda.prop.mappers.CommercialPropertyMapper;
import com.propadda.prop.mappers.ResidentialPropertyMapper;
import com.propadda.prop.mappers.UserMapper;
import com.propadda.prop.model.AllPropertyView;
import com.propadda.prop.model.AllPropertyViewFilter;
import com.propadda.prop.model.CommercialPropertyDetails;
import com.propadda.prop.model.EnquiredListingsDetails;
import com.propadda.prop.model.MediaProduction;
import com.propadda.prop.model.NotificationDetails;
import com.propadda.prop.model.RejectionDetails;
import com.propadda.prop.model.ResidentialPropertyDetails;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.AllPropertyViewFilterRepository;
import com.propadda.prop.repo.AllPropertyViewRepository;
import com.propadda.prop.repo.CommercialPropertyDetailsRepo;
import com.propadda.prop.repo.EnquiredListingsDetailsRepo;
import com.propadda.prop.repo.MediaProductionRepo;
import com.propadda.prop.repo.NotificationDetailsRepository;
import com.propadda.prop.repo.RejectionDetailsRepository;
import com.propadda.prop.repo.ResidentialPropertyDetailsRepo;
import com.propadda.prop.repo.UsersRepo;

import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class AdminService {

    @Autowired
    private CommercialPropertyDetailsRepo cpdRepo;

    @Autowired
    private ResidentialPropertyDetailsRepo rpdRepo;

    @Autowired
    private RejectionDetailsRepository rejectionRepo;

    @Autowired
    private UsersRepo userRepo;

    @Autowired
    NotificationDetailsRepository notificationRepo;

    @Autowired
    EnquiredListingsDetailsRepo enqRepo;

    @Autowired
    AllPropertyViewRepository allPropertyViewRepo;

    @Autowired
    private AllPropertyViewFilterRepository allPropertyViewFilterRepo;

    @Autowired
    MediaProductionRepo mpRepo;

    @Autowired
    private GcsService gcsService;

    @Autowired
    private MailSenderService mailService;

    @Autowired
    private AllPropertyViewMapper allPropertyViewMapper;

    public Page<AllPropertyViewResponse> getAllProperties(int page, int size) {

        Pageable pageable = PageRequest.of(
            page, size, Sort.by(Sort.Direction.DESC, "approvedAt")
        );

        Page<AllPropertyView> pageResult = allPropertyViewRepo
            .findByAdminApprovedAndExpiredAndSold(
                "Approved", false, false, pageable
            );
        List<AllPropertyViewResponse> dtoList =
                allPropertyViewMapper.toDtoList(pageResult.getContent());
        return new PageImpl<>(
                dtoList,
                pageable,
                pageResult.getTotalElements()
        );
    }

    public Page<AllPropertyViewResponse> getExpiredProperties(int page, int size){
        Pageable pageable = PageRequest.of(
            page, size, Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<AllPropertyView> pageResult = allPropertyViewRepo
            .findByAdminApprovedAndExpiredAndSold(
                "Pending", true, false, pageable
            );
        List<AllPropertyViewResponse> dtoList =
                allPropertyViewMapper.toDtoList(pageResult.getContent());
        return new PageImpl<>(
                dtoList,
                pageable,
                pageResult.getTotalElements()
        );
    }

    public Page<AllPropertyViewResponse> getVipProperties(int page, int size){
        Pageable pageable = PageRequest.of(
            page, size, Sort.by(Sort.Direction.DESC, "approvedAt")
        );

        Page<AllPropertyView> pageResult = allPropertyViewRepo
            .findByAdminApprovedAndExpiredAndSoldAndVip(
                "Approved", false, false, true, pageable
            );
        List<AllPropertyViewResponse> dtoList =
                allPropertyViewMapper.toDtoList(pageResult.getContent());
        return new PageImpl<>(
                dtoList,
                pageable,
                pageResult.getTotalElements()
        );
    }

    public Page<AllPropertyViewResponse> getPendingProperties(int page, int size){
        Pageable pageable = PageRequest.of(
            page, size, Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<AllPropertyView> pageResult = allPropertyViewRepo
            .findByAdminApprovedAndExpiredAndSold(
                "Pending", false, false, pageable
            );
        List<AllPropertyViewResponse> dtoList =
                allPropertyViewMapper.toDtoList(pageResult.getContent());
        return new PageImpl<>(
                dtoList,
                pageable,
                pageResult.getTotalElements()
        );
    }

    public Page<AllPropertyViewResponse> getSoldProperties(int page, int size){
        Pageable pageable = PageRequest.of(
            page, size, Sort.by(Sort.Direction.DESC, "approvedAt")
        );

        Page<AllPropertyView> pageResult = allPropertyViewRepo
            .findByAdminApprovedAndExpiredAndSold(
                "Approved", false, true, pageable
            );
        List<AllPropertyViewResponse> dtoList =
                allPropertyViewMapper.toDtoList(pageResult.getContent());
        return new PageImpl<>(
                dtoList,
                pageable,
                pageResult.getTotalElements()
        );
    }

    public Page<AllPropertyViewResponse> allFilteredPropertiesPaged(DetailedFilterRequest filters, int page, int size){
        Sort sort = Sort.by("approvedAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AllPropertyViewFilter> result =
            allPropertyViewFilterRepo.findAll(this.detailedFilter(filters,"Approved", false,false,null), pageable);

        return result.map(allPropertyViewMapper::toDtoFiltered);
    }

    public Page<AllPropertyViewResponse> pendingFilteredPropertiesPaged(DetailedFilterRequest filters, int page, int size){
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AllPropertyViewFilter> result =
            allPropertyViewFilterRepo.findAll(this.detailedFilter(filters,"Pending", false,false,null), pageable);

        return result.map(allPropertyViewMapper::toDtoFiltered);
    }

    public Page<AllPropertyViewResponse> expiredFilteredPropertiesPaged(DetailedFilterRequest filters, int page, int size){
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AllPropertyViewFilter> result =
            allPropertyViewFilterRepo.findAll(this.detailedFilter(filters,"Pending", true,false,null), pageable);

        return result.map(allPropertyViewMapper::toDtoFiltered);
    }

    public Page<AllPropertyViewResponse> vipFilteredPropertiesPaged(DetailedFilterRequest filters, int page, int size){
        Sort sort = Sort.by("approvedAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AllPropertyViewFilter> result =
            allPropertyViewFilterRepo.findAll(this.detailedFilter(filters,"Approved", false,false,true), pageable);

        return result.map(allPropertyViewMapper::toDtoFiltered);
    }

    public Page<AllPropertyViewResponse> soldFilteredPropertiesPaged(DetailedFilterRequest filters, int page, int size){
        Sort sort = Sort.by("approvedAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AllPropertyViewFilter> result =
            allPropertyViewFilterRepo.findAll(this.detailedFilter(filters,"Approved", false,true,null), pageable);

        return result.map(allPropertyViewMapper::toDtoFiltered);
    }

    public Object getPropertyById(Integer listingId, String category){
        if ("Commercial".equalsIgnoreCase(category)) {
            return cpdRepo.findById(listingId)
                          .map(CommercialPropertyMapper::toDto) 
                          .orElseThrow(() -> new ResourceNotFoundException("Commercial Property not found with ID: " + listingId));
        }
        
        if ("Residential".equalsIgnoreCase(category)) {
            return rpdRepo.findById(listingId)
                          .map(ResidentialPropertyMapper::toDto)
                          .orElseThrow(() -> new ResourceNotFoundException("Residential Property not found with ID: " + listingId));
        }
        throw new IllegalArgumentException("Invalid property category: " + category);       
    }

    @Transactional
    public boolean approveProperty(Integer listingId, String category) throws MessagingException {
        if ("Residential".equalsIgnoreCase(category)) {
            Optional<ResidentialPropertyDetails> opt = rpdRepo.findById(listingId);
            if (opt.isPresent()) {
                ResidentialPropertyDetails p = opt.get();
                p.setAdminApproved("Approved");
                p.setApprovedAt(OffsetDateTime.now());
                rpdRepo.save(p);

                //notification flow
                NotificationDetails notification = new NotificationDetails();
                String message = "Great news! Your listing titled "+p.getTitle()+" is live.";
                notification.setNotificationType(NotificationType.ListingApproval);
                notification.setNotificationMessage(message);
                notification.setNotificationReceiverId(p.getResidentialOwner().getUserId());
                notification.setNotificationReceiverRole(Role.AGENT);
                notification.setNotificationSenderId(1);
                notification.setNotificationSenderRole(Role.ADMIN);
                notificationRepo.save(notification);

                //email flow
                String to = p.getResidentialOwner().getEmail();
                String subject = "Approved- "+p.getTitle();
                String body = "Great news! Your listing titled "+p.getTitle()+" is live.";
                mailService.send(to, subject, body);
                return true;
            }
            return false;
        } else if ("Commercial".equalsIgnoreCase(category)) {
            Optional<CommercialPropertyDetails> opt = cpdRepo.findById(listingId);
            if (opt.isPresent()) {
                CommercialPropertyDetails p = opt.get();
                p.setAdminApproved("Approved");
                p.setApprovedAt(OffsetDateTime.now());
                cpdRepo.save(p);

                //notification flow
                NotificationDetails notification = new NotificationDetails();
                String message = "Great news! Your listing titled "+p.getTitle()+" is live.";
                notification.setNotificationType(NotificationType.ListingApproval);
                notification.setNotificationMessage(message);
                notification.setNotificationReceiverId(p.getCommercialOwner().getUserId());
                notification.setNotificationReceiverRole(Role.AGENT);
                notification.setNotificationSenderId(1);
                notification.setNotificationSenderRole(Role.ADMIN);
                notificationRepo.save(notification);

                //email flow
                String to = p.getCommercialOwner().getEmail();
                String subject = "Approved- "+p.getTitle();
                String body = "Great news! Your listing titled "+p.getTitle()+" is live.";
                mailService.send(to, subject, body);

                return true;
            }
            return false;
        }
        return false;
    }

    @Transactional
    public boolean markPropertyAsSold(Integer listingId, String category) {
        if ("Residential".equalsIgnoreCase(category)) {
            Optional<ResidentialPropertyDetails> opt = rpdRepo.findById(listingId);
            if (opt.isPresent()) {
                ResidentialPropertyDetails p = opt.get();
                p.setSold(true);
                rpdRepo.save(p);
                return true;
            }
            return false;
        } else if ("Commercial".equalsIgnoreCase(category)) {
            Optional<CommercialPropertyDetails> opt = cpdRepo.findById(listingId);
            if (opt.isPresent()) {
                CommercialPropertyDetails p = opt.get();
                p.setSold(true);
                cpdRepo.save(p);
                return true;
            }
            return false;
        }
        return false;
    }

    @Transactional
    public boolean rejectProperty(Integer listingId, String category, String reason) throws MessagingException {
        if ("Residential".equalsIgnoreCase(category)) {
            Optional<ResidentialPropertyDetails> opt = rpdRepo.findById(listingId);
            if (opt.isPresent()) {
                ResidentialPropertyDetails p = opt.get();
                p.setAdminApproved("Rejected");
                rpdRepo.save(p);

                // persist rejection reason
                RejectionDetails rejection = new RejectionDetails();
                rejection.setRejectionType(RejectionType.RESIDENTIAL_PROPERTY);
                rejection.setRejectionReason(reason);
                rejection.setAgentId(p.getResidentialOwner().getUserId());
                rejection.setListingId(listingId);

                //notification flow
                NotificationDetails notification = new NotificationDetails();
                String message = "Your listing titled- "+p.getTitle()+" was not Approved. Reason: "+reason+". Please resubmit.";
                notification.setNotificationType(NotificationType.ListingRejection);
                notification.setNotificationMessage(message);
                notification.setNotificationReceiverId(p.getResidentialOwner().getUserId());
                notification.setNotificationReceiverRole(Role.AGENT);
                notification.setNotificationSenderId(1);
                notification.setNotificationSenderRole(Role.ADMIN);
                notificationRepo.save(notification);

                rejection.setAgentNotified(true);
                rejection.setAgentEmailed(false);
                rejectionRepo.save(rejection);

                //email flow
                String to = p.getResidentialOwner().getEmail();
                String subject = "Rejected- "+p.getTitle();
                String body = "Your listing titled- "+p.getTitle()+" was not Approved. Reason: "+reason+". Please resubmit.";
                mailService.send(to, subject, body);

                return true;
            }
            return false;
        } else if ("Commercial".equalsIgnoreCase(category)) {
            Optional<CommercialPropertyDetails> opt = cpdRepo.findById(listingId);
            if (opt.isPresent()) {
                CommercialPropertyDetails p = opt.get();
                p.setAdminApproved("Rejected");
                cpdRepo.save(p);
                RejectionDetails rejection = new RejectionDetails();
                rejection.setRejectionType(RejectionType.COMMERCIAL_PROPERTY);
                rejection.setRejectionReason(reason);
                rejection.setAgentId(p.getCommercialOwner().getUserId());
                rejection.setListingId(listingId);

                //notification flow
                NotificationDetails notification = new NotificationDetails();
                String message = "Your listing titled- "+p.getTitle()+" was not Approved. Reason: "+reason+". Please resubmit.";
                notification.setNotificationType(NotificationType.ListingRejection);
                notification.setNotificationMessage(message);
                notification.setNotificationReceiverId(p.getCommercialOwner().getUserId());
                notification.setNotificationReceiverRole(Role.AGENT);
                notification.setNotificationSenderId(1);
                notification.setNotificationSenderRole(Role.ADMIN);
                notificationRepo.save(notification);

                rejection.setAgentNotified(true);
                rejection.setAgentEmailed(false);
                rejectionRepo.save(rejection);

                //email flow
                String to = p.getCommercialOwner().getEmail();
                String subject = "Rejected- "+p.getTitle();
                String body = "Your listing titled- "+p.getTitle()+" was not Approved. Reason: "+reason+". Please resubmit.";
                mailService.send(to, subject, body);

                return true;
            }
            return false;
        }
        return false;
    }

    @Transactional
    public Object toggleExpired(Integer listingId, String category) {
        if(category.equalsIgnoreCase("Commercial") && cpdRepo.findById(listingId).isPresent()){
            CommercialPropertyDetails cpd = cpdRepo.findById(listingId).get();
            cpd.setExpired(!cpd.isExpired());
            cpdRepo.save(cpd);
            return cpd;
        }
        else
        if(category.equalsIgnoreCase("Residential") && rpdRepo.findById(listingId).isPresent()){
            ResidentialPropertyDetails rpd = rpdRepo.findById(listingId).get();
            rpd.setExpired(!rpd.isExpired());
            rpdRepo.save(rpd);
            return rpd;
        }
        else {
            return null;
        }
    }

    @Transactional
    public Object renewProperty(Integer listingId, String category) throws MessagingException {
        if(category.equalsIgnoreCase("Commercial") && cpdRepo.findById(listingId).isPresent()){
            CommercialPropertyDetails cpd = cpdRepo.findById(listingId).get();
            cpd.setExpired(false);
            cpd.setAdminApproved("Pending");
            cpdRepo.save(cpd);
            //notification flow
            NotificationDetails notification = new NotificationDetails();
            String message = "Your listing titled- "+cpd.getTitle()+" has been renewed.";
            notification.setNotificationType(NotificationType.RenewedListing);
            notification.setNotificationMessage(message);
            notification.setNotificationReceiverId(cpd.getCommercialOwner().getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);

            //email flow
            String to = cpd.getCommercialOwner().getEmail();
            String subject = "Renewed- "+cpd.getTitle();
            String body = "Your listing titled- "+cpd.getTitle()+" has been renewed.";
            mailService.send(to, subject, body);

            return cpd;
        }
        else
        if(category.equalsIgnoreCase("Residential") && rpdRepo.findById(listingId).isPresent()){
            ResidentialPropertyDetails rpd = rpdRepo.findById(listingId).get();
            rpd.setExpired(false);
            rpd.setAdminApproved("Pending");
            rpdRepo.save(rpd);
            NotificationDetails notification = new NotificationDetails();
            String message = "Your listing titled- "+rpd.getTitle()+" has been renewed.";
            notification.setNotificationType(NotificationType.RenewedListing);
            notification.setNotificationMessage(message);
            notification.setNotificationReceiverId(rpd.getResidentialOwner().getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);

            //email flow
            String to = rpd.getResidentialOwner().getEmail();
            String subject = "Renewed- "+rpd.getTitle();
            String body = "Your listing titled- "+rpd.getTitle()+" has been renewed.";
            mailService.send(to, subject, body);
            
            return rpd;
        }
        else {
            return null;
        }
    }

    @Transactional
    public Object notifyDealer(Integer listingId, String category) throws MessagingException {
        if(category.equalsIgnoreCase("Commercial") && cpdRepo.findById(listingId).isPresent()){
            CommercialPropertyDetails cpd = cpdRepo.findById(listingId).get();

            //notification flow
            NotificationDetails notification = new NotificationDetails();
            String message = "Your listing titled- "+cpd.getTitle()+" has expired. Please renew it from your Agent Dashboard.";
            notification.setNotificationMessage(message);
            notification.setNotificationType(NotificationType.ExpiredListing);
            notification.setNotificationReceiverId(cpd.getCommercialOwner().getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);

            //email flow
            String to = cpd.getCommercialOwner().getEmail();
            String subject = "Expired- "+cpd.getTitle();
            String body = "Your listing titled- "+cpd.getTitle()+" has expired. Please renew it from your Agent Dashboard.";
            mailService.send(to, subject, body);

            return cpd;
        }
        else
        if(category.equalsIgnoreCase("Residential") && rpdRepo.findById(listingId).isPresent()){
            ResidentialPropertyDetails rpd = rpdRepo.findById(listingId).get();

            //notification flow
            NotificationDetails notification = new NotificationDetails();
            notification.setNotificationType(NotificationType.ExpiredListing);
            String message = "Your listing titled- "+rpd.getTitle()+" has expired. Please renew it from your Agent Dashboard.";
            notification.setNotificationMessage(message);
            notification.setNotificationReceiverId(rpd.getResidentialOwner().getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);

            //email flow
            String to = rpd.getResidentialOwner().getEmail();
            String subject = "Expired- "+rpd.getTitle();
            String body = "Your listing titled- "+rpd.getTitle()+" has expired. Please renew it from your Agent Dashboard.";
            mailService.send(to, subject, body);
            return rpd;
        }
        else {
            return null;
        }
    }

    @Transactional
    public Object toggleVip(Integer listingId, String category) {
        if(category.equalsIgnoreCase("Commercial") && cpdRepo.findById(listingId).isPresent()){
            CommercialPropertyDetails cpd = cpdRepo.findById(listingId).get();
            cpd.setVip(!cpd.isVip());
            cpdRepo.save(cpd);
            return cpd;
        }
        else
        if(category.equalsIgnoreCase("Residential") && rpdRepo.findById(listingId).isPresent()){
            ResidentialPropertyDetails rpd = rpdRepo.findById(listingId).get();
            rpd.setVip(!rpd.isVip());
            rpdRepo.save(rpd);
            return rpd;
        }
        else {
            return null;
        }
    }

    @Transactional
    public Object toggleReraVerified(Integer listingId, String category) {
        if(category.equalsIgnoreCase("Commercial") && cpdRepo.findById(listingId).isPresent()){
            CommercialPropertyDetails cpd = cpdRepo.findById(listingId).get();
            cpd.setReraVerified(cpd.getReraVerified());
            cpdRepo.save(cpd);
            return cpd;
        }
        else
        if(category.equalsIgnoreCase("Residential") && rpdRepo.findById(listingId).isPresent()){
            ResidentialPropertyDetails rpd = rpdRepo.findById(listingId).get();
            rpd.setReraVerified(!rpd.getReraVerified());
            rpdRepo.save(rpd);
            return rpd;
        }
        else {
            return null;
        }
    }

    public Page<UserResponse> getAllUsers(int page, int size){
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "userId")
            );

        Page<Users> usersPage = userRepo.findAll(pageable);

        return usersPage.map(u -> UserMapper.toDto(u, gcsService));
    }

    public Page<AgentResponse> getAllSellers(int page, int size){
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "userId")
            );

        Page<Users> sellersPage = userRepo.findSellers(pageable);

        return sellersPage.map(u -> AgentMapper.toDto(u, gcsService));
    }

    @Transactional
    public Object togglePropaddaVerified(Integer userId) {
        if(userRepo.findById(userId).isPresent()){
            Users u = userRepo.findById(userId).get();
            u.setPropaddaVerified(!(u.getPropaddaVerified()));
            userRepo.save(u);
            return u;
        }
        else {
            return null;
        }
    }

    public Page<AgentResponse> pendingKycUsers(int page, int size){
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "userId")
            );

        Page<Users> sellersPage = userRepo.findUsersWithPendingKyc(pageable);

        return sellersPage.map(u -> AgentMapper.toDto(u, gcsService));
    }

    @Transactional
    public boolean approveKyc(Integer userId) throws MessagingException{
        Users user = userRepo.findById(userId).isPresent() ? userRepo.findById(userId).get() : null;
        if(user!=null){
            user.setKycVerified(Kyc.APPROVED);
            user.setRole(Role.AGENT);
            userRepo.save(user);

            //notification flow
            NotificationDetails notification = new NotificationDetails();
            String message = "Great news! KYC approved. Add properties now.";
            notification.setNotificationType(NotificationType.KycApproved);
            notification.setNotificationMessage(message);
            notification.setNotificationReceiverId(user.getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);

            //email flow
            String to = user.getEmail();
            String subject = "KYC Approved";
            String body = "Great news! KYC approved. Add properties now.";
            mailService.send(to, subject, body);

            return true;
        }
        else {
            return false;
        }
    }

    @Transactional
    public boolean rejectKyc(Integer userId, String reason) throws MessagingException{
        Optional<Users> user = userRepo.findById(userId);
        if(user.isPresent()){
            Users u = user.get();
            u.setKycVerified(Kyc.REJECTED);
            userRepo.save(u);
            RejectionDetails rejection = new RejectionDetails();
            rejection.setRejectionType(RejectionType.KYC);
            rejection.setRejectionReason(reason);
            rejection.setAgentId(userId);
            rejection.setAgentNotified(false);
            rejection.setAgentEmailed(false);
            rejectionRepo.save(rejection);

                        //notification flow
            NotificationDetails notification = new NotificationDetails();
            String message = "KYC was not approved for: "+reason+" Please edit & resubmit.";
            notification.setNotificationType(NotificationType.KycRejected);
            notification.setNotificationMessage(message);
            notification.setNotificationReceiverId(u.getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);

            //email flow
            String to = u.getEmail();
            String subject = "KYC Rejected";
            String body = "KYC was not approved for: "+reason+" Please edit & resubmit.";
            mailService.send(to, subject, body);

            return true;
        }
        else {
            return false;
        }
    }

    public Map<String,Long> dashboardMetrics(){
        Map<String,Long> metrics = new HashMap<>();
        metrics.put("totalProperties",cpdRepo.count()+rpdRepo.count());
        metrics.put("totalSellers",userRepo.countByRole(Role.AGENT));
        metrics.put("totalBuyers",userRepo.count());
        metrics.put("totalEnquiries",enqRepo.countByEnquiryStatus(EnquiryStatus.CREATED)+enqRepo.countByEnquiryStatus(EnquiryStatus.ASSIGNED));
        return metrics;
    }

    @Transactional
    public Boolean addNotification(NotificationDetails notification){
        notificationRepo.save(notification);
        return true;
    }

    public List<NotificationDetails> getAllNotificationsForAdmin(){
        return notificationRepo.findByNotificationReceiverRole(Role.ADMIN);
    }
    
    public List<NotificationDetails> getNewNotificationsForAdmin(){
        OffsetDateTime cutoff = OffsetDateTime.now().minusHours(24);
        return notificationRepo.findByCreatedAtAfterAndNotificationReceiverRole(cutoff, Role.ADMIN);
    }

    @Transactional
    public List<NotificationDetails> markNotificationViewed(Integer notificationId){
        if(notificationRepo.findById(notificationId).isPresent()){
            NotificationDetails n = notificationRepo.findById(notificationId).get();
            n.setNotificationViewed(true);
            notificationRepo.save(n);
        }
        return notificationRepo.findByNotificationReceiverRole(Role.ADMIN);
    }

    public Integer getNotificationCount(){
       List<NotificationDetails> noti = notificationRepo.findByNotificationReceiverRole(Role.ADMIN);
        Integer count=0;
        for(NotificationDetails n : noti){
            if(n.getNotificationViewed()!=null && n.getNotificationViewed()){
                
            } else {
                count++;
            }
        }
        return count;
    }

    @Transactional
    public List<NotificationDetails> markAllNotificationViewedForAdmin(){
        List<NotificationDetails> notifications = notificationRepo.findByNotificationReceiverRole(Role.ADMIN);
        if(!notifications.isEmpty()){
            for(NotificationDetails n : notifications){
                n.setNotificationViewed(true);
                notificationRepo.save(n);
            }
        }
        return notificationRepo.findByNotificationReceiverRole(Role.ADMIN);
    }

    public List<LeadsResponse> getCreatedLeads(){
        List<EnquiredListingsDetails> enqiries = enqRepo.findByEnquiryStatus(EnquiryStatus.CREATED);
        List<LeadsResponse> enqResponseList = new ArrayList<>();
        for(EnquiredListingsDetails e : enqiries){
            LeadsResponse lr = new LeadsResponse();
            lr.setEnquiryId(e.getEnquiryId());
            lr.setUser(UserMapper.toDto(userRepo.findById(e.getEnquiriesByBuyer().getUserId()).get(),gcsService));
            lr.setBuyerName(e.getBuyerName());
            lr.setBuyerPhoneNumber(e.getBuyerPhoneNumber());
            lr.setBuyerType(e.getBuyerType());
            lr.setBuyerReason(e.getBuyerReason());
            lr.setBuyerReasonDetail(e.getBuyerReasonDetail());
            lr.setEnquiryStatus(e.getEnquiryStatus());
            
            if(e.getPropertyCategory().equalsIgnoreCase("commercial")){
                lr.setComResponse(CommercialPropertyMapper.toDto(cpdRepo.findById(e.getPropertyId()).get()));
            } else {
                lr.setResResponse(ResidentialPropertyMapper.toDto(rpdRepo.findById(e.getPropertyId()).get()));
            }
            enqResponseList.add(lr);
        }
        return enqResponseList;
    }

    public List<LeadsResponse> getAssignedLeads(){
        List<EnquiredListingsDetails> enqiries = enqRepo.findByEnquiryStatus(EnquiryStatus.ASSIGNED);
        List<LeadsResponse> enqResponseList = new ArrayList<>();
        for(EnquiredListingsDetails e : enqiries){
            LeadsResponse lr = new LeadsResponse();
            lr.setEnquiryId(e.getEnquiryId());
            lr.setUser(UserMapper.toDto(userRepo.findById(e.getEnquiriesByBuyer().getUserId()).get(),gcsService));
            lr.setBuyerName(e.getBuyerName());
            lr.setBuyerPhoneNumber(e.getBuyerPhoneNumber());
            lr.setBuyerType(e.getBuyerType());
            lr.setBuyerReason(e.getBuyerReason());
            lr.setBuyerReasonDetail(e.getBuyerReasonDetail());
            lr.setEnquiryStatus(e.getEnquiryStatus());
            
            if(e.getPropertyCategory().equalsIgnoreCase("commercial")){
                lr.setComResponse(CommercialPropertyMapper.toDto(cpdRepo.findById(e.getPropertyId()).get()));
            } else {
                lr.setResResponse(ResidentialPropertyMapper.toDto(rpdRepo.findById(e.getPropertyId()).get()));
            }
            enqResponseList.add(lr);
        }
        return enqResponseList;
    }

    @Transactional
    public EnquiredListingsDetails markLeadAsAssigned(Integer enquiryId){
        EnquiredListingsDetails e = enqRepo.findById(enquiryId).orElseThrow(() -> new IllegalArgumentException("Enquiry not found: " + enquiryId));
        e.setEnquiryStatus(EnquiryStatus.ASSIGNED);
        enqRepo.save(e);
        return e;
    }

    @Transactional
    public EnquiredListingsDetails markLeadAsRemoved(Integer enquiryId){
        EnquiredListingsDetails e = enqRepo.findById(enquiryId).orElseThrow(() -> new IllegalArgumentException("Enquiry not found: " + enquiryId));
        e.setEnquiryStatus(EnquiryStatus.REMOVED);
        enqRepo.save(e);
        return e;
    }

    @Transactional
    public EnquiredListingsDetails markLeadAsSold(Integer enquiryId){
        EnquiredListingsDetails e = enqRepo.findById(enquiryId).orElseThrow(() -> new IllegalArgumentException("Enquiry not found: " + enquiryId));
        e.setEnquiryStatus(EnquiryStatus.SOLD);
        enqRepo.save(e);
        if(e.getPropertyCategory().equalsIgnoreCase("commercial")){
            CommercialPropertyDetails cpd = cpdRepo.findById(e.getPropertyId()).orElseThrow(() -> new IllegalArgumentException("Property not found: " + e.getPropertyId()));
            cpd.setSold(true);
            cpdRepo.save(cpd);
        } else {
            ResidentialPropertyDetails rpd = rpdRepo.findById(e.getPropertyId()).orElseThrow(() -> new IllegalArgumentException("Property not found: " + e.getPropertyId()));
            rpd.setSold(true);
            rpdRepo.save(rpd);
        }
        return e;
    }

    @Transactional
    public EnquiredListingsDetails markLeadAsNotInterested(Integer enquiryId){
        EnquiredListingsDetails e = enqRepo.findById(enquiryId).orElseThrow(() -> new IllegalArgumentException("Enquiry not found: " + enquiryId));
        e.setEnquiryStatus(EnquiryStatus.NOT_INTERESTED);
        enqRepo.save(e);
        return e;
    }

    public List<MediaProductionResponse> getMediaProductionRequests(){
        List<MediaProduction> mpList = mpRepo.findAll();
        List<MediaProductionResponse> mpResList = new ArrayList<>();
        for(MediaProduction mp : mpList){
            MediaProductionResponse mpRes = new MediaProductionResponse();
            mpRes.setMediaProductionId(mp.getMediaProductionId());
            mpRes.setGraphics(mp.getGraphics());
            mpRes.setPhotoshoot(mp.getPhotoshoot());
            mpRes.setAgent(AgentMapper.toDto(userRepo.findById(mp.getRequesterUserId()).get(),gcsService));

            if(mp.getPropertyCategory().equalsIgnoreCase("commercial")){
                mpRes.setComResponse(CommercialPropertyMapper.toDto(cpdRepo.findById(mp.getPropertyId()).get()));
            } else {
                mpRes.setResResponse(ResidentialPropertyMapper.toDto(rpdRepo.findById(mp.getPropertyId()).get()));
            }
            mpResList.add(mpRes);
        }
        return mpResList;
    }

    private static boolean hasValue(String s) {
        return s != null && !s.isBlank()
            && !"All".equalsIgnoreCase(s)
            && !"Any".equalsIgnoreCase(s);
    }

    private static Predicate eqIgnoreCase(
        CriteriaBuilder cb,
        Path<String> path,
        String value
    ) {
        return cb.equal(cb.lower(path), value.toLowerCase());
    }

    public Specification<AllPropertyViewFilter> detailedFilter(
        DetailedFilterRequest f, String adminApproved, Boolean expired, Boolean sold, Boolean vip) {
        return (root, query, cb) -> {

            List<Predicate> p = new ArrayList<>();

            if (hasValue(adminApproved)) {
                p.add(eqIgnoreCase(cb, root.get("adminApproved"), adminApproved));
            }

            if (expired!=null) {
                p.add(cb.equal(root.get("expired"), expired));
            }
            if (sold!=null) {
                p.add(cb.equal(root.get("sold"), sold));
            }
            if (vip!=null) {
                p.add(cb.equal(root.get("vip"), vip));
            }

            // category
            if (hasValue(f.category)) {
                p.add(eqIgnoreCase(cb, root.get("category"), f.category));
            }

            // preference
            if (hasValue(f.preference)) {
                p.add(eqIgnoreCase(cb, root.get("preference"), f.preference));
            }

            // furnishing
            if (hasValue(f.furnishing)) {
                p.add(eqIgnoreCase(cb, root.get("furnishing"), f.furnishing));
            }

            // property type (multi-select, case-insensitive)
            if (f.propertyType != null && !f.propertyType.isEmpty()) {
                p.add(
                    cb.lower(root.get("propertyType"))
                    .in(
                        f.propertyType.stream()
                            .map(String::toLowerCase)
                            .toList()
                    )
                );
            }

            // price
            if (f.priceMin != null) {
                p.add(cb.ge(root.get("price"), f.priceMin));
            }

            if (f.priceMax != null) {
                p.add(cb.le(root.get("price"), f.priceMax));
            }

            // state
            if (hasValue(f.state)) {
                p.add(eqIgnoreCase(cb, root.get("state"), f.state));
            }

            // city
            if (hasValue(f.city)) {
                p.add(eqIgnoreCase(cb, root.get("city"), f.city));
            }

            // availability
            if (hasValue(f.availability)) {
                p.add(eqIgnoreCase(cb, root.get("availability"), f.availability));
            }

            // area
            if (f.areaMin != null) {
                p.add(cb.ge(root.get("area"), f.areaMin));
            }

            if (f.areaMax != null) {
                p.add(cb.le(root.get("area"), f.areaMax));
            }

            // age (multi-select, case-insensitive)
            if (f.age != null && !f.age.isEmpty()) {
                p.add(
                    cb.lower(root.get("age"))
                    .in(
                        f.age.stream()
                            .map(String::toLowerCase)
                            .toList()
                    )
                );
            }

            // amenities (AND logic)
            if (f.amenities != null && !f.amenities.isEmpty()) {
                for (String a : f.amenities) {
                    switch (a) {
                        case "Elevator" -> p.add(cb.isTrue(root.get("elevator")));
                        case "Water 24x7" -> p.add(cb.isTrue(root.get("water24x7")));
                        case "Gas Pipeline" -> p.add(cb.isTrue(root.get("gasPipeline")));
                        case "Pet Friendly" -> p.add(cb.isTrue(root.get("petFriendly")));
                        case "Emergency Exit" -> p.add(cb.isTrue(root.get("emergencyExit")));
                        case "Wheelchair Friendly" -> p.add(cb.isTrue(root.get("wheelchairFriendly")));
                        case "Vastu Compliant" -> p.add(cb.isTrue(root.get("vastuCompliant")));
                        case "Pooja Room" -> p.add(cb.isTrue(root.get("poojaRoom")));
                        case "Study Room" -> p.add(cb.isTrue(root.get("studyRoom")));
                        case "Servant Room" -> p.add(cb.isTrue(root.get("servantRoom")));
                        case "Store Room" -> p.add(cb.isTrue(root.get("storeRoom")));
                        case "Modular Kitchen" -> p.add(cb.isTrue(root.get("modularKitchen")));
                        case "High Ceiling Height" -> p.add(cb.isTrue(root.get("highCeilingHeight")));
                        case "Park" -> p.add(cb.isTrue(root.get("park")));
                        case "Swimming Pool" -> p.add(cb.isTrue(root.get("swimmingPool")));
                        case "Gym" -> p.add(cb.isTrue(root.get("gym")));
                        case "Clubhouse / Community Center" ->
                                p.add(cb.isTrue(root.get("clubhouseCommunityCenter")));
                        case "Municipal Corporation" ->
                                p.add(cb.isTrue(root.get("municipalCorporation")));
                        case "In Gated Society" ->
                                p.add(cb.isTrue(root.get("inGatedSociety")));
                        case "Corner Property" ->
                                p.add(cb.isTrue(root.get("cornerProperty")));
                    }
                }
            }

            return cb.and(p.toArray(Predicate[]::new));
        };
    }

}
