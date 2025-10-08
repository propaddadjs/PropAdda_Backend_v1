package com.propadda.prop.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.propadda.prop.dto.AgentResponse;
import com.propadda.prop.dto.CommercialPropertyResponse;
import com.propadda.prop.dto.ResidentialPropertyResponse;
import com.propadda.prop.dto.UserResponse;
import com.propadda.prop.enumerations.Kyc;
import com.propadda.prop.enumerations.NotificationType;
import com.propadda.prop.enumerations.RejectionType;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.exceptions.ResourceNotFoundException;
import com.propadda.prop.mappers.AgentMapper;
import com.propadda.prop.mappers.CommercialPropertyMapper;
import com.propadda.prop.mappers.ResidentialPropertyMapper;
import com.propadda.prop.mappers.UserMapper;
import com.propadda.prop.model.CommercialPropertyDetails;
import com.propadda.prop.model.NotificationDetails;
import com.propadda.prop.model.RejectionDetails;
import com.propadda.prop.model.ResidentialPropertyDetails;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.CommercialPropertyDetailsRepo;
import com.propadda.prop.repo.NotificationDetailsRepository;
import com.propadda.prop.repo.RejectionDetailsRepository;
import com.propadda.prop.repo.ResidentialPropertyDetailsRepo;
import com.propadda.prop.repo.UsersRepo;

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

    public Map<String,List<?>> getAllProperties(){
        
        List<ResidentialPropertyDetails> rpd = rpdRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,false);
        List<CommercialPropertyDetails> cpd = cpdRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,false);

        Map<String, List<?>> prop = new HashMap<>();
        prop.put("commercial",ResidentialPropertyMapper.toDtoList(rpd));
        prop.put("residential",CommercialPropertyMapper.toDtoList(cpd));
        return prop;
    }

    public Map<String,List<?>> getExpiredProperties(){
        List<ResidentialPropertyDetails> rpd = rpdRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,true);
        List<CommercialPropertyDetails> cpd = cpdRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,true);

        Map<String, List<?>> prop = new HashMap<>();
        prop.put("commercial",ResidentialPropertyMapper.toDtoList(rpd));
        prop.put("residential",CommercialPropertyMapper.toDtoList(cpd));
        return prop;
    }

    public Map<String,List<?>> getVipProperties(){
        List<ResidentialPropertyDetails> rpd = rpdRepo.findByAdminApprovedAndSoldAndExpiredAndVip("Approved",false,false,true);
        List<CommercialPropertyDetails> cpd = cpdRepo.findByAdminApprovedAndSoldAndExpiredAndVip("Approved",false,false,true);

        Map<String, List<?>> prop = new HashMap<>();
        prop.put("commercial",ResidentialPropertyMapper.toDtoList(rpd));
        prop.put("residential",CommercialPropertyMapper.toDtoList(cpd));
        return prop;
    }

    public Map<String,List<?>> getPendingProperties(){
        List<ResidentialPropertyDetails> rpd = rpdRepo.findByAdminApprovedAndSoldAndExpired("Pending",false,false);
        List<CommercialPropertyDetails> cpd = cpdRepo.findByAdminApprovedAndSoldAndExpired("Pending",false,false);

        Map<String, List<?>> prop = new HashMap<>();
        prop.put("commercial",ResidentialPropertyMapper.toDtoList(rpd));
        prop.put("residential",CommercialPropertyMapper.toDtoList(cpd));
        return prop;
    }

    public Map<String,List<?>> getSoldProperties(){
        List<ResidentialPropertyDetails> rpd = rpdRepo.findBySold(true);
        List<CommercialPropertyDetails> cpd = cpdRepo.findBySold(true);

        Map<String, List<?>> prop = new HashMap<>();
        prop.put("commercial",ResidentialPropertyMapper.toDtoList(rpd));
        prop.put("residential",CommercialPropertyMapper.toDtoList(cpd));
        return prop;
    }

    public Map<String, List<?>> combinedFilteredAllPropList(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<ResidentialPropertyResponse> residentialDtos = filterAllResProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
        List<CommercialPropertyResponse> commercialDtos = filterAllComProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);

        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", residentialDtos);
        res.put("commercial", commercialDtos);
        
        return res;
    }

    public Map<String, List<?>> combinedFilteredPendingPropList(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<ResidentialPropertyResponse> residentialDtos = filterPendingResProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
        List<CommercialPropertyResponse> commercialDtos = filterPendingComProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);

        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", residentialDtos);
        res.put("commercial", commercialDtos);
        
        return res;
    }

    public Map<String, List<?>> combinedFilteredExpiredPropList(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<ResidentialPropertyResponse> residentialDtos = filterExpiredResProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
        List<CommercialPropertyResponse> commercialDtos = filterExpiredComProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);

        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", residentialDtos);
        res.put("commercial", commercialDtos);
        
        return res;
    }

    public Map<String, List<?>> combinedFilteredVipPropList(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<ResidentialPropertyResponse> residentialDtos = filterVipResProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);
        List<CommercialPropertyResponse> commercialDtos = filterVipComProp(propertyTypes, preference, priceMin, priceMax, furnishing, state, city, amenities, availability, areaMin, areaMax, ageRanges);

        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", residentialDtos);
        res.put("commercial", commercialDtos);
        
        return res;
    }

    public List<ResidentialPropertyResponse> filterAllResProp(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<ResidentialPropertyDetails> allResProp =  rpdRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,false);
        List<ResidentialPropertyDetails> filteredList =  new ArrayList<>();
        List<String> propTypes = propertyTypes!=null ? Arrays.asList(propertyTypes.split(",")) : new ArrayList<>();
    System.out.println("propTypes list : "+propTypes);
        List<String> amen = amenities!=null ? Arrays.asList(amenities.split(",")) : new ArrayList<>();
    System.out.println("amenities list : "+amen);
        List<String> ageRan = ageRanges!=null ? Arrays.asList(ageRanges.split(",")) : new ArrayList<>();
    System.out.println("ageRanges list : "+ageRan);
 
        for(ResidentialPropertyDetails rpd: allResProp){
            boolean propertyTypeValue = false;
            boolean preferenceValue = false;
            boolean priceMinValue = false;
            boolean priceMaxValue = false;
            boolean furnishingValue = false;
            boolean stateValue = false;
            boolean cityValue=false;
            boolean availabilityValue = false;
            boolean areaMinValue = false;
            boolean areaMaxValue = false;
            boolean ageRangesValue = false;
            boolean amenitiesValue = false;

            boolean isElevator=false; 
            boolean isGasPipeline=false; 
            boolean isEmergencyExit=false; 
            boolean isWater24x7=false; 
            boolean isPetFriendly=false; 
            boolean isWheelchairFriendly=false; 
            boolean isVastuCompliant=false; 
            boolean isStudyRoom=false; 
            boolean isStoreRoom=false; 
            boolean isHighCeilingHeight=false; 
            boolean isPoojaRoom=false; 
            boolean isServantRoom=false; 
            boolean isModularKitchen=false; 
            boolean isPark=false; 
            boolean isSwimmingPool=false; 
            boolean isClubhouseCommunityCenter=false; 
            boolean isInGatedSociety=false; 
            boolean isGym=false; 
            boolean isMunicipalCorporation=false; 
            boolean isCornerProperty = false;

            if(propertyTypes==null){
                propertyTypeValue = true;
            } else
            if(propTypes.contains(rpd.getPropertyType())){
                propertyTypeValue = propTypes.contains(rpd.getPropertyType());
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

            if(preference==null){
                preferenceValue = true;
            } else
            if(rpd.getPreference().equalsIgnoreCase(preference)){
                preferenceValue = rpd.getPreference().equalsIgnoreCase(preference);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of preferenceValue:: "+preferenceValue);
            
            if(priceMin == null){
                priceMinValue = true;
            } else
            if(rpd.getPrice()>=priceMin){
                priceMinValue = rpd.getPrice()>=priceMin;
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of priceMinValue:: "+priceMinValue);

            if(priceMax==null){
                priceMaxValue = true;
            } else
            if(rpd.getPrice()<=priceMax){
                priceMaxValue = rpd.getPrice()<=priceMax;
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of priceMaxValue:: "+priceMaxValue);

            if(furnishing==null){
                furnishingValue = true;
            } else
            if(rpd.getFurnishing().equalsIgnoreCase(furnishing)){
                furnishingValue = rpd.getFurnishing().equalsIgnoreCase(furnishing);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of furnishingValue:: "+furnishingValue);

            if(state==null){
                stateValue = true;
            } else
            if(rpd.getState().equalsIgnoreCase(state)){
                stateValue = rpd.getState().equalsIgnoreCase(state);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of stateValue:: "+stateValue);

            if(city==null){
                cityValue = true;
            } else
            if(rpd.getCity().equalsIgnoreCase(city)){
                cityValue = rpd.getCity().equalsIgnoreCase(city);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of cityValue:: "+cityValue);

            if(availability==null){
                availabilityValue = true;
            } else
            if(rpd.getAvailability().equalsIgnoreCase(availability)){
                availabilityValue = rpd.getAvailability().equalsIgnoreCase(availability);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of availabilityValue:: "+availabilityValue);

            if(areaMin==null){
                areaMinValue = true;
            } else
            if(rpd.getArea()>=Double.valueOf(areaMin)){
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of rpd.getArea():: "+rpd.getArea()+" and Double.valueOf(areaMin):: "+Double.valueOf(areaMin));
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of (rpd.getArea()>=Double.valueOf(areaMin)):: "+(rpd.getArea()>=Double.valueOf(areaMin)));
                areaMinValue = rpd.getArea()>=Double.valueOf(areaMin);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of areaMinValue:: "+areaMinValue);

            if(areaMax==null){
                areaMaxValue = true;
            } else
            if(rpd.getArea()<=Double.valueOf(areaMax)){
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of rpd.getArea():: "+rpd.getArea()+" and Double.valueOf(areaMax):: "+Double.valueOf(areaMax));
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of (rpd.getArea()<=Double.valueOf(areaMax):: "+(rpd.getArea()<=Double.valueOf(areaMax)));
                areaMaxValue = rpd.getArea()<=Double.valueOf(areaMax);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of areaMaxValue:: "+areaMaxValue);

            if(ageRanges==null){
                ageRangesValue = true;
            } else
            if(ageRan.contains(rpd.getAge())){
                ageRangesValue = ageRan.contains(rpd.getAge());
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of ageRangesValue:: "+ageRangesValue);

            if(amenities==null){
                isElevator=true;
            } else
            if(!amen.isEmpty() && amen.contains("Elevator")){
                isElevator = rpd.getAmenities().isElevator();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isElevator:: "+isElevator);

            if(amenities==null){
                isGasPipeline=true;
            } else
            if(!amen.isEmpty() && amen.contains("Gas Pipeline")){
                isGasPipeline = rpd.getAmenities().isGasPipeline();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isGasPipeline:: "+isGasPipeline);

            if(amenities==null){
                isEmergencyExit=true;
            } else
            if(!amen.isEmpty() && amen.contains("Emergency Exit")){
                isEmergencyExit = rpd.getAmenities().isEmergencyExit();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isEmergencyExit:: "+isEmergencyExit);

            if(amenities==null){
                isWater24x7=true;
            } else
            if(!amen.isEmpty() && amen.contains("Water 24x7")){
                isWater24x7 = rpd.getAmenities().isWater24x7();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isWater24x7:: "+isWater24x7);

            if(amenities==null){
                isPetFriendly=true;
            } else
            if(!amen.isEmpty() && amen.contains("Pet Friendly")){
                isPetFriendly = rpd.getAmenities().isPetFriendly();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPetFriendly:: "+isPetFriendly);

            if(amenities==null){
                isWheelchairFriendly=true;
            } else
            if(!amen.isEmpty() && amen.contains("Wheelchair Friendly")){
                isWheelchairFriendly = rpd.getAmenities().isWheelchairFriendly();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isWheelchairFriendly:: "+isWheelchairFriendly);

            if(amenities==null){
                isVastuCompliant=true;
            } else
            if(!amen.isEmpty() && amen.contains("Vastu Compliant")){
                isVastuCompliant = rpd.getAmenities().isVastuCompliant();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isVastuCompliant:: "+isVastuCompliant);

            if(amenities==null){
                isStudyRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Study Room")){
                isStudyRoom = rpd.getAmenities().isStudyRoom();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isStudyRoom:: "+isStudyRoom);

            if(amenities==null){
                isStoreRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Store Room")){
                isStoreRoom = rpd.getAmenities().isStoreRoom();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isStoreRoom:: "+isStoreRoom);

            if(amenities==null){
                isHighCeilingHeight=true;
            } else
            if(!amen.isEmpty() && amen.contains("High Ceiling Height")){
                isHighCeilingHeight = rpd.getAmenities().isHighCeilingHeight();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isHighCeilingHeight:: "+isHighCeilingHeight);

            if(amenities==null){
                isPoojaRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Pooja Room")){
                isPoojaRoom = rpd.getAmenities().isPoojaRoom();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPoojaRoom:: "+isPoojaRoom);

            if(amenities==null){
                isServantRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Servant Room")){
                isServantRoom = rpd.getAmenities().isServantRoom();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isServantRoom:: "+isServantRoom);

            if(amenities==null){
                isModularKitchen=true;
            } else
            if(!amen.isEmpty() && amen.contains("Modular Kitchen")){
                isModularKitchen = rpd.getAmenities().isModularKitchen();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isModularKitchen:: "+isModularKitchen);

            if(amenities==null){
                isPark=true;
            } else
            if(!amen.isEmpty() && amen.contains("Park")){
                isPark = rpd.getAmenities().isPark();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPark:: "+isPark);

            if(amenities==null){
                isSwimmingPool=true;
            } else
            if(!amen.isEmpty() && amen.contains("Swimming Pool")){
                isSwimmingPool = rpd.getAmenities().isSwimmingPool();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isSwimmingPool:: "+isSwimmingPool);

            if(amenities==null){
                isClubhouseCommunityCenter=true;
            } else
            if(!amen.isEmpty() && amen.contains("Clubhouse / Community Center")){
                isClubhouseCommunityCenter = rpd.getAmenities().isClubhouseCommunityCenter();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isClubhouseCommunityCenter:: "+isClubhouseCommunityCenter);

            if(amenities==null){
                isInGatedSociety=true;
            } else
            if(!amen.isEmpty() && amen.contains("In Gated Society")){
                isInGatedSociety = rpd.getAmenities().isInGatedSociety();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isInGatedSociety:: "+isInGatedSociety);

            if(amenities==null){
                isGym=true;
            } else
            if(!amen.isEmpty() && amen.contains("Gym")){
                isGym = rpd.getAmenities().isGym();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isGym:: "+isGym);

            if(amenities==null){
                isMunicipalCorporation=true;
            } else
            if(!amen.isEmpty() && amen.contains("Municipal Corporation")){
                isMunicipalCorporation = rpd.getAmenities().isMunicipalCorporation();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isMunicipalCorporation:: "+isMunicipalCorporation);
            
            if(amenities==null){
                isCornerProperty=true;
            } else
            if(!amen.isEmpty() && amen.contains("Corner Property")){
                isCornerProperty = rpd.getAmenities().isCornerProperty();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isCornerProperty:: "+isCornerProperty);

            if(amenities==null){
                amenitiesValue = true;
            }else{
                amenitiesValue = (isElevator || isGasPipeline || isEmergencyExit || isWater24x7 || isPetFriendly || isWheelchairFriendly || isVastuCompliant || isStudyRoom || isStoreRoom || isHighCeilingHeight || isPoojaRoom || isServantRoom || isModularKitchen || isPark || isSwimmingPool || isClubhouseCommunityCenter || isInGatedSociety || isGym || isMunicipalCorporation || isCornerProperty);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of amenitiesValue:: "+amenitiesValue);
        
            if(propertyTypeValue && preferenceValue && priceMinValue && priceMaxValue && furnishingValue && stateValue && cityValue && availabilityValue && areaMinValue && areaMaxValue && ageRangesValue &&
            amenitiesValue
            )
            {
                filteredList.add(rpd);
            System.out.println("---------Added into residential filtered list::::::::"+rpd.getListingId());
            }
        }
        List<ResidentialPropertyResponse> filteredResidentialDtos = ResidentialPropertyMapper.toDtoList(filteredList);
    System.out.println("Residential filteredList after transformation:::::::"+filteredResidentialDtos);
        return filteredResidentialDtos;
    }

    public List<CommercialPropertyResponse> filterAllComProp(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<CommercialPropertyDetails> allComProp =  cpdRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,false);
        List<CommercialPropertyDetails> filteredList =  new ArrayList<>();
        List<String> propTypes = propertyTypes!=null ? Arrays.asList(propertyTypes.split(",")) : new ArrayList<>();
    System.out.println("propTypes list : "+propTypes);
        //List<String> amen = amenities!=null ? Arrays.asList(amenities.split(",")) : new ArrayList<>();
        List<String> ageRan = ageRanges!=null ? Arrays.asList(ageRanges.split(",")) : new ArrayList<>();
    System.out.println("ageRanges list : "+ageRan);
        
        for(CommercialPropertyDetails cpd: allComProp){

            boolean propertyTypeValue = false;
            boolean preferenceValue = false;
            boolean priceMinValue = false;
            boolean priceMaxValue = false;
            boolean stateValue = false;
            boolean cityValue=false;
            boolean availabilityValue = false;
            boolean areaMinValue = false;
            boolean areaMaxValue = false;
            boolean ageRangesValue = false;
            boolean furnishingValue = false;
            boolean amenitiesValue = false;

            if(propertyTypes==null){
                propertyTypeValue = true;
            } else
            if(propTypes.contains(cpd.getPropertyType())){
                propertyTypeValue = propTypes.contains(cpd.getPropertyType());
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

            if(preference==null){
                preferenceValue = true;
            } else
            if(cpd.getPreference().equalsIgnoreCase(preference)){
                preferenceValue = cpd.getPreference().equalsIgnoreCase(preference);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of preferenceValue:: "+preferenceValue);
            
            if(priceMin == null){
                priceMinValue = true;
            } else
            if(cpd.getPrice()>=priceMin){
                priceMinValue = cpd.getPrice()>=priceMin;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of priceMinValue:: "+priceMinValue);

            if(priceMax==null){
                priceMaxValue = true;
            } else
            if(cpd.getPrice()<=priceMax){
                priceMaxValue = cpd.getPrice()<=priceMax;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of priceMaxValue:: "+priceMaxValue);

            if(state==null){
                stateValue = true;
            } else
            if(cpd.getState().equalsIgnoreCase(state)){
                stateValue = cpd.getState().equalsIgnoreCase(state);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of stateValue:: "+stateValue);

            if(city==null){
                cityValue = true;
            } else
            if(cpd.getCity().equalsIgnoreCase(city)){
                cityValue = cpd.getCity().equalsIgnoreCase(city);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cityValue:: "+cityValue);

            if(availability==null){
                availabilityValue = true;
            } else
            if(cpd.getAvailability().equalsIgnoreCase(availability)){
                availabilityValue = cpd.getAvailability().equalsIgnoreCase(availability);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of availabilityValue:: "+availabilityValue);

            if(areaMin==null){
                areaMinValue = true;
            } else
            if(cpd.getArea()>=Double.valueOf(areaMin)){
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cpd.getArea():: "+cpd.getArea()+" and Double.valueOf(areaMin):: "+Double.valueOf(areaMin));
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of (cpd.getArea()>=Double.valueOf(areaMin)):: "+(cpd.getArea()>=Double.valueOf(areaMin)));
                areaMinValue = cpd.getArea()>=Double.valueOf(areaMin);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of areaMinValue:: "+areaMinValue);

            if(areaMax==null){
                areaMaxValue = true;
            } else
            if(cpd.getArea()<=Double.valueOf(areaMax)){
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cpd.getArea():: "+cpd.getArea()+" and Double.valueOf(areaMax):: "+Double.valueOf(areaMax));
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of (cpd.getArea()<=Double.valueOf(areaMax):: "+(cpd.getArea()<=Double.valueOf(areaMax)));
                areaMaxValue = cpd.getArea()<=Double.valueOf(areaMax);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of areaMaxValue:: "+areaMaxValue);

            if(ageRanges==null){
                ageRangesValue = true;
            } else
            if(ageRan.contains(cpd.getAge())){
                ageRangesValue = ageRan.contains(cpd.getAge());
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of ageRangesValue:: "+ageRangesValue);

            if(furnishing==null){
                furnishingValue=true;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of furnishingValue:: "+furnishingValue);

            if(amenities==null){
                amenitiesValue=true;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of amenitiesValue:: "+amenitiesValue);

            if(propertyTypeValue && preferenceValue && priceMinValue && priceMaxValue && stateValue && cityValue && availabilityValue && areaMinValue && areaMaxValue && ageRangesValue && furnishingValue && amenitiesValue
            ){
                filteredList.add(cpd);
            System.out.println("---------Added into commercial filtered list::::::::"+cpd.getListingId());
            }
        }

        List<CommercialPropertyResponse> filteredCommercialDtos = CommercialPropertyMapper.toDtoList(filteredList);
    System.out.println("Commercial filteredList after transformation:::::::"+filteredCommercialDtos);
        return filteredCommercialDtos;
    }

    public List<ResidentialPropertyResponse> filterPendingResProp(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<ResidentialPropertyDetails> allResProp =  rpdRepo.findByAdminApprovedAndSoldAndExpired("Pending",false,false);
        List<ResidentialPropertyDetails> filteredList =  new ArrayList<>();
        List<String> propTypes = propertyTypes!=null ? Arrays.asList(propertyTypes.split(",")) : new ArrayList<>();
    System.out.println("propTypes list : "+propTypes);
        List<String> amen = amenities!=null ? Arrays.asList(amenities.split(",")) : new ArrayList<>();
    System.out.println("amenities list : "+amen);
        List<String> ageRan = ageRanges!=null ? Arrays.asList(ageRanges.split(",")) : new ArrayList<>();
    System.out.println("ageRanges list : "+ageRan);

        for(ResidentialPropertyDetails rpd: allResProp){
            boolean propertyTypeValue = false;
            boolean preferenceValue = false;
            boolean priceMinValue = false;
            boolean priceMaxValue = false;
            boolean furnishingValue = false;
            boolean stateValue = false;
            boolean cityValue=false;
            boolean availabilityValue = false;
            boolean areaMinValue = false;
            boolean areaMaxValue = false;
            boolean ageRangesValue = false;
            boolean amenitiesValue = false;

            boolean isElevator=false; 
            boolean isGasPipeline=false; 
            boolean isEmergencyExit=false; 
            boolean isWater24x7=false; 
            boolean isPetFriendly=false; 
            boolean isWheelchairFriendly=false; 
            boolean isVastuCompliant=false; 
            boolean isStudyRoom=false; 
            boolean isStoreRoom=false; 
            boolean isHighCeilingHeight=false; 
            boolean isPoojaRoom=false; 
            boolean isServantRoom=false; 
            boolean isModularKitchen=false; 
            boolean isPark=false; 
            boolean isSwimmingPool=false; 
            boolean isClubhouseCommunityCenter=false; 
            boolean isInGatedSociety=false; 
            boolean isGym=false; 
            boolean isMunicipalCorporation=false; 
            boolean isCornerProperty = false;

            if(propertyTypes==null){
                propertyTypeValue = true;
            } else
            if(propTypes.contains(rpd.getPropertyType())){
                propertyTypeValue = propTypes.contains(rpd.getPropertyType());
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

            if(preference==null){
                preferenceValue = true;
            } else
            if(rpd.getPreference().equalsIgnoreCase(preference)){
                preferenceValue = rpd.getPreference().equalsIgnoreCase(preference);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of preferenceValue:: "+preferenceValue);
            
            if(priceMin == null){
                priceMinValue = true;
            } else
            if(rpd.getPrice()>=priceMin){
                priceMinValue = rpd.getPrice()>=priceMin;
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of priceMinValue:: "+priceMinValue);

            if(priceMax==null){
                priceMaxValue = true;
            } else
            if(rpd.getPrice()<=priceMax){
                priceMaxValue = rpd.getPrice()<=priceMax;
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of priceMaxValue:: "+priceMaxValue);

            if(furnishing==null){
                furnishingValue = true;
            } else
            if(rpd.getFurnishing().equalsIgnoreCase(furnishing)){
                furnishingValue = rpd.getFurnishing().equalsIgnoreCase(furnishing);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of furnishingValue:: "+furnishingValue);

            if(state==null){
                stateValue = true;
            } else
            if(rpd.getState().equalsIgnoreCase(state)){
                stateValue = rpd.getState().equalsIgnoreCase(state);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of stateValue:: "+stateValue);

            if(city==null){
                cityValue = true;
            } else
            if(rpd.getCity().equalsIgnoreCase(city)){
                cityValue = rpd.getCity().equalsIgnoreCase(city);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of cityValue:: "+cityValue);

            if(availability==null){
                availabilityValue = true;
            } else
            if(rpd.getAvailability().equalsIgnoreCase(availability)){
                availabilityValue = rpd.getAvailability().equalsIgnoreCase(availability);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of availabilityValue:: "+availabilityValue);

            if(areaMin==null){
                areaMinValue = true;
            } else
            if(rpd.getArea()>=Double.valueOf(areaMin)){
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of rpd.getArea():: "+rpd.getArea()+" and Double.valueOf(areaMin):: "+Double.valueOf(areaMin));
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of (rpd.getArea()>=Double.valueOf(areaMin)):: "+(rpd.getArea()>=Double.valueOf(areaMin)));
                areaMinValue = rpd.getArea()>=Double.valueOf(areaMin);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of areaMinValue:: "+areaMinValue);

            if(areaMax==null){
                areaMaxValue = true;
            } else
            if(rpd.getArea()<=Double.valueOf(areaMax)){
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of rpd.getArea():: "+rpd.getArea()+" and Double.valueOf(areaMax):: "+Double.valueOf(areaMax));
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of (rpd.getArea()<=Double.valueOf(areaMax):: "+(rpd.getArea()<=Double.valueOf(areaMax)));
                areaMaxValue = rpd.getArea()<=Double.valueOf(areaMax);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of areaMaxValue:: "+areaMaxValue);

            if(ageRanges==null){
                ageRangesValue = true;
            } else
            if(ageRan.contains(rpd.getAge())){
                ageRangesValue = ageRan.contains(rpd.getAge());
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of ageRangesValue:: "+ageRangesValue);

            if(amenities==null){
                isElevator=true;
            } else
            if(!amen.isEmpty() && amen.contains("Elevator")){
                isElevator = rpd.getAmenities().isElevator();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isElevator:: "+isElevator);

            if(amenities==null){
                isGasPipeline=true;
            } else
            if(!amen.isEmpty() && amen.contains("Gas Pipeline")){
                isGasPipeline = rpd.getAmenities().isGasPipeline();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isGasPipeline:: "+isGasPipeline);

            if(amenities==null){
                isEmergencyExit=true;
            } else
            if(!amen.isEmpty() && amen.contains("Emergency Exit")){
                isEmergencyExit = rpd.getAmenities().isEmergencyExit();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isEmergencyExit:: "+isEmergencyExit);

            if(amenities==null){
                isWater24x7=true;
            } else
            if(!amen.isEmpty() && amen.contains("Water 24x7")){
                isWater24x7 = rpd.getAmenities().isWater24x7();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isWater24x7:: "+isWater24x7);

            if(amenities==null){
                isPetFriendly=true;
            } else
            if(!amen.isEmpty() && amen.contains("Pet Friendly")){
                isPetFriendly = rpd.getAmenities().isPetFriendly();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPetFriendly:: "+isPetFriendly);

            if(amenities==null){
                isWheelchairFriendly=true;
            } else
            if(!amen.isEmpty() && amen.contains("Wheelchair Friendly")){
                isWheelchairFriendly = rpd.getAmenities().isWheelchairFriendly();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isWheelchairFriendly:: "+isWheelchairFriendly);

            if(amenities==null){
                isVastuCompliant=true;
            } else
            if(!amen.isEmpty() && amen.contains("Vastu Compliant")){
                isVastuCompliant = rpd.getAmenities().isVastuCompliant();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isVastuCompliant:: "+isVastuCompliant);

            if(amenities==null){
                isStudyRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Study Room")){
                isStudyRoom = rpd.getAmenities().isStudyRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isStudyRoom:: "+isStudyRoom);

            if(amenities==null){
                isStoreRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Store Room")){
                isStoreRoom = rpd.getAmenities().isStoreRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isStoreRoom:: "+isStoreRoom);

            if(amenities==null){
                isHighCeilingHeight=true;
            } else
            if(!amen.isEmpty() && amen.contains("High Ceiling Height")){
                isHighCeilingHeight = rpd.getAmenities().isHighCeilingHeight();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isHighCeilingHeight:: "+isHighCeilingHeight);

            if(amenities==null){
                isPoojaRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Pooja Room")){
                isPoojaRoom = rpd.getAmenities().isPoojaRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPoojaRoom:: "+isPoojaRoom);

            if(amenities==null){
                isServantRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Servant Room")){
                isServantRoom = rpd.getAmenities().isServantRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isServantRoom:: "+isServantRoom);

            if(amenities==null){
                isModularKitchen=true;
            } else
            if(!amen.isEmpty() && amen.contains("Modular Kitchen")){
                isModularKitchen = rpd.getAmenities().isModularKitchen();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isModularKitchen:: "+isModularKitchen);

            if(amenities==null){
                isPark=true;
            } else
            if(!amen.isEmpty() && amen.contains("Park")){
                isPark = rpd.getAmenities().isPark();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPark:: "+isPark);

            if(amenities==null){
                isSwimmingPool=true;
            } else
            if(!amen.isEmpty() && amen.contains("Swimming Pool")){
                isSwimmingPool = rpd.getAmenities().isSwimmingPool();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isSwimmingPool:: "+isSwimmingPool);

            if(amenities==null){
                isClubhouseCommunityCenter=true;
            } else
            if(!amen.isEmpty() && amen.contains("Clubhouse / Community Center")){
                isClubhouseCommunityCenter = rpd.getAmenities().isClubhouseCommunityCenter();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isClubhouseCommunityCenter:: "+isClubhouseCommunityCenter);

            if(amenities==null){
                isInGatedSociety=true;
            } else
            if(!amen.isEmpty() && amen.contains("In Gated Society")){
                isInGatedSociety = rpd.getAmenities().isInGatedSociety();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isInGatedSociety:: "+isInGatedSociety);

            if(amenities==null){
                isGym=true;
            } else
            if(!amen.isEmpty() && amen.contains("Gym")){
                isGym = rpd.getAmenities().isGym();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isGym:: "+isGym);

            if(amenities==null){
                isMunicipalCorporation=true;
            } else
            if(!amen.isEmpty() && amen.contains("Municipal Corporation")){
                isMunicipalCorporation = rpd.getAmenities().isMunicipalCorporation();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isMunicipalCorporation:: "+isMunicipalCorporation);
            
            if(amenities==null){
                isCornerProperty=true;
            } else
            if(!amen.isEmpty() && amen.contains("Corner Property")){
                isCornerProperty = rpd.getAmenities().isCornerProperty();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isCornerProperty:: "+isCornerProperty);

            if(amenities==null){
                amenitiesValue = true;
            }else{
                amenitiesValue = (isElevator || isGasPipeline || isEmergencyExit || isWater24x7 || isPetFriendly || isWheelchairFriendly || isVastuCompliant || isStudyRoom || isStoreRoom || isHighCeilingHeight || isPoojaRoom || isServantRoom || isModularKitchen || isPark || isSwimmingPool || isClubhouseCommunityCenter || isInGatedSociety || isGym || isMunicipalCorporation || isCornerProperty);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of amenitiesValue:: "+amenitiesValue);

            if(propertyTypeValue && preferenceValue && priceMinValue && priceMaxValue && furnishingValue && stateValue && cityValue && availabilityValue && areaMinValue && areaMaxValue && ageRangesValue &&
             amenitiesValue
            ){
                filteredList.add(rpd);
            System.out.println("---------Added into residential filtered list::::::::"+rpd.getListingId());
            }
        }
        List<ResidentialPropertyResponse> filteredResidentialDtos = ResidentialPropertyMapper.toDtoList(filteredList);
    System.out.println("Residential filteredList after transformation:::::::"+filteredResidentialDtos);
        return filteredResidentialDtos;
    }

    public List<CommercialPropertyResponse> filterPendingComProp(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<CommercialPropertyDetails> allComProp =  cpdRepo.findByAdminApprovedAndSoldAndExpired("Pending",false,false);
        List<CommercialPropertyDetails> filteredList =  new ArrayList<>();
        List<String> propTypes = propertyTypes!=null ? Arrays.asList(propertyTypes.split(",")) : new ArrayList<>();
    System.out.println("propTypes list : "+propTypes);
        //List<String> amen = amenities!=null ? Arrays.asList(amenities.split(",")) : new ArrayList<>();
        List<String> ageRan = ageRanges!=null ? Arrays.asList(ageRanges.split(",")) : new ArrayList<>();
    System.out.println("ageRanges list : "+ageRan);
        
        for(CommercialPropertyDetails cpd: allComProp){

            boolean propertyTypeValue = false;
            boolean preferenceValue = false;
            boolean priceMinValue = false;
            boolean priceMaxValue = false;
            boolean stateValue = false;
            boolean cityValue=false;
            boolean availabilityValue = false;
            boolean areaMinValue = false;
            boolean areaMaxValue = false;
            boolean ageRangesValue = false;
            boolean furnishingValue = false;
            boolean amenitiesValue = false;

            if(propertyTypes==null){
                propertyTypeValue = true;
            } else
            if(propTypes.contains(cpd.getPropertyType())){
                propertyTypeValue = propTypes.contains(cpd.getPropertyType());
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

            if(preference==null){
                preferenceValue = true;
            } else
            if(cpd.getPreference().equalsIgnoreCase(preference)){
                preferenceValue = cpd.getPreference().equalsIgnoreCase(preference);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of preferenceValue:: "+preferenceValue);
            
            if(priceMin == null){
                priceMinValue = true;
            } else
            if(cpd.getPrice()>=priceMin){
                priceMinValue = cpd.getPrice()>=priceMin;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of priceMinValue:: "+priceMinValue);

            if(priceMax==null){
                priceMaxValue = true;
            } else
            if(cpd.getPrice()<=priceMax){
                priceMaxValue = cpd.getPrice()<=priceMax;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of priceMaxValue:: "+priceMaxValue);

            if(state==null){
                stateValue = true;
            } else
            if(cpd.getState().equalsIgnoreCase(state)){
                stateValue = cpd.getState().equalsIgnoreCase(state);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of stateValue:: "+stateValue);

            if(city==null){
                cityValue = true;
            } else
            if(cpd.getCity().equalsIgnoreCase(city)){
                cityValue = cpd.getCity().equalsIgnoreCase(city);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cityValue:: "+cityValue);

            if(availability==null){
                availabilityValue = true;
            } else
            if(cpd.getAvailability().equalsIgnoreCase(availability)){
                availabilityValue = cpd.getAvailability().equalsIgnoreCase(availability);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of availabilityValue:: "+availabilityValue);

            if(areaMin==null){
                areaMinValue = true;
            } else
            if(cpd.getArea()>=Double.valueOf(areaMin)){
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cpd.getArea():: "+cpd.getArea()+" and Double.valueOf(areaMin):: "+Double.valueOf(areaMin));
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of (cpd.getArea()>=Double.valueOf(areaMin)):: "+(cpd.getArea()>=Double.valueOf(areaMin)));
                areaMinValue = cpd.getArea()>=Double.valueOf(areaMin);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of areaMinValue:: "+areaMinValue);

            if(areaMax==null){
                areaMaxValue = true;
            } else
            if(cpd.getArea()<=Double.valueOf(areaMax)){
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cpd.getArea():: "+cpd.getArea()+" and Double.valueOf(areaMax):: "+Double.valueOf(areaMax));
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of (cpd.getArea()<=Double.valueOf(areaMax):: "+(cpd.getArea()<=Double.valueOf(areaMax)));
                areaMaxValue = cpd.getArea()<=Double.valueOf(areaMax);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of areaMaxValue:: "+areaMaxValue);

            if(ageRanges==null){
                ageRangesValue = true;
            } else
            if(ageRan.contains(cpd.getAge())){
                ageRangesValue = ageRan.contains(cpd.getAge());
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of ageRangesValue:: "+ageRangesValue);

            if(furnishing==null){
                furnishingValue=true;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of furnishingValue:: "+furnishingValue);

            if(amenities==null){
                amenitiesValue=true;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of amenitiesValue:: "+amenitiesValue);

            if(propertyTypeValue && preferenceValue && priceMinValue && priceMaxValue && stateValue && cityValue && availabilityValue && areaMinValue && areaMaxValue && ageRangesValue && furnishingValue && amenitiesValue
            ){
                filteredList.add(cpd);
            System.out.println("---------Added into commercial filtered list::::::::"+cpd.getListingId());
            }
        }

        List<CommercialPropertyResponse> filteredCommercialDtos = CommercialPropertyMapper.toDtoList(filteredList);
    System.out.println("Commercial filteredList after transformation:::::::"+filteredCommercialDtos);
        return filteredCommercialDtos;
    }


    public List<ResidentialPropertyResponse> filterExpiredResProp(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<ResidentialPropertyDetails> allResProp =  rpdRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,true);
        List<ResidentialPropertyDetails> filteredList =  new ArrayList<>();
        List<String> propTypes = propertyTypes!=null ? Arrays.asList(propertyTypes.split(",")) : new ArrayList<>();
    System.out.println("propTypes list : "+propTypes);
        List<String> amen = amenities!=null ? Arrays.asList(amenities.split(",")) : new ArrayList<>();
    System.out.println("amenities list : "+amen);
        List<String> ageRan = ageRanges!=null ? Arrays.asList(ageRanges.split(",")) : new ArrayList<>();
    System.out.println("ageRanges list : "+ageRan);
        
        for(ResidentialPropertyDetails rpd: allResProp){
            boolean propertyTypeValue = false;
            boolean preferenceValue = false;
            boolean priceMinValue = false;
            boolean priceMaxValue = false;
            boolean furnishingValue = false;
            boolean stateValue = false;
            boolean cityValue=false;
            boolean availabilityValue = false;
            boolean areaMinValue = false;
            boolean areaMaxValue = false;
            boolean ageRangesValue = false;
            boolean amenitiesValue = false;

            boolean isElevator=false; 
            boolean isGasPipeline=false; 
            boolean isEmergencyExit=false; 
            boolean isWater24x7=false; 
            boolean isPetFriendly=false; 
            boolean isWheelchairFriendly=false; 
            boolean isVastuCompliant=false; 
            boolean isStudyRoom=false; 
            boolean isStoreRoom=false; 
            boolean isHighCeilingHeight=false; 
            boolean isPoojaRoom=false; 
            boolean isServantRoom=false; 
            boolean isModularKitchen=false; 
            boolean isPark=false; 
            boolean isSwimmingPool=false; 
            boolean isClubhouseCommunityCenter=false; 
            boolean isInGatedSociety=false; 
            boolean isGym=false; 
            boolean isMunicipalCorporation=false; 
            boolean isCornerProperty = false;

            if(propertyTypes==null){
                propertyTypeValue = true;
            } else
            if(propTypes.contains(rpd.getPropertyType())){
                propertyTypeValue = propTypes.contains(rpd.getPropertyType());
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

            if(preference==null){
                preferenceValue = true;
            } else
            if(rpd.getPreference().equalsIgnoreCase(preference)){
                preferenceValue = rpd.getPreference().equalsIgnoreCase(preference);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of preferenceValue:: "+preferenceValue);
            
            if(priceMin == null){
                priceMinValue = true;
            } else
            if(rpd.getPrice()>=priceMin){
                priceMinValue = rpd.getPrice()>=priceMin;
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of priceMinValue:: "+priceMinValue);

            if(priceMax==null){
                priceMaxValue = true;
            } else
            if(rpd.getPrice()<=priceMax){
                priceMaxValue = rpd.getPrice()<=priceMax;
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of priceMaxValue:: "+priceMaxValue);

            if(furnishing==null){
                furnishingValue = true;
            } else
            if(rpd.getFurnishing().equalsIgnoreCase(furnishing)){
                furnishingValue = rpd.getFurnishing().equalsIgnoreCase(furnishing);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of furnishingValue:: "+furnishingValue);

            if(state==null){
                stateValue = true;
            } else
            if(rpd.getState().equalsIgnoreCase(state)){
                stateValue = rpd.getState().equalsIgnoreCase(state);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of stateValue:: "+stateValue);

            if(city==null){
                cityValue = true;
            } else
            if(rpd.getCity().equalsIgnoreCase(city)){
                cityValue = rpd.getCity().equalsIgnoreCase(city);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of cityValue:: "+cityValue);

            if(availability==null){
                availabilityValue = true;
            } else
            if(rpd.getAvailability().equalsIgnoreCase(availability)){
                availabilityValue = rpd.getAvailability().equalsIgnoreCase(availability);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of availabilityValue:: "+availabilityValue);

            if(areaMin==null){
                areaMinValue = true;
            } else
            if(rpd.getArea()>=Double.valueOf(areaMin)){
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of rpd.getArea():: "+rpd.getArea()+" and Double.valueOf(areaMin):: "+Double.valueOf(areaMin));
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of (rpd.getArea()>=Double.valueOf(areaMin)):: "+(rpd.getArea()>=Double.valueOf(areaMin)));
                areaMinValue = rpd.getArea()>=Double.valueOf(areaMin);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of areaMinValue:: "+areaMinValue);

            if(areaMax==null){
                areaMaxValue = true;
            } else
            if(rpd.getArea()<=Double.valueOf(areaMax)){
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of rpd.getArea():: "+rpd.getArea()+" and Double.valueOf(areaMax):: "+Double.valueOf(areaMax));
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of (rpd.getArea()<=Double.valueOf(areaMax):: "+(rpd.getArea()<=Double.valueOf(areaMax)));
                areaMaxValue = rpd.getArea()<=Double.valueOf(areaMax);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of areaMaxValue:: "+areaMaxValue);

            if(ageRanges==null){
                ageRangesValue = true;
            } else
            if(ageRan.contains(rpd.getAge())){
                ageRangesValue = ageRan.contains(rpd.getAge());
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of ageRangesValue:: "+ageRangesValue);

            if(amenities==null){
                isElevator=true;
            } else
            if(!amen.isEmpty() && amen.contains("Elevator")){
                isElevator = rpd.getAmenities().isElevator();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isElevator:: "+isElevator);

            if(amenities==null){
                isGasPipeline=true;
            } else
            if(!amen.isEmpty() && amen.contains("Gas Pipeline")){
                isGasPipeline = rpd.getAmenities().isGasPipeline();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isGasPipeline:: "+isGasPipeline);

            if(amenities==null){
                isEmergencyExit=true;
            } else
            if(!amen.isEmpty() && amen.contains("Emergency Exit")){
                isEmergencyExit = rpd.getAmenities().isEmergencyExit();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isEmergencyExit:: "+isEmergencyExit);

            if(amenities==null){
                isWater24x7=true;
            } else
            if(!amen.isEmpty() && amen.contains("Water 24x7")){
                isWater24x7 = rpd.getAmenities().isWater24x7();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isWater24x7:: "+isWater24x7);

            if(amenities==null){
                isPetFriendly=true;
            } else
            if(!amen.isEmpty() && amen.contains("Pet Friendly")){
                isPetFriendly = rpd.getAmenities().isPetFriendly();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPetFriendly:: "+isPetFriendly);

            if(amenities==null){
                isWheelchairFriendly=true;
            } else
            if(!amen.isEmpty() && amen.contains("Wheelchair Friendly")){
                isWheelchairFriendly = rpd.getAmenities().isWheelchairFriendly();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isWheelchairFriendly:: "+isWheelchairFriendly);

            if(amenities==null){
                isVastuCompliant=true;
            } else
            if(!amen.isEmpty() && amen.contains("Vastu Compliant")){
                isVastuCompliant = rpd.getAmenities().isVastuCompliant();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isVastuCompliant:: "+isVastuCompliant);

            if(amenities==null){
                isStudyRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Study Room")){
                isStudyRoom = rpd.getAmenities().isStudyRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isStudyRoom:: "+isStudyRoom);

            if(amenities==null){
                isStoreRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Store Room")){
                isStoreRoom = rpd.getAmenities().isStoreRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isStoreRoom:: "+isStoreRoom);

            if(amenities==null){
                isHighCeilingHeight=true;
            } else
            if(!amen.isEmpty() && amen.contains("High Ceiling Height")){
                isHighCeilingHeight = rpd.getAmenities().isHighCeilingHeight();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isHighCeilingHeight:: "+isHighCeilingHeight);

            if(amenities==null){
                isPoojaRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Pooja Room")){
                isPoojaRoom = rpd.getAmenities().isPoojaRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPoojaRoom:: "+isPoojaRoom);

            if(amenities==null){
                isServantRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Servant Room")){
                isServantRoom = rpd.getAmenities().isServantRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isServantRoom:: "+isServantRoom);

            if(amenities==null){
                isModularKitchen=true;
            } else
            if(!amen.isEmpty() && amen.contains("Modular Kitchen")){
                isModularKitchen = rpd.getAmenities().isModularKitchen();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isModularKitchen:: "+isModularKitchen);

            if(amenities==null){
                isPark=true;
            } else
            if(!amen.isEmpty() && amen.contains("Park")){
                isPark = rpd.getAmenities().isPark();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPark:: "+isPark);

            if(amenities==null){
                isSwimmingPool=true;
            } else
            if(!amen.isEmpty() && amen.contains("Swimming Pool")){
                isSwimmingPool = rpd.getAmenities().isSwimmingPool();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isSwimmingPool:: "+isSwimmingPool);

            if(amenities==null){
                isClubhouseCommunityCenter=true;
            } else
            if(!amen.isEmpty() && amen.contains("Clubhouse / Community Center")){
                isClubhouseCommunityCenter = rpd.getAmenities().isClubhouseCommunityCenter();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isClubhouseCommunityCenter:: "+isClubhouseCommunityCenter);

            if(amenities==null){
                isInGatedSociety=true;
            } else
            if(!amen.isEmpty() && amen.contains("In Gated Society")){
                isInGatedSociety = rpd.getAmenities().isInGatedSociety();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isInGatedSociety:: "+isInGatedSociety);

            if(amenities==null){
                isGym=true;
            } else
            if(!amen.isEmpty() && amen.contains("Gym")){
                isGym = rpd.getAmenities().isGym();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isGym:: "+isGym);

            if(amenities==null){
                isMunicipalCorporation=true;
            } else
            if(!amen.isEmpty() && amen.contains("Municipal Corporation")){
                isMunicipalCorporation = rpd.getAmenities().isMunicipalCorporation();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isMunicipalCorporation:: "+isMunicipalCorporation);
            
            if(amenities==null){
                isCornerProperty=true;
            } else
            if(!amen.isEmpty() && amen.contains("Corner Property")){
                isCornerProperty = rpd.getAmenities().isCornerProperty();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isCornerProperty:: "+isCornerProperty);

            if(amenities==null){
                amenitiesValue = true;
            }else{
                amenitiesValue = (isElevator || isGasPipeline || isEmergencyExit || isWater24x7 || isPetFriendly || isWheelchairFriendly || isVastuCompliant || isStudyRoom || isStoreRoom || isHighCeilingHeight || isPoojaRoom || isServantRoom || isModularKitchen || isPark || isSwimmingPool || isClubhouseCommunityCenter || isInGatedSociety || isGym || isMunicipalCorporation || isCornerProperty);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of amenitiesValue:: "+amenitiesValue);

            if(propertyTypeValue && preferenceValue && priceMinValue && priceMaxValue && furnishingValue && stateValue && cityValue && availabilityValue && areaMinValue && areaMaxValue && ageRangesValue &&
            amenitiesValue
            ){
                filteredList.add(rpd);
            System.out.println("---------Added into residential filtered list::::::::"+rpd.getListingId());
            }
        }
        List<ResidentialPropertyResponse> filteredResidentialDtos = ResidentialPropertyMapper.toDtoList(filteredList);
    System.out.println("Residential filteredList after transformation:::::::"+filteredResidentialDtos);
        return filteredResidentialDtos;
    }

    public List<CommercialPropertyResponse> filterExpiredComProp(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<CommercialPropertyDetails> allComProp =  cpdRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,true);
        List<CommercialPropertyDetails> filteredList =  new ArrayList<>();
        List<String> propTypes = propertyTypes!=null ? Arrays.asList(propertyTypes.split(",")) : new ArrayList<>();
    System.out.println("propTypes list : "+propTypes);
        //List<String> amen = amenities!=null ? Arrays.asList(amenities.split(",")) : new ArrayList<>();
        List<String> ageRan = ageRanges!=null ? Arrays.asList(ageRanges.split(",")) : new ArrayList<>();
    System.out.println("ageRanges list : "+ageRan);
        
        for(CommercialPropertyDetails cpd: allComProp){

            boolean propertyTypeValue = false;
            boolean preferenceValue = false;
            boolean priceMinValue = false;
            boolean priceMaxValue = false;
            boolean stateValue = false;
            boolean cityValue=false;
            boolean availabilityValue = false;
            boolean areaMinValue = false;
            boolean areaMaxValue = false;
            boolean ageRangesValue = false;
            boolean furnishingValue = false;
            boolean amenitiesValue = false;

            if(propertyTypes==null){
                propertyTypeValue = true;
            } else
            if(propTypes.contains(cpd.getPropertyType())){
                propertyTypeValue = propTypes.contains(cpd.getPropertyType());
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

            if(preference==null){
                preferenceValue = true;
            } else
            if(cpd.getPreference().equalsIgnoreCase(preference)){
                preferenceValue = cpd.getPreference().equalsIgnoreCase(preference);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of preferenceValue:: "+preferenceValue);
            
            if(priceMin == null){
                priceMinValue = true;
            } else
            if(cpd.getPrice()>=priceMin){
                priceMinValue = cpd.getPrice()>=priceMin;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of priceMinValue:: "+priceMinValue);

            if(priceMax==null){
                priceMaxValue = true;
            } else
            if(cpd.getPrice()<=priceMax){
                priceMaxValue = cpd.getPrice()<=priceMax;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of priceMaxValue:: "+priceMaxValue);

            if(state==null){
                stateValue = true;
            } else
            if(cpd.getState().equalsIgnoreCase(state)){
                stateValue = cpd.getState().equalsIgnoreCase(state);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of stateValue:: "+stateValue);

            if(city==null){
                cityValue = true;
            } else
            if(cpd.getCity().equalsIgnoreCase(city)){
                cityValue = cpd.getCity().equalsIgnoreCase(city);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cityValue:: "+cityValue);

            if(availability==null){
                availabilityValue = true;
            } else
            if(cpd.getAvailability().equalsIgnoreCase(availability)){
                availabilityValue = cpd.getAvailability().equalsIgnoreCase(availability);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of availabilityValue:: "+availabilityValue);

            if(areaMin==null){
                areaMinValue = true;
            } else
            if(cpd.getArea()>=Double.valueOf(areaMin)){
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cpd.getArea():: "+cpd.getArea()+" and Double.valueOf(areaMin):: "+Double.valueOf(areaMin));
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of (cpd.getArea()>=Double.valueOf(areaMin)):: "+(cpd.getArea()>=Double.valueOf(areaMin)));
                areaMinValue = cpd.getArea()>=Double.valueOf(areaMin);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of areaMinValue:: "+areaMinValue);

            if(areaMax==null){
                areaMaxValue = true;
            } else
            if(cpd.getArea()<=Double.valueOf(areaMax)){
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cpd.getArea():: "+cpd.getArea()+" and Double.valueOf(areaMax):: "+Double.valueOf(areaMax));
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of (cpd.getArea()<=Double.valueOf(areaMax):: "+(cpd.getArea()<=Double.valueOf(areaMax)));
                areaMaxValue = cpd.getArea()<=Double.valueOf(areaMax);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of areaMaxValue:: "+areaMaxValue);

            if(ageRanges==null){
                ageRangesValue = true;
            } else
            if(ageRan.contains(cpd.getAge())){
                ageRangesValue = ageRan.contains(cpd.getAge());
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of ageRangesValue:: "+ageRangesValue);

            if(furnishing==null){
                furnishingValue=true;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of furnishingValue:: "+furnishingValue);

            if(amenities==null){
                amenitiesValue=true;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of amenitiesValue:: "+amenitiesValue);

            if(propertyTypeValue && preferenceValue && priceMinValue && priceMaxValue && stateValue && cityValue && availabilityValue && areaMinValue && areaMaxValue && ageRangesValue && furnishingValue && amenitiesValue
            ){
                filteredList.add(cpd);
            System.out.println("---------Added into commercial filtered list::::::::"+cpd.getListingId());
            }
        }

        List<CommercialPropertyResponse> filteredCommercialDtos = CommercialPropertyMapper.toDtoList(filteredList);
    System.out.println("Commercial filteredList after transformation:::::::"+filteredCommercialDtos);
        return filteredCommercialDtos;
    }


    public List<ResidentialPropertyResponse> filterVipResProp(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<ResidentialPropertyDetails> allResProp =  rpdRepo.findByAdminApprovedAndSoldAndExpiredAndVip("Approved",false,false,true);
        List<ResidentialPropertyDetails> filteredList =  new ArrayList<>();
        List<String> propTypes = propertyTypes!=null ? Arrays.asList(propertyTypes.split(",")) : new ArrayList<>();
    System.out.println("propTypes list : "+propTypes);
        List<String> amen = amenities!=null ? Arrays.asList(amenities.split(",")) : new ArrayList<>();
    System.out.println("amenities list : "+amen);
        List<String> ageRan = ageRanges!=null ? Arrays.asList(ageRanges.split(",")) : new ArrayList<>();
    System.out.println("ageRanges list : "+ageRan);
 
        for(ResidentialPropertyDetails rpd: allResProp){
            boolean propertyTypeValue = false;
            boolean preferenceValue = false;
            boolean priceMinValue = false;
            boolean priceMaxValue = false;
            boolean furnishingValue = false;
            boolean stateValue = false;
            boolean cityValue=false;
            boolean availabilityValue = false;
            boolean areaMinValue = false;
            boolean areaMaxValue = false;
            boolean ageRangesValue = false;
            boolean amenitiesValue = false;

            boolean isElevator=false; 
            boolean isGasPipeline=false; 
            boolean isEmergencyExit=false; 
            boolean isWater24x7=false; 
            boolean isPetFriendly=false; 
            boolean isWheelchairFriendly=false; 
            boolean isVastuCompliant=false; 
            boolean isStudyRoom=false; 
            boolean isStoreRoom=false; 
            boolean isHighCeilingHeight=false; 
            boolean isPoojaRoom=false; 
            boolean isServantRoom=false; 
            boolean isModularKitchen=false; 
            boolean isPark=false; 
            boolean isSwimmingPool=false; 
            boolean isClubhouseCommunityCenter=false; 
            boolean isInGatedSociety=false; 
            boolean isGym=false; 
            boolean isMunicipalCorporation=false; 
            boolean isCornerProperty = false;

            if(propertyTypes==null){
                propertyTypeValue = true;
            } else
            if(propTypes.contains(rpd.getPropertyType())){
                propertyTypeValue = propTypes.contains(rpd.getPropertyType());
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

            if(preference==null){
                preferenceValue = true;
            } else
            if(rpd.getPreference().equalsIgnoreCase(preference)){
                preferenceValue = rpd.getPreference().equalsIgnoreCase(preference);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of preferenceValue:: "+preferenceValue);
            
            if(priceMin == null){
                priceMinValue = true;
            } else
            if(rpd.getPrice()>=priceMin){
                priceMinValue = rpd.getPrice()>=priceMin;
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of priceMinValue:: "+priceMinValue);

            if(priceMax==null){
                priceMaxValue = true;
            } else
            if(rpd.getPrice()<=priceMax){
                priceMaxValue = rpd.getPrice()<=priceMax;
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of priceMaxValue:: "+priceMaxValue);

            if(furnishing==null){
                furnishingValue = true;
            } else
            if(rpd.getFurnishing().equalsIgnoreCase(furnishing)){
                furnishingValue = rpd.getFurnishing().equalsIgnoreCase(furnishing);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of furnishingValue:: "+furnishingValue);

            if(state==null){
                stateValue = true;
            } else
            if(rpd.getState().equalsIgnoreCase(state)){
                stateValue = rpd.getState().equalsIgnoreCase(state);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of stateValue:: "+stateValue);

            if(city==null){
                cityValue = true;
            } else
            if(rpd.getCity().equalsIgnoreCase(city)){
                cityValue = rpd.getCity().equalsIgnoreCase(city);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of cityValue:: "+cityValue);

            if(availability==null){
                availabilityValue = true;
            } else
            if(rpd.getAvailability().equalsIgnoreCase(availability)){
                availabilityValue = rpd.getAvailability().equalsIgnoreCase(availability);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of availabilityValue:: "+availabilityValue);

            if(areaMin==null){
                areaMinValue = true;
            } else
            if(rpd.getArea()>=Double.valueOf(areaMin)){
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of rpd.getArea():: "+rpd.getArea()+" and Double.valueOf(areaMin):: "+Double.valueOf(areaMin));
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of (rpd.getArea()>=Double.valueOf(areaMin)):: "+(rpd.getArea()>=Double.valueOf(areaMin)));
                areaMinValue = rpd.getArea()>=Double.valueOf(areaMin);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of areaMinValue:: "+areaMinValue);

            if(areaMax==null){
                areaMaxValue = true;
            } else
            if(rpd.getArea()<=Double.valueOf(areaMax)){
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of rpd.getArea():: "+rpd.getArea()+" and Double.valueOf(areaMax):: "+Double.valueOf(areaMax));
            System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of (rpd.getArea()<=Double.valueOf(areaMax):: "+(rpd.getArea()<=Double.valueOf(areaMax)));
                areaMaxValue = rpd.getArea()<=Double.valueOf(areaMax);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of areaMaxValue:: "+areaMaxValue);

            if(ageRanges==null){
                ageRangesValue = true;
            } else
            if(ageRan.contains(rpd.getAge())){
                ageRangesValue = ageRan.contains(rpd.getAge());
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of ageRangesValue:: "+ageRangesValue);

            if(amenities==null){
                isElevator=true;
            } else
            if(!amen.isEmpty() && amen.contains("Elevator")){
                isElevator = rpd.getAmenities().isElevator();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isElevator:: "+isElevator);

            if(amenities==null){
                isGasPipeline=true;
            } else
            if(!amen.isEmpty() && amen.contains("Gas Pipeline")){
                isGasPipeline = rpd.getAmenities().isGasPipeline();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isGasPipeline:: "+isGasPipeline);

            if(amenities==null){
                isEmergencyExit=true;
            } else
            if(!amen.isEmpty() && amen.contains("Emergency Exit")){
                isEmergencyExit = rpd.getAmenities().isEmergencyExit();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isEmergencyExit:: "+isEmergencyExit);

            if(amenities==null){
                isWater24x7=true;
            } else
            if(!amen.isEmpty() && amen.contains("Water 24x7")){
                isWater24x7 = rpd.getAmenities().isWater24x7();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isWater24x7:: "+isWater24x7);

            if(amenities==null){
                isPetFriendly=true;
            } else
            if(!amen.isEmpty() && amen.contains("Pet Friendly")){
                isPetFriendly = rpd.getAmenities().isPetFriendly();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPetFriendly:: "+isPetFriendly);

            if(amenities==null){
                isWheelchairFriendly=true;
            } else
            if(!amen.isEmpty() && amen.contains("Wheelchair Friendly")){
                isWheelchairFriendly = rpd.getAmenities().isWheelchairFriendly();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isWheelchairFriendly:: "+isWheelchairFriendly);

            if(amenities==null){
                isVastuCompliant=true;
            } else
            if(!amen.isEmpty() && amen.contains("Vastu Compliant")){
                isVastuCompliant = rpd.getAmenities().isVastuCompliant();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isVastuCompliant:: "+isVastuCompliant);

            if(amenities==null){
                isStudyRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Study Room")){
                isStudyRoom = rpd.getAmenities().isStudyRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isStudyRoom:: "+isStudyRoom);

            if(amenities==null){
                isStoreRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Store Room")){
                isStoreRoom = rpd.getAmenities().isStoreRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isStoreRoom:: "+isStoreRoom);

            if(amenities==null){
                isHighCeilingHeight=true;
            } else
            if(!amen.isEmpty() && amen.contains("High Ceiling Height")){
                isHighCeilingHeight = rpd.getAmenities().isHighCeilingHeight();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isHighCeilingHeight:: "+isHighCeilingHeight);

            if(amenities==null){
                isPoojaRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Pooja Room")){
                isPoojaRoom = rpd.getAmenities().isPoojaRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPoojaRoom:: "+isPoojaRoom);

            if(amenities==null){
                isServantRoom=true;
            } else
            if(!amen.isEmpty() && amen.contains("Servant Room")){
                isServantRoom = rpd.getAmenities().isServantRoom();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isServantRoom:: "+isServantRoom);

            if(amenities==null){
                isModularKitchen=true;
            } else
            if(!amen.isEmpty() && amen.contains("Modular Kitchen")){
                isModularKitchen = rpd.getAmenities().isModularKitchen();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isModularKitchen:: "+isModularKitchen);

            if(amenities==null){
                isPark=true;
            } else
            if(!amen.isEmpty() && amen.contains("Park")){
                isPark = rpd.getAmenities().isPark();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPark:: "+isPark);

            if(amenities==null){
                isSwimmingPool=true;
            } else
            if(!amen.isEmpty() && amen.contains("Swimming Pool")){
                isSwimmingPool = rpd.getAmenities().isSwimmingPool();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isSwimmingPool:: "+isSwimmingPool);

            if(amenities==null){
                isClubhouseCommunityCenter=true;
            } else
            if(!amen.isEmpty() && amen.contains("Clubhouse / Community Center")){
                isClubhouseCommunityCenter = rpd.getAmenities().isClubhouseCommunityCenter();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isClubhouseCommunityCenter:: "+isClubhouseCommunityCenter);

            if(amenities==null){
                isInGatedSociety=true;
            } else
            if(!amen.isEmpty() && amen.contains("In Gated Society")){
                isInGatedSociety = rpd.getAmenities().isInGatedSociety();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isInGatedSociety:: "+isInGatedSociety);

            if(amenities==null){
                isGym=true;
            } else
            if(!amen.isEmpty() && amen.contains("Gym")){
                isGym = rpd.getAmenities().isGym();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isGym:: "+isGym);

            if(amenities==null){
                isMunicipalCorporation=true;
            } else
            if(!amen.isEmpty() && amen.contains("Municipal Corporation")){
                isMunicipalCorporation = rpd.getAmenities().isMunicipalCorporation();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isMunicipalCorporation:: "+isMunicipalCorporation);
            
            if(amenities==null){
                isCornerProperty=true;
            } else
            if(!amen.isEmpty() && amen.contains("Corner Property")){
                isCornerProperty = rpd.getAmenities().isCornerProperty();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isCornerProperty:: "+isCornerProperty);

            if(amenities==null){
                amenitiesValue = true;
            }else{
                amenitiesValue = (isElevator || isGasPipeline || isEmergencyExit || isWater24x7 || isPetFriendly || isWheelchairFriendly || isVastuCompliant || isStudyRoom || isStoreRoom || isHighCeilingHeight || isPoojaRoom || isServantRoom || isModularKitchen || isPark || isSwimmingPool || isClubhouseCommunityCenter || isInGatedSociety || isGym || isMunicipalCorporation || isCornerProperty);
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of amenitiesValue:: "+amenitiesValue);

            if(propertyTypeValue && preferenceValue && priceMinValue && priceMaxValue && furnishingValue && stateValue && cityValue && availabilityValue && areaMinValue && areaMaxValue && ageRangesValue &&
            amenitiesValue
            ){
                filteredList.add(rpd);
            System.out.println("---------Added into residential filtered list::::::::"+rpd.getListingId());
            }
        }
        List<ResidentialPropertyResponse> filteredResidentialDtos = ResidentialPropertyMapper.toDtoList(filteredList);
    System.out.println("Residential filteredList after transformation:::::::"+filteredResidentialDtos);
        return filteredResidentialDtos;
    }

    public List<CommercialPropertyResponse> filterVipComProp(String propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, String amenities, String availability, Integer areaMin, Integer areaMax, String ageRanges){
        List<CommercialPropertyDetails> allComProp =  cpdRepo.findByAdminApprovedAndSoldAndExpiredAndVip("Approved",false,false,true);
        List<CommercialPropertyDetails> filteredList =  new ArrayList<>();
        List<String> propTypes = propertyTypes!=null ? Arrays.asList(propertyTypes.split(",")) : new ArrayList<>();
    System.out.println("propTypes list : "+propTypes);
        //List<String> amen = amenities!=null ? Arrays.asList(amenities.split(",")) : new ArrayList<>();
        List<String> ageRan = ageRanges!=null ? Arrays.asList(ageRanges.split(",")) : new ArrayList<>();
    System.out.println("ageRanges list : "+ageRan);
        
        for(CommercialPropertyDetails cpd: allComProp){

            boolean propertyTypeValue = false;
            boolean preferenceValue = false;
            boolean priceMinValue = false;
            boolean priceMaxValue = false;
            boolean stateValue = false;
            boolean cityValue=false;
            boolean availabilityValue = false;
            boolean areaMinValue = false;
            boolean areaMaxValue = false;
            boolean ageRangesValue = false;
            boolean furnishingValue = false;
            boolean amenitiesValue = false;

            if(propertyTypes==null){
                propertyTypeValue = true;
            } else
            if(propTypes.contains(cpd.getPropertyType())){
                propertyTypeValue = propTypes.contains(cpd.getPropertyType());
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

            if(preference==null){
                preferenceValue = true;
            } else
            if(cpd.getPreference().equalsIgnoreCase(preference)){
                preferenceValue = cpd.getPreference().equalsIgnoreCase(preference);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of preferenceValue:: "+preferenceValue);
            
            if(priceMin == null){
                priceMinValue = true;
            } else
            if(cpd.getPrice()>=priceMin){
                priceMinValue = cpd.getPrice()>=priceMin;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of priceMinValue:: "+priceMinValue);

            if(priceMax==null){
                priceMaxValue = true;
            } else
            if(cpd.getPrice()<=priceMax){
                priceMaxValue = cpd.getPrice()<=priceMax;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of priceMaxValue:: "+priceMaxValue);

            if(state==null){
                stateValue = true;
            } else
            if(cpd.getState().equalsIgnoreCase(state)){
                stateValue = cpd.getState().equalsIgnoreCase(state);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of stateValue:: "+stateValue);

            if(city==null){
                cityValue = true;
            } else
            if(cpd.getCity().equalsIgnoreCase(city)){
                cityValue = cpd.getCity().equalsIgnoreCase(city);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cityValue:: "+cityValue);

            if(availability==null){
                availabilityValue = true;
            } else
            if(cpd.getAvailability().equalsIgnoreCase(availability)){
                availabilityValue = cpd.getAvailability().equalsIgnoreCase(availability);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of availabilityValue:: "+availabilityValue);

            if(areaMin==null){
                areaMinValue = true;
            } else
            if(cpd.getArea()>=Double.valueOf(areaMin)){
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cpd.getArea():: "+cpd.getArea()+" and Double.valueOf(areaMin):: "+Double.valueOf(areaMin));
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of (cpd.getArea()>=Double.valueOf(areaMin)):: "+(cpd.getArea()>=Double.valueOf(areaMin)));
                areaMinValue = cpd.getArea()>=Double.valueOf(areaMin);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of areaMinValue:: "+areaMinValue);

            if(areaMax==null){
                areaMaxValue = true;
            } else
            if(cpd.getArea()<=Double.valueOf(areaMax)){
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cpd.getArea():: "+cpd.getArea()+" and Double.valueOf(areaMax):: "+Double.valueOf(areaMax));
            System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of (cpd.getArea()<=Double.valueOf(areaMax):: "+(cpd.getArea()<=Double.valueOf(areaMax)));
                areaMaxValue = cpd.getArea()<=Double.valueOf(areaMax);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of areaMaxValue:: "+areaMaxValue);

            if(ageRanges==null){
                ageRangesValue = true;
            } else
            if(ageRan.contains(cpd.getAge())){
                ageRangesValue = ageRan.contains(cpd.getAge());
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of ageRangesValue:: "+ageRangesValue);

            if(furnishing==null){
                furnishingValue=true;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of furnishingValue:: "+furnishingValue);

            if(amenities==null){
                amenitiesValue=true;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of amenitiesValue:: "+amenitiesValue);

            if(propertyTypeValue && preferenceValue && priceMinValue && priceMaxValue && stateValue && cityValue && availabilityValue && areaMinValue && areaMaxValue && ageRangesValue && furnishingValue && amenitiesValue
            ){
                filteredList.add(cpd);
            System.out.println("---------Added into commercial filtered list::::::::"+cpd.getListingId());
            }
        }

        List<CommercialPropertyResponse> filteredCommercialDtos = CommercialPropertyMapper.toDtoList(filteredList);
    System.out.println("Commercial filteredList after transformation:::::::"+filteredCommercialDtos);
        return filteredCommercialDtos;
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
        public boolean approveProperty(Integer listingId, String category) {
            if ("Residential".equalsIgnoreCase(category)) {
                Optional<ResidentialPropertyDetails> opt = rpdRepo.findById(listingId);
                if (opt.isPresent()) {
                    ResidentialPropertyDetails p = opt.get();
                    p.setAdminApproved("Approved");
                    p.setApprovedAt(OffsetDateTime.now());
                    rpdRepo.save(p);
                    NotificationDetails notification = new NotificationDetails();
                    String message = "Great news! Your listing titled "+p.getTitle()+" is live.";
                    notification.setNotificationType(NotificationType.ListingApproval);
                    notification.setNotificationMessage(message);
                    notification.setNotificationReceiverId(p.getResidentialOwner().getUserId());
                    notification.setNotificationReceiverRole(Role.AGENT);
                    notification.setNotificationSenderId(1);
                    notification.setNotificationSenderRole(Role.ADMIN);
                    notificationRepo.save(notification);
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
                    NotificationDetails notification = new NotificationDetails();
                    String message = "Great news! Your listing titled "+p.getTitle()+" is live.";
                    notification.setNotificationType(NotificationType.ListingRejection);
                    notification.setNotificationMessage(message);
                    notification.setNotificationReceiverId(p.getCommercialOwner().getUserId());
                    notification.setNotificationReceiverRole(Role.AGENT);
                    notification.setNotificationSenderId(1);
                    notification.setNotificationSenderRole(Role.ADMIN);
                    notificationRepo.save(notification);
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
        public boolean rejectProperty(Integer listingId, String category, String reason) {
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

                    NotificationDetails notification = new NotificationDetails();
                    String message = "Your listing titled- "+p.getTitle()+" was not Approved. Reason: "+reason+". Please edit and resubmit.";
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

                    NotificationDetails notification = new NotificationDetails();
                    String message = "Your listing titled- "+p.getTitle()+" was not Approved. Reason: "+reason+". Please edit and resubmit.";
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

                    return true;
                }
                return false;
            }
            return false;
        }

    // @Transactional    
    // public Object toggleAdminReject(Integer listingId, String category) {
    //     if(category.equalsIgnoreCase("Commercial") && cpdRepo.findById(listingId).isPresent()){
    //         CommercialPropertyDetails cpd = cpdRepo.findById(listingId).get();
    //         cpd.setAdminApproved("Rejected");
    //         cpdRepo.save(cpd);
    //         return cpd;
    //     }
    //     else
    //     if(category.equalsIgnoreCase("Residential") && rpdRepo.findById(listingId).isPresent()){
    //         ResidentialPropertyDetails rpd = rpdRepo.findById(listingId).get();
    //         rpd.setAdminApproved("Rejected");
    //         rpdRepo.save(rpd);
    //         return rpd;
    //     }
    //     else {
    //         return null;
    //     }
    // }

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
    public Object renewProperty(Integer listingId, String category) {
        if(category.equalsIgnoreCase("Commercial") && cpdRepo.findById(listingId).isPresent()){
            CommercialPropertyDetails cpd = cpdRepo.findById(listingId).get();
            cpd.setExpired(false);
            cpd.setAdminApproved("Pending");
            cpdRepo.save(cpd);
            NotificationDetails notification = new NotificationDetails();
            String message = "Your listing titled- "+cpd.getTitle()+" has been renewed.";
            notification.setNotificationType(NotificationType.RenewedListing);
            notification.setNotificationMessage(message);
            notification.setNotificationReceiverId(cpd.getCommercialOwner().getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);
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
            return rpd;
        }
        else {
            return null;
        }
    }

    @Transactional
    public Object notifyDealer(Integer listingId, String category) {
        if(category.equalsIgnoreCase("Commercial") && cpdRepo.findById(listingId).isPresent()){
            CommercialPropertyDetails cpd = cpdRepo.findById(listingId).get();
            NotificationDetails notification = new NotificationDetails();
            String message = "Your listing titled- "+cpd.getTitle()+" has expired. Please renew it from your Agent Dashboard.";
            notification.setNotificationMessage(message);
            notification.setNotificationType(NotificationType.ExpiredListing);
            notification.setNotificationReceiverId(cpd.getCommercialOwner().getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);
            return cpd;
        }
        else
        if(category.equalsIgnoreCase("Residential") && rpdRepo.findById(listingId).isPresent()){
            ResidentialPropertyDetails rpd = rpdRepo.findById(listingId).get();
            NotificationDetails notification = new NotificationDetails();
            notification.setNotificationType(NotificationType.ExpiredListing);
            String message = "Your listing titled- "+rpd.getTitle()+" has expired. Please renew it from your Agent Dashboard.";
            notification.setNotificationMessage(message);
            notification.setNotificationReceiverId(rpd.getResidentialOwner().getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);
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

    public List<UserResponse> getAllUsers(){
        List<Users> users = userRepo.findAll();
        List<UserResponse> userRes = new ArrayList<>();

        for(Users u : users){
            UserResponse ures = UserMapper.toDto(u);
            userRes.add(ures);
        }
        return userRes;
    }

    public List<AgentResponse> getAllSellers(){
        List<Users> users = userRepo.findSellers();
        List<AgentResponse> sellerRes = new ArrayList<>();

        for(Users u : users){
            AgentResponse sres = AgentMapper.toDto(u);
            sellerRes.add(sres);
        }
        return sellerRes;
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

    public List<AgentResponse> pendingKycUsers(){
        List<Users> users = userRepo.findUsersWithPendingKyc();
        List<AgentResponse> sellerRes = new ArrayList<>();

        for(Users u : users){
            AgentResponse sres = AgentMapper.toDto(u);
            sellerRes.add(sres);
        }
        return sellerRes;
    }

    @Transactional
    public boolean approveKyc(Integer userId){
        Users user = userRepo.findById(userId).isPresent() ? userRepo.findById(userId).get() : null;
        if(user!=null){
            user.setKycVerified(Kyc.APPROVED);
            user.setRole(Role.AGENT);
            userRepo.save(user);
            return true;
        }
        else {
            return false;
        }
    }

    @Transactional
    public boolean rejectKyc(Integer userId, String reason){
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
        metrics.put("totalEnquiries",0L);
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
        return notificationRepo.countUnreadNotifications(Role.ADMIN,1);
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
}
