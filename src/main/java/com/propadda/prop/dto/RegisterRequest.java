package com.propadda.prop.dto;

import com.propadda.prop.enumerations.Role;

public class RegisterRequest {
    public String username;
    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String state;
    public String city;
    public Role role;  // BUYER or SELLER
}
