package com.gary.wl.dao.impl;

import org.springframework.stereotype.Repository;

import com.gary.dao.hibernate.impl.GenericDAOImpl;
import com.gary.wl.dao.GroupDao;
import com.gary.wl.entity.Group;

@Repository
public class GroupDaoImpl extends GenericDAOImpl<Group> implements GroupDao {

	@Override
	protected String getDataSource() {
		// TODO Auto-generated method stub
		return "mysql";
	}

	@Override
	protected Class<Group> getEntityClass() {
		// TODO Auto-generated method stub
		return Group.class;
	}

}
