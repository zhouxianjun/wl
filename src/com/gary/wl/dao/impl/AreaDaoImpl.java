package com.gary.wl.dao.impl;

import org.springframework.stereotype.Repository;

import com.gary.dao.hibernate.impl.GenericDAOImpl;
import com.gary.wl.dao.AreaDao;
import com.gary.wl.entity.Area;

@Repository
public class AreaDaoImpl extends GenericDAOImpl<Area> implements AreaDao {

	@Override
	protected String getDataSource() {
		// TODO Auto-generated method stub
		return "mysql";
	}

	@Override
	protected Class<Area> getEntityClass() {
		// TODO Auto-generated method stub
		return Area.class;
	}

}
