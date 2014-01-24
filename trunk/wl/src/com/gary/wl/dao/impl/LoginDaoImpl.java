package com.gary.wl.dao.impl;

import org.springframework.stereotype.Repository;

import com.gary.dao.hibernate.impl.GenericDAOImpl;
import com.gary.wl.dao.LoginDao;
import com.gary.wl.entity.Login;

@Repository
public class LoginDaoImpl extends GenericDAOImpl<Login> implements LoginDao {

	@Override
	protected String getDataSource() {
		// TODO Auto-generated method stub
		return "mysql";
	}

	@Override
	protected Class<Login> getEntityClass() {
		// TODO Auto-generated method stub
		return Login.class;
	}

}
