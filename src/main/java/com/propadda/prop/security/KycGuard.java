// Author-Hemant Arora
package com.propadda.prop.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("kycGuard")
public class KycGuard {
  public boolean isApproved(Authentication auth) {
    if (auth == null || !auth.isAuthenticated()) return false;
    // Assuming your CustomUserDetails exposes kycVerified
    Object principal = auth.getPrincipal();
    if (principal instanceof com.propadda.prop.security.CustomUserDetails cud) {
      return "APPROVED".equalsIgnoreCase(cud.getKycVerified().name());
    }
    return false;
  }
}
