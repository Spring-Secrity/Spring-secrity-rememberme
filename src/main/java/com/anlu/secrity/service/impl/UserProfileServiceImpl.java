package com.anlu.secrity.service.impl;

import com.anlu.secrity.model.UserProfile;
import com.anlu.secrity.service.UserProfileService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserProfileServiceImpl implements UserProfileService,UserDetailsService {

    public List<UserProfile> findAll() {
        return null;
    }

    public UserProfile findByType(String type) {
        return null;
    }

    public UserProfile findById(int id) {
        return null;
    }

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
