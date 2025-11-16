// Author-Hemant Arora
package com.propadda.prop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.propadda.prop.enumerations.EnquiryStatus;
import com.propadda.prop.enumerations.NotificationType;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.mappers.CommercialPropertyMapper;
import com.propadda.prop.mappers.ResidentialPropertyMapper;
import com.propadda.prop.model.CommercialPropertyDetails;
import com.propadda.prop.model.EnquiredListingsDetails;
import com.propadda.prop.model.FavoriteListingsDetails;
import com.propadda.prop.model.NotificationDetails;
import com.propadda.prop.model.ResidentialPropertyDetails;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.CommercialPropertyDetailsRepo;
import com.propadda.prop.repo.EnquiredListingsDetailsRepo;
import com.propadda.prop.repo.FavoriteListingsDetailsRepo;
import com.propadda.prop.repo.NotificationDetailsRepository;
import com.propadda.prop.repo.ResidentialPropertyDetailsRepo;
import com.propadda.prop.repo.UsersRepo;

import jakarta.mail.MessagingException;

@Service
public class BuyerService {

    @Autowired
    private UsersRepo userRepo;
    
    @Autowired
    private CommercialPropertyDetailsRepo cRepo;

    @Autowired
    private ResidentialPropertyDetailsRepo rRepo;

    @Autowired
    private FavoriteListingsDetailsRepo favRepo;

    @Autowired
    private EnquiredListingsDetailsRepo enqRepo;

    @Autowired
    private MailSenderService mailService;
    
    @Autowired
    NotificationDetailsRepository notificationRepo;

    public Map<String,List<?>> allFavoritePropertiesByBuyer(Integer buyerId) {
        Users b = userRepo.findById(buyerId).isPresent() ? userRepo.findById(buyerId).get() : null;
        List<ResidentialPropertyDetails> rpd = new ArrayList<>();
        List<CommercialPropertyDetails> cpd = new ArrayList<>();
        if(b!=null){
            List<FavoriteListingsDetails> favs = favRepo.findByFavoritesOfBuyer(b);
            for(FavoriteListingsDetails f : favs){
                if(f.getPropertyCategory().equalsIgnoreCase("Commercial")){
                    if(cRepo.findById(f.getPropertyId()).isPresent())
                        cpd.add(cRepo.findById(f.getPropertyId()).get());
                }
                else if(f.getPropertyCategory().equalsIgnoreCase("Residential")){
                    if(rRepo.findById(f.getPropertyId()).isPresent())
                        rpd.add(rRepo.findById(f.getPropertyId()).get());
                }
            }
        Map<String,List<?>> res = new HashMap<>();
        res.put("Commercial",CommercialPropertyMapper.toDtoList(cpd));
        res.put("Residential", ResidentialPropertyMapper.toDtoList(rpd));
        return res;
        }
        else{
            return null;
        }
    }

    public Map<String,List<?>> allEnquiriesByBuyer(Integer buyerId) {
        Users b = userRepo.findById(buyerId).isPresent() ? userRepo.findById(buyerId).get() : null;
        List<ResidentialPropertyDetails> rpd = new ArrayList<>();
        List<CommercialPropertyDetails> cpd = new ArrayList<>();
        if(b!=null){
            List<EnquiredListingsDetails> enqs = enqRepo.findByEnquiriesByBuyer(b);
            for(EnquiredListingsDetails e : enqs){
                if(e.getPropertyCategory().equalsIgnoreCase("Commercial")){
                    if(cRepo.findById(e.getPropertyId()).isPresent())
                        cpd.add(cRepo.findById(e.getPropertyId()).get());
                }
                else if(e.getPropertyCategory().equalsIgnoreCase("Residential")){
                    if(rRepo.findById(e.getPropertyId()).isPresent())
                        rpd.add(rRepo.findById(e.getPropertyId()).get());
                }
            }
        Map<String,List<?>> res = new HashMap<>();
        res.put("Commercial",CommercialPropertyMapper.toDtoList(cpd));
        res.put("Residential", ResidentialPropertyMapper.toDtoList(rpd));
        return res;
        }
        else{
            return null;
        }
    }

    public FavoriteListingsDetails addPropertyToFavoritesForBuyer(String category, Integer listingId, Integer buyerId) {
        FavoriteListingsDetails f = new FavoriteListingsDetails();
        Users u = userRepo.findById(buyerId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if(favRepo.findByFavoritesOfBuyerAndPropertyIdAndPropertyCategory(u,listingId,category).isPresent()){
            return null;
        }
        f.setFavoritesOfBuyer(userRepo.findById(buyerId).get());
        f.setPropertyCategory(category);
        f.setPropertyId(listingId);
        return favRepo.save(f);
    }

    public boolean removePropertyFromFavoritesForBuyer(String category, Integer listingId, Integer buyerId) {
        Users u = userRepo.findById(buyerId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Optional<FavoriteListingsDetails> opt = favRepo.findByFavoritesOfBuyerAndPropertyIdAndPropertyCategory(u, listingId, category);
        if (opt.isPresent()) {
            favRepo.delete(opt.get());
            return true;
        }
        // not found
        return false;
    }

    public Boolean checkFavorite(String category, Integer listingId, Integer buyerId) {
        
        Users u = userRepo.findById(buyerId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return !favRepo.findByFavoritesOfBuyerAndPropertyIdAndPropertyCategory(u,listingId,category).isEmpty();
    }

    public EnquiredListingsDetails sendEnquiriesFromBuyer(EnquiredListingsDetails enquiry, String category, Integer listingId, Integer buyerId) throws MessagingException {
        EnquiredListingsDetails e = new EnquiredListingsDetails();
        Users u = userRepo.findById(buyerId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if(enqRepo.findByEnquiriesByBuyerAndPropertyIdAndPropertyCategory(u,listingId,category).isPresent()){
            return null;
        }
        e.setEnquiriesByBuyer(userRepo.findById(buyerId).get());
        e.setPropertyCategory(category);
        e.setPropertyId(listingId);
        e.setBuyerName(enquiry.getBuyerName());
        e.setBuyerPhoneNumber(enquiry.getBuyerPhoneNumber());
        e.setBuyerType(enquiry.getBuyerType());
        e.setBuyerReason(enquiry.getBuyerReason());
        e.setBuyerReasonDetail(enquiry.getBuyerReasonDetail());
        e.setEnquiryStatus(EnquiryStatus.CREATED);
        enqRepo.save(e);

        //email flow for buyer
        String title;
        if(category.equalsIgnoreCase("residential")){
            ResidentialPropertyDetails rpd = rRepo.findById(listingId).get();
            title = rpd.getTitle();
        }
        else {
            CommercialPropertyDetails cpd = cRepo.findById(listingId).get();
            title = cpd.getTitle();
        }
        String to = u.getEmail();
        String subject = "Enquiry received- "+title;
        String body = "We've received your enquiry for listing- "+title+". Our team will contact you shortly.";
        mailService.send(to, subject, body);

        //notification flow for admin
        NotificationDetails notification = new NotificationDetails();
        String message = "New enquiry on "+title+" from "+u.getFirstName()+" "+u.getLastName()+". Email: "+u.getEmail()+" and Phone number: "+u.getPhoneNumber();
        notification.setNotificationType(NotificationType.EnquiryForAdmin);
        notification.setNotificationMessage(message);
        notification.setNotificationReceiverId(1);
        notification.setNotificationReceiverRole(Role.ADMIN);
        notification.setNotificationSenderId(buyerId);
        notification.setNotificationSenderRole(Role.BUYER);
        notificationRepo.save(notification);

        //email flow for admin
        String toAdmin = "sales@propadda.in";
        String subjectAdmin = "New Enquiry on- "+title;
        String bodyAdmin = "New enquiry on "+title+" from "+u.getFirstName()+" "+u.getLastName()+". Email: "+u.getEmail()+" and Phone number: "+u.getPhoneNumber();
        mailService.send(toAdmin, subjectAdmin, bodyAdmin);
        return e;
    }

    public Boolean checkEnquiry(EnquiredListingsDetails enquiry, String category, Integer listingId, Integer buyerId) {
        Users u = userRepo.findById(buyerId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return !enqRepo.findByEnquiriesByBuyerAndPropertyIdAndPropertyCategory(u,listingId,category).isEmpty();
    }


}