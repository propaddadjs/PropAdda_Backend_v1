// Author-Hemant Arora
package com.propadda.prop.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.propadda.prop.enumerations.Kyc;
import com.propadda.prop.model.Users;

public class CustomUserDetails implements UserDetails {
    private final Users user;

    public CustomUserDetails(Users user) { this.user = user; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Prefix with ROLE_
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getEmail(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public Kyc getKycVerified() { return user.getKycVerified(); }
    public Users getUser() { return user; }
}
