package com.propadda.prop.mappers;

import com.propadda.prop.dto.UserResponse;
import com.propadda.prop.model.Users;

public class UserMapper {

    public static UserResponse toDto(Users entity) {
        if (entity == null) {
            return null;
        }

        UserResponse dto = new UserResponse();
        
        // Direct Field Mapping
        dto.setUserId(entity.getUserId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setState(entity.getState());
        dto.setCity(entity.getCity());
        
        // Enum to String Mapping
        // The role field is an Enum in the entity, mapped to String in the DTO.
        dto.setRole(entity.getRole() != null ? entity.getRole().name() : null);
        
        // Image URL
        dto.setProfileImageUrl(entity.getProfileImageUrl());
        
        // NOTE: Sensitive fields (password) and application-specific agent/admin fields 
        // (kycVerified, agentReraNumber, propaddaVerified, aadharUrl) 
        // are correctly omitted as they are not in the UserResponse DTO.

        return dto;
    }
}