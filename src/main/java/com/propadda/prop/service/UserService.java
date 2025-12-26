// Author-Hemant Arora
package com.propadda.prop.service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
import com.propadda.prop.dto.AllPropertyViewResponse;
import com.propadda.prop.dto.DetailedFilterRequest;
import com.propadda.prop.dto.LoginRequest;
import com.propadda.prop.dto.LoginResponse;
import com.propadda.prop.dto.PasswordUpdateRequest;
import com.propadda.prop.dto.UserRequest;
import com.propadda.prop.dto.UserResponse;
import com.propadda.prop.enumerations.Kyc;
import com.propadda.prop.enumerations.NotificationType;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.exceptions.ResourceNotFoundException;
import com.propadda.prop.mappers.AgentMapper;
import com.propadda.prop.mappers.AllPropertyViewMapper;
import com.propadda.prop.mappers.CommercialPropertyMapper;
import com.propadda.prop.mappers.ResidentialPropertyMapper;
import com.propadda.prop.mappers.UserMapper;
import com.propadda.prop.model.AllPropertyView;
import com.propadda.prop.model.AllPropertyViewFilter;
import com.propadda.prop.model.FeedbackDetails;
import com.propadda.prop.model.HelpDetails;
import com.propadda.prop.model.NotificationDetails;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.AllPropertyViewFilterRepository;
import com.propadda.prop.repo.AllPropertyViewRepository;
import com.propadda.prop.repo.CommercialPropertyDetailsRepo;
import com.propadda.prop.repo.FeedbackDetailsRepo;
import com.propadda.prop.repo.HelpDetailsRepo;
import com.propadda.prop.repo.NotificationDetailsRepository;
import com.propadda.prop.repo.ResidentialPropertyDetailsRepo;
import com.propadda.prop.repo.UsersRepo;

import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
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

    @Autowired
    private AllPropertyViewRepository allPropertyViewRepo;

    @Autowired
    private AllPropertyViewFilterRepository allPropertyViewFilterRepo;

    @Autowired
    private AllPropertyViewMapper allPropertyViewMapper;
    
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

    public Page<AllPropertyViewResponse> getDetailedFilteredPropertiesPaged(DetailedFilterRequest filters, String search, String sortBy, int page, int size) {
        
        Sort sort = Sort.by("approvedAt").descending();

        if (null != sortBy)
            switch (sortBy) {
            case "priceAsc" -> sort = Sort.by("price").ascending();
            case "priceDesc" -> sort = Sort.by("price").descending();
            case "areaAsc" -> sort = Sort.by("area").ascending();
            case "areaDesc" -> sort = Sort.by("area").descending();
            default -> {
                   }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AllPropertyViewFilter> result =
            allPropertyViewFilterRepo.findAll(this.detailedFilter(filters, search), pageable);

        return result.map(allPropertyViewMapper::toDtoFiltered);
    }

    public Page<AllPropertyViewResponse> filterByPreferenceAndLocationPaged(
        String preference,
        String state,
        String city,
        String locality,
        String search,
        String sortBy,
        int page,
        int size
    ) {

        Sort sort = Sort.by("approvedAt").descending();

        if (sortBy != null) {
            switch (sortBy) {
                case "priceAsc" -> sort = Sort.by("price").ascending();
                case "priceDesc" -> sort = Sort.by("price").descending();
                case "areaAsc" -> sort = Sort.by("area").ascending();
                case "areaDesc" -> sort = Sort.by("area").descending();
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AllPropertyViewFilter> result =
            allPropertyViewFilterRepo.findAll(
                this.filterPreferenceAndLocation(
                    preference, state, city, locality, search
                ),
                pageable
            );

        return result.map(allPropertyViewMapper::toDtoFiltered);
    }

    public Page<AllPropertyViewResponse> filterByPlotAndLocationPaged(
        String state,
        String city,
        String locality,
        String search,
        String sortBy,
        int page,
        int size
    ) {

        Sort sort = Sort.by("approvedAt").descending();

        if (sortBy != null) {
            switch (sortBy) {
                case "priceAsc" -> sort = Sort.by("price").ascending();
                case "priceDesc" -> sort = Sort.by("price").descending();
                case "areaAsc" -> sort = Sort.by("area").ascending();
                case "areaDesc" -> sort = Sort.by("area").descending();
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AllPropertyViewFilter> result =
            allPropertyViewFilterRepo.findAll(
                this.filterPlotAndLocation(state, city, locality, search),
                pageable
            );

        return result.map(allPropertyViewMapper::toDtoFiltered);
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
        DetailedFilterRequest f,
        String search
    ) {
        return (root, query, cb) -> {

            List<Predicate> p = new ArrayList<>();

            // mandatory filters
            p.add(cb.equal(root.get("adminApproved"), "Approved"));
            p.add(cb.isFalse(root.get("expired")));
            p.add(cb.isFalse(root.get("sold")));

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

            // 🔍 search (already case-insensitive)
            if (search != null && !search.isBlank()) {
                String like = "%" + search.toLowerCase() + "%";

                p.add(
                    cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like),
                        cb.like(cb.lower(root.get("locality")), like),
                        cb.like(cb.lower(root.get("city")), like),
                        cb.like(cb.lower(root.get("state")), like)
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

    public Specification<AllPropertyViewFilter> filterPreferenceAndLocation(
        String preference,
        String state,
        String city,
        String locality,
        String search
    ) {
        return (root, query, cb) -> {

            List<Predicate> p = new ArrayList<>();

            // mandatory base filters
            p.add(cb.equal(root.get("adminApproved"), "Approved"));
            p.add(cb.isFalse(root.get("expired")));
            p.add(cb.isFalse(root.get("sold")));

            // preference (MANDATORY, case-insensitive)
            if (hasValue(preference)) {
                p.add(eqIgnoreCase(cb, root.get("preference"), preference));
            }

            // state
            if (hasValue(state)) {
                p.add(eqIgnoreCase(cb, root.get("state"), state));
            }

            // city
            if (hasValue(city)) {
                p.add(eqIgnoreCase(cb, root.get("city"), city));
            }

            // locality / nearbyPlace / title (contains, OR)
            if (hasValue(locality)) {
                String like = "%" + locality.toLowerCase() + "%";

                p.add(
                    cb.or(
                        cb.like(cb.lower(root.get("locality")), like),
                        cb.like(cb.lower(root.get("nearbyPlace")), like),
                        cb.like(cb.lower(root.get("title")), like)
                    )
                );
            }

            // search (same as other APIs)
            if (search != null && !search.isBlank()) {
                String like = "%" + search.toLowerCase() + "%";

                p.add(
                    cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like),
                        cb.like(cb.lower(root.get("address")), like),
                        cb.like(cb.lower(root.get("locality")), like),
                        cb.like(cb.lower(root.get("city")), like),
                        cb.like(cb.lower(root.get("state")), like)
                    )
                );
            }

            return cb.and(p.toArray(Predicate[]::new));
        };
    }

    public Specification<AllPropertyViewFilter> filterPlotAndLocation(
        String state,
        String city,
        String locality,
        String search
    ) {
        return (root, query, cb) -> {

            List<Predicate> p = new ArrayList<>();

            // mandatory
            p.add(cb.equal(root.get("adminApproved"), "Approved"));
            p.add(cb.isFalse(root.get("expired")));
            p.add(cb.isFalse(root.get("sold")));

            // propertyType contains 'plot' (case-insensitive)
            p.add(
                cb.like(
                    cb.lower(root.get("propertyType")),
                    "%plot%"
                )
            );

            // state
            if (hasValue(state)) {
                p.add(eqIgnoreCase(cb, root.get("state"), state));
            }

            // city
            if (hasValue(city)) {
                p.add(eqIgnoreCase(cb, root.get("city"), city));
            }

            if (hasValue(locality)) {
                String like = "%" + locality.toLowerCase() + "%";

                p.add(
                    cb.or(
                        cb.like(cb.lower(root.get("locality")), like),
                        cb.like(cb.lower(root.get("nearbyPlace")), like),
                        cb.like(cb.lower(root.get("title")), like)
                    )
                );
            }

            // search (same logic as main filter)
            if (search != null && !search.isBlank()) {
                String like = "%" + search.toLowerCase() + "%";

                p.add(
                    cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like),
                        cb.like(cb.lower(root.get("address")), like),
                        cb.like(cb.lower(root.get("locality")), like),
                        cb.like(cb.lower(root.get("city")), like),
                        cb.like(cb.lower(root.get("state")), like)
                    )
                );
            }

            return cb.and(p.toArray(Predicate[]::new));
        };
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

    public List<AllPropertyViewResponse> getVipFilterByPropertyType(
        String propertyType
    ) {

        Pageable pageable =
            PageRequest.of(
                0,
                10,
                Sort.by("approvedAt").descending()
            );

        List<AllPropertyView> result =
            allPropertyViewRepo
                .findAll(
                    this.vipByPropertyType(propertyType, null),
                    pageable
                )
                .getContent();

        return result.stream()
                    .map(allPropertyViewMapper::toDto)
                    .toList();
    }

    public List<AllPropertyViewResponse> getVipFilterByPG() {

        Pageable pageable =
            PageRequest.of(
                0,
                10,
                Sort.by("approvedAt").descending()
            );

        List<AllPropertyView> result =
            allPropertyViewRepo
                .findAll(
                    this.vipPG(null),
                    pageable
                )
                .getContent();

        return result.stream()
                    .map(allPropertyViewMapper::toDto)
                    .toList();
    }

    public Page<AllPropertyViewResponse> getVipFilterByPropertyTypePaged(
        String propertyType,
        String search,
        String sortBy,
        int page,
        int size
    ) {

        Sort sort = Sort.by("approvedAt").descending();

        if (sortBy != null) {
            switch (sortBy) {
                case "priceAsc" -> sort = Sort.by("price").ascending();
                case "priceDesc" -> sort = Sort.by("price").descending();
                case "areaAsc" -> sort = Sort.by("area").ascending();
                case "areaDesc" -> sort = Sort.by("area").descending();
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AllPropertyView> result =
            allPropertyViewRepo.findAll(
                this.vipByPropertyType(propertyType, search),
                pageable
            );

        return result.map(allPropertyViewMapper::toDto);
    }

    public Page<AllPropertyViewResponse> getVipFilterByPGPaged(
        String search,
        String sortBy,
        int page,
        int size
    ) {

        Sort sort = Sort.by("approvedAt").descending();

        if (sortBy != null) {
            switch (sortBy) {
                case "priceAsc" -> sort = Sort.by("price").ascending();
                case "priceDesc" -> sort = Sort.by("price").descending();
                case "areaAsc" -> sort = Sort.by("area").ascending();
                case "areaDesc" -> sort = Sort.by("area").descending();
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AllPropertyView> result =
            allPropertyViewRepo.findAll(
                this.vipPG(search),
                pageable
            );

        return result.map(allPropertyViewMapper::toDto);
    }

    public Specification<AllPropertyView> vipByPropertyType(
            String propertyType,
            String search
    ) {
        return (root, query, cb) -> {

            List<Predicate> p = new ArrayList<>();

            // base filters
            p.add(cb.equal(root.get("adminApproved"), "Approved"));
            p.add(cb.isFalse(root.get("expired")));
            p.add(cb.isFalse(root.get("sold")));
            p.add(cb.isTrue(root.get("vip")));

            // property type logic
            if (hasValue(propertyType)) {

                String pt = propertyType.toLowerCase();

                switch (pt) {

                    case "plot" ->
                        // plot / land
                        p.add(
                            cb.or(
                                cb.like(cb.lower(root.get("propertyType")), "%plot%"),
                                cb.like(cb.lower(root.get("propertyType")), "%land%")
                            )
                        );

                    case "flat" ->
                        // flat / apartment
                        p.add(
                            cb.or(
                                cb.like(cb.lower(root.get("propertyType")), "%flat%"),
                                cb.like(cb.lower(root.get("propertyType")), "%apartment%")
                            )
                        );

                    default ->
                        p.add(
                            cb.like(
                                cb.lower(root.get("propertyType")),
                                "%" + pt + "%"
                            )
                        );
                }
            }

            // optional search
            if (search != null && !search.isBlank()) {
                String like = "%" + search.toLowerCase() + "%";

                p.add(
                    cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like),
                        cb.like(cb.lower(root.get("locality")), like),
                        cb.like(cb.lower(root.get("city")), like),
                        cb.like(cb.lower(root.get("state")), like)
                    )
                );
            }

            return cb.and(p.toArray(Predicate[]::new));
        };
    }

    public Specification<AllPropertyView> vipPG(
        String search
    ) {
        return (root, query, cb) -> {

            List<Predicate> p = new ArrayList<>();

            // base filters
            p.add(cb.equal(root.get("adminApproved"), "Approved"));
            p.add(cb.isFalse(root.get("expired")));
            p.add(cb.isFalse(root.get("sold")));
            p.add(cb.isTrue(root.get("vip")));

            // PG preference
            p.add(eqIgnoreCase(cb, root.get("preference"), "pg"));

            // optional search
            if (search != null && !search.isBlank()) {
                String like = "%" + search.toLowerCase() + "%";

                p.add(
                    cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like),
                        cb.like(cb.lower(root.get("locality")), like),
                        cb.like(cb.lower(root.get("city")), like),
                        cb.like(cb.lower(root.get("state")), like)
                    )
                );
            }

            return cb.and(p.toArray(Predicate[]::new));
        };
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
    public Object addFeedbackFromUser(FeedbackDetails feedbackRequest, Integer userId) throws MessagingException {
         Users user = userRepo.findById(userId).isPresent() ? userRepo.findById(userId).get() : null;
        if(user!=null){
        feedbackRequest.setFeedbackUser(userRepo.findById(userId).get());
        FeedbackDetails f = feedbackRepo.save(feedbackRequest);
        //email flow
            String to = "support@propadda.in";
            String subject = "Feedback added by - "+user.getFirstName()+" "+user.getLastName();
            String htmlBody = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.5;">
                <h2 style="color: #333;">New Feedback Received</h2>
                <p><strong>Feedback Category:</strong> %s</p>
                <p><strong>Feedback Subcategory:</strong> %s</p>
                <p><strong>Feedback Message:</strong> %s</p>
                <p><strong>Rating:</strong> %s</p>
                <hr style="border: none; border-top: 1px solid #ddd;" />
                <h3 style="color: #333;">User's Details:</h3>
                <p><strong>Name:</strong> %s %s</p>
                <p><strong>Email:</strong> %s</p>
                <p><strong>Phone:</strong> %s</p>
                <p><strong>State:</strong> %s</p>
                <p><strong>City:</strong> %s</p>
                <p><strong>Role:</strong> %s</p>
                <hr style="border: none; border-top: 1px solid #ddd;" />
            </body>
            </html>
            """.formatted(
                feedbackRequest.getFeedbackCategory(),
                feedbackRequest.getFeedbackSubcategory(),
                feedbackRequest.getFeedbackDetail(),
                feedbackRequest.getRating(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getState(),
                user.getCity(),
                user.getRole().name()
            );

            mailService.sendHtml(to, subject, htmlBody);
        return f;
        }
        else {
            return null;
        }
    }

    @Transactional
    public Object addHelpRequestFromUser(HelpDetails helpRequest, Integer userId) throws MessagingException {
        Users user = userRepo.findById(userId).isPresent() ? userRepo.findById(userId).get() : null;
        if(user!=null){
        helpRequest.setHelpUser(userRepo.findById(userId).get());
        HelpDetails h = helpRepo.save(helpRequest);
        String to = "support@propadda.in";
        String subject = "Help request added by - "+user.getFirstName()+" "+user.getLastName();
        String htmlBody = """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.5;">
            <h2 style="color: #333;">New Help Request Received</h2>
            <p><strong>Help Category:</strong> %s</p>
            <p><strong>Help Subcategory:</strong> %s</p>
            <p><strong>Help Message:</strong> %s</p>
            <hr style="border: none; border-top: 1px solid #ddd;" />
            <h3 style="color: #333;">User's Details:</h3>
            <p><strong>Name:</strong> %s %s</p>
            <p><strong>Email:</strong> %s</p>
            <p><strong>Phone:</strong> %s</p>
            <p><strong>State:</strong> %s</p>
            <p><strong>City:</strong> %s</p>
            <p><strong>Role:</strong> %s</p>
            <hr style="border: none; border-top: 1px solid #ddd;" />
        </body>
        </html>
        """.formatted(
            helpRequest.getHelpCategory(),
            helpRequest.getHelpSubcategory(),
            helpRequest.getHelpDetail(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getState(),
            user.getCity(),
            user.getRole().name()
        );

        mailService.sendHtml(to, subject, htmlBody);
        return h;
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
        String toAdmin = "sales@propadda.in";
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
