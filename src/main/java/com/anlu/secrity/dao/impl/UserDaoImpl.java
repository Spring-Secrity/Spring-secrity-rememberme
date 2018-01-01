package com.anlu.secrity.dao.impl;

import com.anlu.secrity.dao.AbstractDao;
import com.anlu.secrity.dao.UserDao;
import com.anlu.secrity.model.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {
    public void save(User user) {
        persist(user);
    }

    public User findById(int id) {
        return findById(id);
    }

    public User findBySSO(String sso) {
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("ssoId", sso));
        return (User) crit.uniqueResult();
    }
}
