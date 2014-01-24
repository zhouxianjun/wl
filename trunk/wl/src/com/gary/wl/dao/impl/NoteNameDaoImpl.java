package com.gary.wl.dao.impl;

import org.springframework.stereotype.Repository;

import com.gary.dao.hibernate.impl.GenericDAOImpl;
import com.gary.wl.dao.NoteNameDao;
import com.gary.wl.entity.NoteName;

@Repository
public class NoteNameDaoImpl extends GenericDAOImpl<NoteName> implements NoteNameDao {

	@Override
	protected String getDataSource() {
		// TODO Auto-generated method stub
		return "mysql";
	}

	@Override
	protected Class<NoteName> getEntityClass() {
		// TODO Auto-generated method stub
		return NoteName.class;
	}

}
