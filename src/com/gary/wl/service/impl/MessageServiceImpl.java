package com.gary.wl.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gary.dao.dto.SqlWhere;
import com.gary.wl.dao.MessageDao;
import com.gary.wl.entity.Chat;
import com.gary.wl.entity.Message;
import com.gary.wl.entity.User;
import com.gary.wl.service.MessageService;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;
	
	@Override
	public Serializable save(Message message) {
		// TODO Auto-generated method stub
		return messageDao.save(message);
	}

	@Override
	public Message get(String id) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("id"), id);
		return messageDao.get(where);
	}

	@Override
	public void update(Message message) {
		// TODO Auto-generated method stub
		messageDao.update(message);
	}

	@Override
	public void delete(Message message) {
		// TODO Auto-generated method stub
		messageDao.delete(message);
	}

	@Override
	public Message get(String from, String to, Integer type, Integer state) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("fromuser_id"), from);
		where.put(new SqlWhere("touser_id"), to);
		where.put(new SqlWhere("type"), type);
		where.put(new SqlWhere("state"), state);
		return messageDao.get(where);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Message> unReadList(User user, Chat chat, Integer type, int count) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from Message msg where msg.to = :userid and msg.chat = :chatid and msg.type = :type and msg.state = 0 order by msg.date asc";
		params.put("userid", user);
		params.put("chatid", chat);
		params.put("type", type);
		return (List<Message>) messageDao.list(hql, params, count);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Message> unReadList(User user, int count, String[] ids) {
		// TODO Auto-generated method stub
		String hql = "";
		if(ids != null && ids.length > 0){
			hql = "from Message msg where msg.to = :userid and msg.state = 0 and msg.id not in(:ids) order by msg.date asc";
		}else
			hql = "from Message msg where msg.to = :userid and msg.state = 0 order by msg.date asc";
		Query query = messageDao.getSession().createQuery(hql);
		query.setParameter("userid", user);
		if(ids != null && ids.length > 0){
			query.setParameterList("ids", ids);
		}
		return query.setFirstResult(0).setMaxResults(count).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Message> hisMsgList(User user, Chat chat, Message last,
			int count) {
		// TODO Auto-generated method stub
		String hql = "from Message msg where msg.to = :user and msg.chat = :chat and msg.type != 3 and msg.state = 1 and msg.date < :lastDate and msg.id != :lastId order by msg.date desc";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		params.put("chat", chat);
		params.put("lastDate", last.getDate());
		params.put("lastId", last.getId());
		return (List<Message>) messageDao.list(hql, params, count);
	}
	
	@Override
	public boolean hasHisMsg(User user, Chat chat, Message last) {
		// TODO Auto-generated method stub
		String hql = "from Message msg where msg.to = :user and msg.chat = :chat and msg.type != 3 and msg.state = 1 and msg.date < :lastDate and msg.id != :lastId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		params.put("chat", chat);
		params.put("lastDate", last.getDate());
		params.put("lastId", last.getId());
		return messageDao.getCount(hql, params) > 0;
	}
	
	@Override
	public int updates(String[] ids) {
		// TODO Auto-generated method stub
		Session session = messageDao.getSession();
		Query query = session.createQuery("update Message msg set msg.state = 1 where msg.id in(:ids)");
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}
}
