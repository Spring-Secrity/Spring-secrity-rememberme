package com.anlu.secrity.service;

import com.anlu.secrity.model.UserProfile;

import java.util.List;

public interface UserProfileService {
    List<UserProfile> findAll();

    UserProfile findByType(String type);

    UserProfile findById(int id);
}
