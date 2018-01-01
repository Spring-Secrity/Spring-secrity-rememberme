package com.anlu.secrity.dao.impl;

import com.anlu.secrity.dao.AbstractDao;
import com.anlu.secrity.dao.UserProfileDao;
import com.anlu.secrity.model.UserProfile;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileDaoImpl extends AbstractDao<Integer,UserProfile> implements UserProfileDao {
    public List<UserProfile> findAll() {
        Criteria criteria = createEntityCriteria();
        criteria.addOrder(Order.asc("type"));
        return (List<UserProfile>) criteria.list();
    }

    public UserProfile findByType(String type) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("type", type));
        return (UserProfile) criteria.uniqueResult();
    }

    public UserProfile findById(int id) {
        return getByKey(id);
    }
}
