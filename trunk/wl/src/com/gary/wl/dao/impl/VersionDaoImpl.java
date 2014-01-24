package com.gary.wl.dao.impl;

import org.springframework.stereotype.Repository;

import com.gary.dao.hibernate.impl.GenericDAOImpl;
import com.gary.wl.dao.VersionDao;
import com.gary.wl.entity.Version;

@Repository
public class VersionDaoImpl extends GenericDAOImpl<Version> implements VersionDao {
	@Override
	protected String getDataSource() {
		// TODO Auto-generated method stub
		return "mysql";
	}
	
	@Override
	protected Class<Version> getEntityClass() {
		// TODO Auto-generated method stub
		return Version.class;
	}
}
