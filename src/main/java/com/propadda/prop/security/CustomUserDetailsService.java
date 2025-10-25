// Author-Hemant Arora
package com.propadda.prop.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.propadda.prop.repo.UsersRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepo repo;

    public CustomUserDetailsService(UsersRepo repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = repo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}
