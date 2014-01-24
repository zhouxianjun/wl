package com.gary.wl.dao.impl;

import org.springframework.stereotype.Repository;

import com.gary.dao.hibernate.impl.GenericDAOImpl;
import com.gary.wl.dao.FileSHADao;
import com.gary.wl.entity.FileSHA;

@Repository
public class FileSHADaoImpl extends GenericDAOImpl<FileSHA> implements FileSHADao {

	@Override
	protected String getDataSource() {
		// TODO Auto-generated method stub
		return "mysql";
	}

	@Override
	protected Class<FileSHA> getEntityClass() {
		// TODO Auto-generated method stub
		return FileSHA.class;
	}

}
