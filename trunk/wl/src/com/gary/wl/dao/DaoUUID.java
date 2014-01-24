package com.gary.wl.dao;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class DaoUUID implements IdentifierGenerator {

	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString().replace("-", "");
	}

}
