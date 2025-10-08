package com.propadda.prop.mappers;

import com.propadda.prop.dto.AgentResponse;
import com.propadda.prop.model.Users;

public class AgentMapper {

    /**
     * Maps the Users JPA entity to the AgentResponse DTO.
     * @param entity The Users object.
     * @return The AgentResponse DTO.
     */
    public static AgentResponse toDto(Users entity) {
        if (entity == null) {
            return null;
        }

        AgentResponse dto = new AgentResponse();
        
        // Basic User Details
        dto.setUserId(entity.getUserId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setState(entity.getState());
        dto.setCity(entity.getCity());
        dto.setAddress(entity.getAddress());
        
        // Role and Verification Details
        // Mapping Enum to String: role and kycVerified
        dto.setRole(entity.getRole() != null ? entity.getRole().name() : null);
        dto.setKycVerified(entity.getKycVerified() != null ? entity.getKycVerified().name() : null);
        
        // Agent Specific Details
        dto.setAgentReraNumber(entity.getAgentReraNumber());
        dto.setPropaddaVerified(entity.getPropaddaVerified());
        
        // Media/Documents
        dto.setAadharUrl(entity.getAadharUrl());
        dto.setProfileImageUrl(entity.getProfileImageUrl());
        
        // NOTE: We omit the 'password' field and all @OneToMany collections 
        // as they are not present in the AgentResponse DTO.

        return dto;
    }
    
}
