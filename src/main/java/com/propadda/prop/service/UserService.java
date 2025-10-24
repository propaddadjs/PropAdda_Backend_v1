package com.propadda.prop.service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.propadda.prop.config.JwtService;
import com.propadda.prop.dto.AgentResponse;
import com.propadda.prop.dto.CommercialPropertyResponse;
import com.propadda.prop.dto.DetailedFilterRequest;
import com.propadda.prop.dto.FilterRequest;
import com.propadda.prop.dto.LoginRequest;
import com.propadda.prop.dto.LoginResponse;
import com.propadda.prop.dto.PasswordUpdateRequest;
import com.propadda.prop.dto.ResidentialPropertyResponse;
import com.propadda.prop.dto.UserRequest;
import com.propadda.prop.dto.UserResponse;
import com.propadda.prop.enumerations.Kyc;
import com.propadda.prop.enumerations.NotificationType;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.exceptions.ResourceNotFoundException;
import com.propadda.prop.mappers.AgentMapper;
import com.propadda.prop.mappers.CommercialPropertyMapper;
import com.propadda.prop.mappers.ResidentialPropertyMapper;
import com.propadda.prop.mappers.UserMapper;
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

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class UserService {
   
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
    private GcsService gcsService;

    @Autowired
    private MailSenderService mailService;
    
    @Autowired
    NotificationDetailsRepository notificationRepo;
    
    private final UsersRepo userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final MailSenderService mailSender;
    private static final Duration TOKEN_TTL = Duration.ofHours(1);
    private static final SecureRandom RNG = new SecureRandom();

    public UserService(UsersRepo repo, PasswordEncoder encoder, AuthenticationManager am, JwtService jwtService, MailSenderService mailSender) {
        this.userRepo = repo; this.encoder = encoder; this.authManager = am;
        this.jwtService = jwtService;
        this.mailSender = mailSender;
    }

    public Users registerBuyer(String firstName, String lastName, String email, String phoneNumber,
                String state, String city,
                String rawPassword) {
        if (userRepo.existsByEmail(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        Users u = new Users();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPhoneNumber(phoneNumber);
        u.setState(state);
        u.setCity(city);
        u.setPassword(encoder.encode(rawPassword));
        u.setRole(Role.BUYER); // default
        u.setKycVerified(Kyc.INAPPLICABLE);
        return userRepo.save(u);
    }

    /** Use for login (will throw if invalid) */
    public Users authenticate(String email, String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return userRepo.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    }

    public LoginResponse login(LoginRequest req) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(req.email(), req.password());
            authManager.authenticate(authToken);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        Users u = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var userDetails = org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities("ROLE_" + u.getRole().name())
                .build();

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new LoginResponse(
                accessToken,
                refreshToken,
                u.getRole().name(),
                u.getKycVerified().name(),  // Kyc enum in your model
                u.getUserId(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                (u.getProfileImageUrl()==null || u.getProfileImageUrl().isEmpty()) ? null : gcsService.generateSignedUrl(u.getProfileImageUrl())
        );
    }

    //homepage and related methods

    public Map<String, List<?>> getAllProperties() {
        List<ResidentialPropertyDetails> allResProp =  rRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,false);
           
        List<CommercialPropertyDetails> allComProp =  cRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,false);
        
        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", allResProp);
        res.put("commercial", allComProp);
        
        return res;
    }

    public Object getPropertyDetails(String category, Integer listingId) {
       if ("Commercial".equalsIgnoreCase(category)) {
            return cRepo.findById(listingId)
                          .map(CommercialPropertyMapper::toDto) 
                          .orElseThrow(() -> new ResourceNotFoundException("Commercial Property not found with ID: " + listingId));
        }
        
        if ("Residential".equalsIgnoreCase(category)) {
            return rRepo.findById(listingId)
                          .map(ResidentialPropertyMapper::toDto)
                          .orElseThrow(() -> new ResourceNotFoundException("Residential Property not found with ID: " + listingId));
        }
        throw new IllegalArgumentException("Invalid property category: " + category);   
    }


    public Map<String, List<?>> getFilteredProperties(FilterRequest filters) {
        System.out.println("Filters- category: "+filters.category+", property type:"+filters.propertyType+", preference: "+filters.preference+", state: "+filters.stateName+", city: "+filters.city);
        Map<String, List<?>> res = new HashMap<>();
        List<ResidentialPropertyResponse> resRes = getFilteredResidentialProperties(filters);
        List<CommercialPropertyResponse> comRes = getFilteredCommercialProperties(filters);
        if(filters.category.equalsIgnoreCase("residential")){
            res.put("residential", resRes);
            res.put("commercial", null);
        } else
        if(filters.category.equalsIgnoreCase("commercial")){
            res.put("residential", null);
            res.put("commercial", comRes);
        }
        else {        
            res.put("residential", resRes);
            res.put("commercial", comRes);
        }
       return res;
    }

    public List<ResidentialPropertyResponse> getFilteredResidentialProperties(FilterRequest filters){

        List<ResidentialPropertyDetails> allResProp =  rRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,false);
        List<ResidentialPropertyDetails> filteredList =  new ArrayList<>();

            for(ResidentialPropertyDetails rpd : allResProp){
                boolean propertyTypeValue = false;
                boolean preferenceValue = false;
                boolean stateValue = false;
                boolean cityValue=false;

                if(filters.propertyType==null){
                    propertyTypeValue = true;
                } else
                if(filters.propertyType.contains(rpd.getPropertyType())){
                    propertyTypeValue = filters.propertyType.contains(rpd.getPropertyType());
                }
            System.out.println("Inside user filterResProp, for listingId: "+rpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

                if(filters.preference==null || "".equals(filters.preference) || "All".equals(filters.preference)){
                    preferenceValue = true;
                } else
                if(rpd.getPreference().equalsIgnoreCase(filters.preference)){
                    preferenceValue = rpd.getPreference().equalsIgnoreCase(filters.preference);
                }
            System.out.println("Inside user filterResProp, for listingId: "+rpd.getListingId()+" value of preferenceValue:: "+preferenceValue);

                if(filters.stateName==null || "".equals(filters.stateName)){
                    stateValue = true;
                } else
                if(rpd.getState().equalsIgnoreCase(filters.stateName)){
                    stateValue = rpd.getState().equalsIgnoreCase(filters.stateName);
                }
            System.out.println("Inside user filterResProp, for listingId: "+rpd.getListingId()+" value of stateValue:: "+stateValue);

                if(filters.city==null || "".equals(filters.city)){
                    cityValue = true;
                } else
                if(rpd.getCity().equalsIgnoreCase(filters.city)){
                    cityValue = rpd.getCity().equalsIgnoreCase(filters.city);
                }
            System.out.println("Inside user filterResProp, for listingId: "+rpd.getListingId()+" value of cityValue:: "+cityValue);

                if(propertyTypeValue && preferenceValue && stateValue && cityValue)
            {
                filteredList.add(rpd);
            System.out.println("---------Added into residential filtered list::::::::"+rpd.getListingId());
            }
            }   

            List<ResidentialPropertyResponse> filteredResidentialDtos = ResidentialPropertyMapper.toDtoList(filteredList);
            System.out.println("Residential filteredList after transformation:::::::"+filteredResidentialDtos);
            return filteredResidentialDtos;
    }

    public List<CommercialPropertyResponse> getFilteredCommercialProperties(FilterRequest filters){
        List<CommercialPropertyDetails> allComProp =  cRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,false);
        List<CommercialPropertyDetails> filteredList =  new ArrayList<>();

            for(CommercialPropertyDetails cpd : allComProp){
                boolean propertyTypeValue = false;
                boolean preferenceValue = false;
                boolean stateValue = false;
                boolean cityValue=false;

                 if(filters.propertyType==null){
                    propertyTypeValue = true;
                } else
                if(filters.propertyType.contains(cpd.getPropertyType())){
                    propertyTypeValue = filters.propertyType.contains(cpd.getPropertyType());
                }
            System.out.println("Inside user filterComProp, for listingId: "+cpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

                if(filters.preference==null || "".equals(filters.preference) || "All".equals(filters.preference)){
                    preferenceValue = true;
                } else
                if(cpd.getPreference().equalsIgnoreCase(filters.preference)){
                    preferenceValue = cpd.getPreference().equalsIgnoreCase(filters.preference);
                }
            System.out.println("Inside user filterComProp, for listingId: "+cpd.getListingId()+" value of preferenceValue:: "+preferenceValue);

                if(filters.stateName==null || "".equals(filters.stateName)){
                    stateValue = true;
                } else
                if(cpd.getState().equalsIgnoreCase(filters.stateName)){
                    stateValue = cpd.getState().equalsIgnoreCase(filters.stateName);
                }
            System.out.println("Inside user filterComProp, for listingId: "+cpd.getListingId()+" value of stateValue:: "+stateValue);

                if(filters.city==null || "".equals(filters.city)){
                    cityValue = true;
                } else
                if(cpd.getCity().equalsIgnoreCase(filters.city)){
                    cityValue = cpd.getCity().equalsIgnoreCase(filters.city);
                }
            System.out.println("Inside user filterComProp, for listingId: "+cpd.getListingId()+" value of cityValue:: "+cityValue);

                if(propertyTypeValue && preferenceValue && stateValue && cityValue)
            {
                filteredList.add(cpd);
            System.out.println("---------Added into commercial filtered list::::::::"+cpd.getListingId());
            }
            }   

            List<CommercialPropertyResponse> filteredCommercialDtos = CommercialPropertyMapper.toDtoList(filteredList);
            System.out.println("Commercial filteredList after transformation:::::::"+filteredCommercialDtos);
            return filteredCommercialDtos;
    }

    public Map<String, List<?>> getDetailedFilteredProperties(DetailedFilterRequest filters) {
        System.out.println("Filters applied: "+filters.toString());
        Map<String, List<?>> res = new HashMap<>();
        List<ResidentialPropertyResponse> resRes = getDetailedFilteredResidentialProperties(filters.propertyType,filters.preference,filters.priceMin,filters.priceMax,filters.furnishing,filters.state,filters.city,filters.amenities,filters.availability,filters.areaMin,filters.areaMax,filters.age);
        List<CommercialPropertyResponse> comRes = getDetailedFilteredCommercialProperties(filters.propertyType,filters.preference,filters.priceMin,filters.priceMax,filters.furnishing,filters.state,filters.city,filters.amenities,filters.availability,filters.areaMin,filters.areaMax,filters.age);
        if(filters.category.equalsIgnoreCase("residential")){
            res.put("residential", resRes);
            res.put("commercial", null);
        } else
        if(filters.category.equalsIgnoreCase("commercial")){
            res.put("residential", null);
            res.put("commercial", comRes);
        }
        else {        
            res.put("residential", resRes);
            res.put("commercial", comRes);
        }
       return res;
    }

       public List<ResidentialPropertyResponse> getDetailedFilteredResidentialProperties(List<String> propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, List<String> amenities, String availability, Double areaMin, Double areaMax, List<String> ageRanges){
        List<ResidentialPropertyDetails> allResProp =  rRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,false);
        List<ResidentialPropertyDetails> filteredList =  new ArrayList<>();
 
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
            boolean amenitiesValue; //= false;

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

            if(propertyTypes==null || propertyTypes.isEmpty()){
                propertyTypeValue = true;
            } else
            if(propertyTypes.contains(rpd.getPropertyType())){
                propertyTypeValue = propertyTypes.contains(rpd.getPropertyType());
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

            if(preference==null || preference.equalsIgnoreCase("all") || preference.equalsIgnoreCase("")){
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

            if(furnishing==null || furnishing.equalsIgnoreCase("all") || furnishing.equalsIgnoreCase("")){
                furnishingValue = true;
            } else
            if(rpd.getFurnishing().equalsIgnoreCase(furnishing)){
                furnishingValue = rpd.getFurnishing().equalsIgnoreCase(furnishing);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of furnishingValue:: "+furnishingValue);

            if(state==null || state.equalsIgnoreCase("all") || state.equalsIgnoreCase("")){
                stateValue = true;
            } else
            if(rpd.getState().equalsIgnoreCase(state)){
                stateValue = rpd.getState().equalsIgnoreCase(state);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of stateValue:: "+stateValue);

            if(city==null || city.equalsIgnoreCase("all") || city.equalsIgnoreCase("")){
                cityValue = true;
            } else
            if(rpd.getCity().equalsIgnoreCase(city)){
                cityValue = rpd.getCity().equalsIgnoreCase(city);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of cityValue:: "+cityValue);

            if(availability==null || availability.equalsIgnoreCase("all") || availability.equalsIgnoreCase("")){
                availabilityValue = true;
            } else
            if(rpd.getAvailability().equalsIgnoreCase(availability)){
                availabilityValue = rpd.getAvailability().equalsIgnoreCase(availability);
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of availabilityValue:: "+availabilityValue);

            if(areaMin==null){
                areaMinValue = true;
            } else
            if(rpd.getArea()>=areaMin){
                areaMinValue = rpd.getArea()>=areaMin;
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of areaMinValue:: "+areaMinValue);

            if(areaMax==null){
                areaMaxValue = true;
            } else
            if(rpd.getArea()<=areaMax){
                areaMaxValue = rpd.getArea()<=areaMax;
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of areaMaxValue:: "+areaMaxValue);

            if(ageRanges==null || ageRanges.isEmpty()){
                ageRangesValue = true;
            } else
            if(ageRanges.contains(rpd.getAge())){
                ageRangesValue = ageRanges.contains(rpd.getAge());
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of ageRangesValue:: "+ageRangesValue);

            if(amenities==null || amenities.isEmpty()){
                isElevator=true;
            } else
            if(amenities.contains("Elevator")){
                isElevator = rpd.getAmenities().isElevator();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isElevator:: "+isElevator);

            if(amenities==null || amenities.isEmpty()){
                isGasPipeline=true;
            } else
            if(amenities.contains("Gas Pipeline")){
                isGasPipeline = rpd.getAmenities().isGasPipeline();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isGasPipeline:: "+isGasPipeline);

            if(amenities==null || amenities.isEmpty()){
                isEmergencyExit=true;
            } else
            if(amenities.contains("Emergency Exit")){
                isEmergencyExit = rpd.getAmenities().isEmergencyExit();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isEmergencyExit:: "+isEmergencyExit);

            if(amenities==null || amenities.isEmpty()){
                isWater24x7=true;
            } else
            if(amenities.contains("Water 24x7")){
                isWater24x7 = rpd.getAmenities().isWater24x7();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isWater24x7:: "+isWater24x7);

            if(amenities==null || amenities.isEmpty()){
                isPetFriendly=true;
            } else
            if(amenities.contains("Pet Friendly")){
                isPetFriendly = rpd.getAmenities().isPetFriendly();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPetFriendly:: "+isPetFriendly);

            if(amenities==null || amenities.isEmpty()){
                isWheelchairFriendly=true;
            } else
            if(amenities.contains("Wheelchair Friendly")){
                isWheelchairFriendly = rpd.getAmenities().isWheelchairFriendly();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isWheelchairFriendly:: "+isWheelchairFriendly);

            if(amenities==null || amenities.isEmpty()){
                isVastuCompliant=true;
            } else
            if(amenities.contains("Vastu Compliant")){
                isVastuCompliant = rpd.getAmenities().isVastuCompliant();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isVastuCompliant:: "+isVastuCompliant);

            if(amenities==null || amenities.isEmpty()){
                isStudyRoom=true;
            } else
            if(amenities.contains("Study Room")){
                isStudyRoom = rpd.getAmenities().isStudyRoom();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isStudyRoom:: "+isStudyRoom);

            if(amenities==null || amenities.isEmpty()){
                isStoreRoom=true;
            } else
            if(amenities.contains("Store Room")){
                isStoreRoom = rpd.getAmenities().isStoreRoom();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isStoreRoom:: "+isStoreRoom);

            if(amenities==null || amenities.isEmpty()){
                isHighCeilingHeight=true;
            } else
            if(amenities.contains("High Ceiling Height")){
                isHighCeilingHeight = rpd.getAmenities().isHighCeilingHeight();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isHighCeilingHeight:: "+isHighCeilingHeight);

            if(amenities==null || amenities.isEmpty()){
                isPoojaRoom=true;
            } else
            if(amenities.contains("Pooja Room")){
                isPoojaRoom = rpd.getAmenities().isPoojaRoom();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPoojaRoom:: "+isPoojaRoom);

            if(amenities==null || amenities.isEmpty()){
                isServantRoom=true;
            } else
            if(amenities.contains("Servant Room")){
                isServantRoom = rpd.getAmenities().isServantRoom();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isServantRoom:: "+isServantRoom);

            if(amenities==null || amenities.isEmpty()){
                isModularKitchen=true;
            } else
            if(amenities.contains("Modular Kitchen")){
                isModularKitchen = rpd.getAmenities().isModularKitchen();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isModularKitchen:: "+isModularKitchen);

            if(amenities==null || amenities.isEmpty()){
                isPark=true;
            } else
            if(amenities.contains("Park")){
                isPark = rpd.getAmenities().isPark();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isPark:: "+isPark);

            if(amenities==null || amenities.isEmpty()){
                isSwimmingPool=true;
            } else
            if(amenities.contains("Swimming Pool")){
                isSwimmingPool = rpd.getAmenities().isSwimmingPool();
            }
    System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isSwimmingPool:: "+isSwimmingPool);

            if(amenities==null || amenities.isEmpty()){
                isClubhouseCommunityCenter=true;
            } else
            if(amenities.contains("Clubhouse / Community Center")){
                isClubhouseCommunityCenter = rpd.getAmenities().isClubhouseCommunityCenter();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isClubhouseCommunityCenter:: "+isClubhouseCommunityCenter);

            if(amenities==null || amenities.isEmpty()){
                isInGatedSociety=true;
            } else
            if(amenities.contains("In Gated Society")){
                isInGatedSociety = rpd.getAmenities().isInGatedSociety();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isInGatedSociety:: "+isInGatedSociety);

            if(amenities==null || amenities.isEmpty()){
                isGym=true;
            } else
            if(amenities.contains("Gym")){
                isGym = rpd.getAmenities().isGym();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isGym:: "+isGym);

            if(amenities==null || amenities.isEmpty()){
                isMunicipalCorporation=true;
            } else
            if(amenities.contains("Municipal Corporation")){
                isMunicipalCorporation = rpd.getAmenities().isMunicipalCorporation();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isMunicipalCorporation:: "+isMunicipalCorporation);
            
            if(amenities==null || amenities.isEmpty()){
                isCornerProperty=true;
            } else
            if(amenities.contains("Corner Property")){
                isCornerProperty = rpd.getAmenities().isCornerProperty();
            }
        System.out.println("Inside filterResProp, for listingId: "+rpd.getListingId()+" value of isCornerProperty:: "+isCornerProperty);

            if(amenities==null || amenities.isEmpty()){
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

    public List<CommercialPropertyResponse> getDetailedFilteredCommercialProperties(List<String> propertyTypes, String preference, Integer priceMin, Integer priceMax, String furnishing, String state, String city, List<String> amenities, String availability, Double areaMin, Double areaMax, List<String> ageRanges){
        List<CommercialPropertyDetails> allComProp =  cRepo.findByAdminApprovedAndSoldAndExpired("Approved",false,false);
        List<CommercialPropertyDetails> filteredList =  new ArrayList<>();
        
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

            if(propertyTypes==null || propertyTypes.isEmpty()){
                propertyTypeValue = true;
            } else
            if(propertyTypes.contains(cpd.getPropertyType())){
                propertyTypeValue = propertyTypes.contains(cpd.getPropertyType());
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of propertyTypeValue:: "+propertyTypeValue);

            if(preference==null || preference.equalsIgnoreCase("all") || preference.equalsIgnoreCase("")){
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

            if(state==null || state.equalsIgnoreCase("all") || state.equalsIgnoreCase("")){
                stateValue = true;
            } else
            if(cpd.getState().equalsIgnoreCase(state)){
                stateValue = cpd.getState().equalsIgnoreCase(state);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of stateValue:: "+stateValue);

            if(city==null || city.equalsIgnoreCase("all") || city.equalsIgnoreCase("")){
                cityValue = true;
            } else
            if(cpd.getCity().equalsIgnoreCase(city)){
                cityValue = cpd.getCity().equalsIgnoreCase(city);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of cityValue:: "+cityValue);

            if(availability==null || availability.equalsIgnoreCase("all") || availability.equalsIgnoreCase("")){
                availabilityValue = true;
            } else
            if(cpd.getAvailability().equalsIgnoreCase(availability)){
                availabilityValue = cpd.getAvailability().equalsIgnoreCase(availability);
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of availabilityValue:: "+availabilityValue);

            if(areaMin==null){
                areaMinValue = true;
            } else
            if(cpd.getArea()>=areaMin){
                areaMinValue = cpd.getArea()>=areaMin;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of areaMinValue:: "+areaMinValue);

            if(areaMax==null){
                areaMaxValue = true;
            } else 
            if(cpd.getArea()<=areaMax){
                areaMaxValue = cpd.getArea()<=areaMax;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of areaMaxValue:: "+areaMaxValue);

            if(ageRanges==null || ageRanges.isEmpty()){
                ageRangesValue = true;
            } else
            if(ageRanges.contains(cpd.getAge())){
                ageRangesValue = ageRanges.contains(cpd.getAge());
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of ageRangesValue:: "+ageRangesValue);

            if(furnishing==null || furnishing.equalsIgnoreCase("all") || furnishing.equalsIgnoreCase("")){
                furnishingValue=true;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of furnishingValue:: "+furnishingValue);

            if(amenities==null || amenities.isEmpty()){
                amenitiesValue=true;
            }
        System.out.println("Inside filterComProp, for listingId: "+cpd.getListingId()+" value of amenitiesValue:: "+amenitiesValue);

            if(propertyTypeValue && preferenceValue && priceMinValue && priceMaxValue && stateValue && cityValue && availabilityValue && areaMinValue && areaMaxValue && ageRangesValue &&furnishingValue && amenitiesValue
            ){
                filteredList.add(cpd);
            System.out.println("---------Added into commercial filtered list::::::::"+cpd.getListingId());
            }
        }

        List<CommercialPropertyResponse> filteredCommercialDtos = CommercialPropertyMapper.toDtoList(filteredList);
    System.out.println("Commercial filteredList after transformation:::::::"+filteredCommercialDtos);
        return filteredCommercialDtos;
    }


    public Map<String, List<?>> filterByPreferenceAndLocation(String preference, String state, String city, String locality) {
        List<ResidentialPropertyDetails> allResProp =  rRepo.findByAdminApprovedAndSoldAndExpired("Approved",false, false);
           
        List<CommercialPropertyDetails> allComProp =  cRepo.findByAdminApprovedAndSoldAndExpired("Approved",false, false);

        List<ResidentialPropertyDetails> filteredRes =  new ArrayList<>();
           
        List<CommercialPropertyDetails> filteredCom =  new ArrayList<>();

        System.out.println("filterByPreferenceAndLocation ::: preference: "+preference+", state: "+state+", city: "+city+", locality: "+locality);

        for(ResidentialPropertyDetails r : allResProp){
            Boolean stateValue=false;
            Boolean cityValue=false;
            Boolean localityValue=false;

            if(state==null || state.equalsIgnoreCase("")){
                stateValue=true;
            } else
            if(r.getState().equalsIgnoreCase(state)){
                stateValue= r.getState().equalsIgnoreCase(state);
            }

                if(city==null || city.equalsIgnoreCase("")){
                    cityValue=true;
                } else
                if(r.getCity().equalsIgnoreCase(city)){
                    cityValue= r.getCity().equalsIgnoreCase(city);
                }

            if(locality==null || locality.equalsIgnoreCase("")){
                localityValue=true;
            } else
            if(r.getLocality().toLowerCase().contains(locality.toLowerCase()) || 
            r.getNearbyPlace().toLowerCase().contains(locality.toLowerCase()) ||
            r.getTitle().toLowerCase().contains(locality.toLowerCase())
            ){
                localityValue= r.getLocality().toLowerCase().contains(locality.toLowerCase()) || 
            r.getNearbyPlace().toLowerCase().contains(locality.toLowerCase()) ||
            r.getTitle().toLowerCase().contains(locality.toLowerCase());
            }

        if(r.getPreference().equalsIgnoreCase(preference) && stateValue && cityValue && localityValue){
            filteredRes.add(r);
        }

        }

        for(CommercialPropertyDetails c: allComProp){

            Boolean stateValue=false;
            Boolean cityValue=false;
            Boolean localityValue=false;

            if(state==null || state.equalsIgnoreCase("")){
                stateValue=true;
            } else
            if(c.getState().equalsIgnoreCase(state)){
                stateValue= c.getState().equalsIgnoreCase(state);
            }

                if(city==null || city.equalsIgnoreCase("")){
                    cityValue=true;
                } else
                if(c.getCity().equalsIgnoreCase(city)){
                    cityValue= c.getCity().equalsIgnoreCase(city);
                }

            if(locality==null || locality.equalsIgnoreCase("")){
                localityValue=true;
            } else
            if(c.getLocality().toLowerCase().contains(locality.toLowerCase()) || 
            c.getNearbyPlace().toLowerCase().contains(locality.toLowerCase()) ||
            c.getTitle().toLowerCase().contains(locality.toLowerCase())){
                localityValue= c.getLocality().toLowerCase().contains(locality.toLowerCase()) || 
            c.getNearbyPlace().toLowerCase().contains(locality.toLowerCase()) ||
            c.getTitle().toLowerCase().contains(locality.toLowerCase());
            }

                if(c.getPreference().equalsIgnoreCase(preference) && stateValue && cityValue && localityValue){
                    filteredCom.add(c);
                }
        }

        List<ResidentialPropertyResponse> filteredResidentialDtos = ResidentialPropertyMapper.toDtoList(filteredRes);
        List<CommercialPropertyResponse> filteredCommercialDtos = CommercialPropertyMapper.toDtoList(filteredCom);
        
        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", filteredResidentialDtos);
        res.put("commercial", filteredCommercialDtos);
        
        return res;
    }

    public Map<String, List<?>> filterByPlotAndLocation(String state, String city, String locality) {

        List<ResidentialPropertyDetails> allResProp =  rRepo.findByAdminApprovedAndSoldAndExpired("Approved",false, false);
           
        List<CommercialPropertyDetails> allComProp =  cRepo.findByAdminApprovedAndSoldAndExpired("Approved",false, false);

        List<ResidentialPropertyDetails> filteredRes =  new ArrayList<>();
           
        List<CommercialPropertyDetails> filteredCom =  new ArrayList<>();

        System.out.println("filterByPlotAndLocation ::: state: "+state+", city: "+city+", locality: "+locality);

        for(ResidentialPropertyDetails r : allResProp){
            Boolean stateValue=false;
            Boolean cityValue=false;
            Boolean localityValue=false;

            if(state==null || state.equalsIgnoreCase("")){
                stateValue=true;
            } else
            if(r.getState().equalsIgnoreCase(state)){
                stateValue= r.getState().equalsIgnoreCase(state);
            }

                if(city==null || city.equalsIgnoreCase("")){
                    cityValue=true;
                } else
                if(r.getCity().equalsIgnoreCase(city)){
                    cityValue= r.getCity().equalsIgnoreCase(city);
                }

            if(locality==null || locality.equalsIgnoreCase("")){
                localityValue=true;
            } else
            if(r.getLocality().toLowerCase().contains(locality.toLowerCase())){
                localityValue= r.getLocality().toLowerCase().contains(locality.toLowerCase());
            }

                if(r.getPropertyType().toLowerCase().contains("plot") && stateValue && cityValue && localityValue){
                    filteredRes.add(r);
                }

        }

        for(CommercialPropertyDetails c: allComProp){

            Boolean stateValue=false;
            Boolean cityValue=false;
            Boolean localityValue=false;

            if(state==null || state.equalsIgnoreCase("")){
                stateValue=true;
            } else
            if(c.getState().equalsIgnoreCase(state)){
                stateValue= c.getState().equalsIgnoreCase(state);
            }

                if(city==null || city.equalsIgnoreCase("")){
                    cityValue=true;
                } else
                if(c.getCity().equalsIgnoreCase(city)){
                    cityValue= c.getCity().equalsIgnoreCase(city);
                }

            if(locality==null || locality.equalsIgnoreCase("")){
                localityValue=true;
            } else
            if(c.getLocality().toLowerCase().contains(locality.toLowerCase())){
                localityValue= c.getLocality().toLowerCase().contains(locality.toLowerCase());
            }

                if(c.getPropertyType().toLowerCase().contains("plot") && stateValue && cityValue && localityValue){
                    filteredCom.add(c);
                }
        }

        List<ResidentialPropertyResponse> filteredResidentialDtos = ResidentialPropertyMapper.toDtoList(filteredRes);
        List<CommercialPropertyResponse> filteredCommercialDtos = CommercialPropertyMapper.toDtoList(filteredCom);
        
        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", filteredResidentialDtos);
        res.put("commercial", filteredCommercialDtos);
        
        return res;
    }

    public Map<String, Integer> getCountByCity() {
        
        Map<String, Integer> res = new HashMap<>();
        res.put("Bengaluru",rRepo.countByCity("Bengaluru")+cRepo.countByCity("Bengaluru"));
        res.put("Gurgaon",rRepo.countByCity("Gurgaon")+cRepo.countByCity("Gurgaon"));
        res.put("New Delhi",rRepo.countByState("Delhi")+cRepo.countByCity("Delhi"));
        res.put("Mumbai",rRepo.countByCity("Mumbai")+cRepo.countByCity("Mumbai"));
        res.put("Pune",rRepo.countByCity("Pune")+cRepo.countByCity("Pune"));
        res.put("Lucknow",rRepo.countByCity("Lucknow")+cRepo.countByCity("Lucknow"));
        res.put("Kolkata",rRepo.countByCity("Kolkata")+cRepo.countByCity("Kolkata"));
        res.put("Ahmedabad",rRepo.countByCity("Ahmedabad")+cRepo.countByCity("Ahmedabad"));
        res.put("Noida",rRepo.countByCity("Noida")+cRepo.countByCity("Noida"));
        res.put("Chennai",rRepo.countByCity("Chennai")+cRepo.countByCity("Chennai"));
        res.put("Hyderabad",rRepo.countByCity("Hyderabad")+cRepo.countByCity("Hyderabad"));
        res.put("Jaipur",rRepo.countByCity("Jaipur")+cRepo.countByCity("Jaipur"));        
        
        return res;
    }

    public Map<String, List<?>> filterByCity(String city) {
            List<ResidentialPropertyDetails> allResProp;
            List<CommercialPropertyDetails> allComProp;

        if(city.equalsIgnoreCase("New Delhi")){
            allResProp = rRepo.filterByState("Delhi");
            allComProp = cRepo.filterByState("Delhi");
        } else {
            allResProp = rRepo.filterByCity(city);
            allComProp = cRepo.filterByCity(city);
        }
        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", ResidentialPropertyMapper.toDtoList(allResProp));
        res.put("commercial", CommercialPropertyMapper.toDtoList(allComProp));
        
        return res;
    }

    public Map<String, List<?>> getVipFilterByPropertyType(String propertyType) {
        List<ResidentialPropertyDetails> allResProp = new ArrayList<>();
        List<CommercialPropertyDetails> allComProp = new ArrayList<>();
        if(propertyType.equalsIgnoreCase("plot")){
            propertyType="plot/land";
        }
        System.out.println("getVipFilterByPropertyType::: propertyType: "+propertyType);
        if(propertyType.equalsIgnoreCase("flat")){
            allResProp.addAll(rRepo.getVipFilterByPropertyType("apartment"));
            allResProp.addAll(rRepo.getVipFilterByPropertyType("flat"));
            allComProp.addAll(cRepo.getVipFilterByPropertyType("apartment"));
            allComProp.addAll(cRepo.getVipFilterByPropertyType("flat"));
        }else{
            allResProp.addAll(rRepo.getVipFilterByPropertyType(propertyType));
           allComProp.addAll(cRepo.getVipFilterByPropertyType(propertyType));
        }
        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", ResidentialPropertyMapper.toDtoList(allResProp));
        res.put("commercial", CommercialPropertyMapper.toDtoList(allComProp));
        
        return res;
    }

    public Map<String, List<?>> getNewlyLaunchedProperties() {

        OffsetDateTime cutoff = OffsetDateTime.now().minusDays(10);

        List<ResidentialPropertyDetails> allResProp =  rRepo.findByApprovedAtAfterAndSold(cutoff,false);
           
        List<CommercialPropertyDetails> allComProp =  cRepo.findByApprovedAtAfterAndSold(cutoff,false);
        
        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", allResProp);
        res.put("commercial", allComProp);
        
        return res;
    }

    public Map<String, List<?>> getVipFilterByPG() {
        List<ResidentialPropertyDetails> allResProp =  rRepo.getVipFilterByPG();
           
        List<CommercialPropertyDetails> allComProp =  cRepo.getVipFilterByPG();
        
        Map<String, List<?>> res = new HashMap<>();
        
        res.put("residential", ResidentialPropertyMapper.toDtoList(allResProp));
        res.put("commercial", CommercialPropertyMapper.toDtoList(allComProp));
        
        return res;
    }

    public UserResponse getUserDetails(Integer userId) {
        UserResponse b = new UserResponse();
        if(userRepo.findById(userId).isPresent()){
            b = UserMapper.toDto(userRepo.findById(userId).get(),gcsService);
        } 
        return b;
    }

    @Transactional
    public UserResponse updateUserDetails(Integer userId, UserRequest userDetails, MultipartFile profileImage) throws IOException {
         Users u = userRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
         u.setFirstName(userDetails.getFirstName());
         u.setLastName(userDetails.getLastName());
         u.setPhoneNumber(userDetails.getPhoneNumber());
         u.setState(userDetails.getState());
         u.setCity(userDetails.getCity());
         if(profileImage!=null && !profileImage.isEmpty()){
            u.setProfileImageUrl(gcsService.uploadKYCFiles(profileImage, "profileImage"));
         }
           UserResponse b = UserMapper.toDto(u,gcsService);
            userRepo.save(u);
        return b;
    }

    public Map<String, Long> getUserDashboardMetrics(Integer userId) {
        Map<String, Long> metrics = new HashMap<>();
        metrics.put("totalUsers",userRepo.count());
        metrics.put("totalSellers",userRepo.countByRole(Role.AGENT));
        return metrics; 
    }

    @Transactional
    public UserResponse updateUserPassword(Integer userId, PasswordUpdateRequest passwordRequest) {
        Users user = userRepo.findById(userId).isPresent() ? userRepo.findById(userId).get() : null;
        if(user!=null){
            if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New passwords do not match.");
            }
             if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid current password provided.");
            }
            String newHashedPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
            user.setPassword(newHashedPassword);
            Users updatedUser = userRepo.save(user);
            return UserMapper.toDto(updatedUser,gcsService);
        } else {
            return null;
        }
    }

    @Transactional
    public Object addFeedbackFromUser(FeedbackDetails feedbackRequest, Integer userId) {
         Users user = userRepo.findById(userId).isPresent() ? userRepo.findById(userId).get() : null;
        if(user!=null){
        feedbackRequest.setFeedbackUser(userRepo.findById(userId).get());
        return feedbackRepo.save(feedbackRequest);
        }
        else {
            return null;
        }
    }

    @Transactional
    public Object addHelpRequestFromUser(HelpDetails helpRequest, Integer userId) {
        Users user = userRepo.findById(userId).isPresent() ? userRepo.findById(userId).get() : null;
        if(user!=null){
        helpRequest.setHelpUser(userRepo.findById(userId).get());
        return helpRepo.save(helpRequest);
        }
        else {
            return null;
        }        
    }

    @Transactional
    public void initiateKyc(String email, String address, String reraNumber,
                            MultipartFile profileImage, MultipartFile aadhar) throws IOException, MessagingException {
        if (address == null || address.isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required");
        if (aadhar == null || aadhar.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aadhar file is required");

        Users u = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String aadharUrl = gcsService.uploadKYCFiles(aadhar,"aadhar"); // gs://... -> https URL if you map it
        String profileUrl = (profileImage != null && !profileImage.isEmpty())
                ? gcsService.uploadKYCFiles(profileImage,"profileImage")
                : null;

        u.setAddress(address);
        u.setAgentReraNumber(reraNumber);
        u.setAadharUrl(aadharUrl);
        if (profileUrl != null) u.setProfileImageUrl(profileUrl);

        // u.setRole(Role.AGENT);                 // becomes agent
        u.setKycVerified(Kyc.PENDING);         // pending approval
        userRepo.save(u);

        //notification flow for admin
        NotificationDetails notification = new NotificationDetails();
        String message = "User - "+u.getEmail()+" added KYC details. Approve/Reject";
        notification.setNotificationType(NotificationType.KycApprovalRequest);
        notification.setNotificationMessage(message);
        notification.setNotificationReceiverId(1);
        notification.setNotificationReceiverRole(Role.ADMIN);
        notification.setNotificationSenderId(u.getUserId());
        notification.setNotificationSenderRole(Role.AGENT);
        notificationRepo.save(notification);

        //email flow for admin
        String toAdmin = "propaddadjs@gmail.com";
        String subjectAdmin = "KYC Request";
        String bodyAdmin = "User - "+u.getEmail()+" added KYC details. Approve/Reject";
        mailService.send(toAdmin, subjectAdmin, bodyAdmin);
    }

        @Transactional
        public void updateKycDetails(String email, String address, String reraNumber, MultipartFile profileImage,
            MultipartFile aadhar) throws IOException {
        if (address == null || address.isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required");
        if (aadhar == null || aadhar.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aadhar file is required");

        Users u = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String aadharUrl = u.getAadharUrl();
        if(!aadhar.isEmpty()){
           aadharUrl = gcsService.uploadKYCFiles(aadhar,"aadhar"); // gs://... -> https URL if you map it
        }
        
        String profileUrl = (profileImage != null && !profileImage.isEmpty())
                ? gcsService.uploadKYCFiles(profileImage,"profileImage")
                : u.getProfileImageUrl();

        u.setAddress(address);
        u.setAgentReraNumber(reraNumber);
        u.setAadharUrl(aadharUrl);
        if (profileUrl != null) u.setProfileImageUrl(profileUrl);

        // u.setRole(Role.AGENT);                 // becomes agent
        u.setKycVerified(Kyc.PENDING);         // pending approval
        userRepo.save(u);
    }

    
    public AgentResponse getStatus(String email) {
        Users u = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return AgentMapper.toDto(u,gcsService);
    }

    @Transactional
    public void sendResetLink(String email, String appBaseUrl) {
        userRepo.findByEmailIgnoreCase(email).ifPresent(user -> {
            // Generate a strong random token
            byte[] bytes = new byte[32];
            RNG.nextBytes(bytes);
            String random = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            String token = random + "." + (Instant.now().plus(TOKEN_TTL).toEpochMilli());
            user.setResetToken(token);

            user.setResetToken(token);
            userRepo.save(user);

            String link = appBaseUrl + "/reset-password?token=" + token;
            try {
                mailSender.send(
                        user.getEmail(),
                        "Password Reset",
                        "Click the link to reset your password (valid for 1 hour): " + link
                );
            } catch (MessagingException ex) {
            }
        });
        // Always return 200 to avoid email enumeration
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        Users user = userRepo.findByResetToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset link"));

        String[] parts = token.split("\\.");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid link");
        long exp = Long.parseLong(parts[1]);
        if (Instant.now().toEpochMilli() > exp) throw new IllegalArgumentException("Expired link");

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepo.save(user);
    }
}
