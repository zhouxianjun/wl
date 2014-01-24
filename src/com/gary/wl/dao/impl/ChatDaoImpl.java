package com.gary.wl.dao.impl;

import org.springframework.stereotype.Repository;

import com.gary.dao.hibernate.impl.GenericDAOImpl;
import com.gary.wl.dao.ChatDao;
import com.gary.wl.entity.Chat;

@Repository
public class ChatDaoImpl extends GenericDAOImpl<Chat> implements ChatDao {

	@Override
	protected String getDataSource() {
		// TODO Auto-generated method stub
		return "mysql";
	}

	@Override
	protected Class<Chat> getEntityClass() {
		// TODO Auto-generated method stub
		return Chat.class;
	}

}
