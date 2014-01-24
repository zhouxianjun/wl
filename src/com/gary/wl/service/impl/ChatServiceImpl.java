package com.gary.wl.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gary.dao.dto.SqlWhere;
import com.gary.wl.dao.ChatDao;
import com.gary.wl.entity.Chat;
import com.gary.wl.entity.Message;
import com.gary.wl.entity.User;
import com.gary.wl.service.ChatService;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

	@Autowired
	private ChatDao chatDao;
	
	@Override
	public Serializable save(Chat chat) {
		// TODO Auto-generated method stub
		return chatDao.save(chat);
	}

	@Override
	public Chat get(String id) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("id"), id);
		return chatDao.get(where);
	}

	@Override
	public void update(Chat chat) {
		// TODO Auto-generated method stub
		chatDao.update(chat);
	}

	@Override
	public void delete(Chat chat) {
		// TODO Auto-generated method stub
		chatDao.delete(chat);
	}

	@Override
	public Chat isHave(User user, User toUser) {
		// TODO Auto-generated method stub
		List<Chat> userChats = user.getChats();
		List<Chat> toUserChats = toUser.getChats();
		for (Chat chat : userChats) {
			for (Chat tochat : toUserChats) {
				if(chat.getId().equals(tochat.getId()) && chat.getType() == Message.PRIVATE && tochat.getType() == Message.PRIVATE){
					Set<User> users = chat.getUsers();
					Set<User> toUsers = tochat.getUsers();
					if(users.size() == toUsers.size() && users.size() == 2){
						return chat;
					}
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Chat> list(String userid, int count) {
		// TODO Auto-generated method stub
		String hql = "select elements(u.chats) from User as u where u.id = :id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", userid);
		return (List<Chat>) chatDao.list(hql, params, count);
	}
	
	@Override
	public List<Chat> list(User user, int count) {
		// TODO Auto-generated method stub
		List<Chat> chats = user.getChats();
		List<Chat> list = new ArrayList<Chat>(chats);
		if(list != null && list.size() > count){
			list.subList(0, count);
		}
		return list;
	}

	public void setChatDao(ChatDao chatDao) {
		this.chatDao = chatDao;
	}
}
