package com.anlu.secrity.dao;

import com.anlu.secrity.model.UserProfile;

import java.util.List;

public interface UserProfileDao {
    List<UserProfile> findAll();

    UserProfile findByType(String type);

    UserProfile findById(int id);
}
