package com.gary.wl.dao.impl;

import org.springframework.stereotype.Repository;

import com.gary.dao.hibernate.impl.GenericDAOImpl;
import com.gary.wl.dao.UserDao;
import com.gary.wl.entity.User;

@Repository
public class UserDaoImpl extends GenericDAOImpl<User> implements UserDao {

	@Override
	protected String getDataSource() {
		// TODO Auto-generated method stub
		return "mysql";
	}

	@Override
	protected Class<User> getEntityClass() {
		// TODO Auto-generated method stub
		return User.class;
	}

}
