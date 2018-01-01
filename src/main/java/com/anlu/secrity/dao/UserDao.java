package com.anlu.secrity.dao;

import com.anlu.secrity.model.User;

public interface UserDao {
    void save(User user);

    User findById(int id);

    User findBySSO(String sso);
}
