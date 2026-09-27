package com.example.E_Commerce.SecurityPac;


import com.example.E_Commerce.Entities.UserClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserDetailsImpl implements UserDetails {

    private UserClass userClass;

    public UserDetailsImpl(UserClass userClass) {
        this.userClass = userClass;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add the ROLE (e.g., ROLE_USER)
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userClass.getRole()));

        // Add all permissions (e.g., READ_PRIVILEGE, etc.)
//        for (Permissions p : customer.getRoles().getPermissionsSet()) {
//            authorities.add(new SimpleGrantedAuthority(p.name()));
//        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return userClass.getPassword();
    }

    @Override
    public String getUsername() {
        return userClass.getUserName();
    }
}
