package com.gary.wl.dao.impl;

import org.springframework.stereotype.Repository;

import com.gary.dao.hibernate.impl.GenericDAOImpl;
import com.gary.wl.dao.MessageDao;
import com.gary.wl.entity.Message;

@Repository
public class MessageDaoImpl extends GenericDAOImpl<Message> implements MessageDao {

	@Override
	protected String getDataSource() {
		// TODO Auto-generated method stub
		return "mysql";
	}

	@Override
	protected Class<Message> getEntityClass() {
		// TODO Auto-generated method stub
		return Message.class;
	}

}
