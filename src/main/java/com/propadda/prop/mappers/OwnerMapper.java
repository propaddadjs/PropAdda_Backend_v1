package com.propadda.prop.mappers;

import com.propadda.prop.dto.OwnerResponse;
import com.propadda.prop.model.Users;

public class OwnerMapper {

    /**
     * Maps the Users JPA entity to the OwnerResponse DTO.
     * @param entity The Users object (commercialOwner in your main model).
     * @return The OwnerResponse DTO.
     */
    public static OwnerResponse toDto(Users entity) {
        if (entity == null) {
            return null;
        }

        OwnerResponse dto = new OwnerResponse();
        
        // Assuming your Users entity has standard getters like getUserId(), getFirstName(), etc.
        dto.setUserId(entity.getUserId()); 
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        
        return dto;
    }
}
