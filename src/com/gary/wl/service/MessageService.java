package com.gary.wl.service;

import java.io.Serializable;
import java.util.List;

import com.gary.wl.entity.Chat;
import com.gary.wl.entity.Message;
import com.gary.wl.entity.User;

public interface MessageService {
	Serializable save(Message message);
	
	Message get(String id);
	
	Message get(String from, String to, Integer type, Integer state);
	
	List<Message> unReadList(User user, Chat chat, Integer type, int count);
	
	List<Message> unReadList(User user, int count, String[] ids);
	
	List<Message> hisMsgList(User user, Chat chat, Message last, int count);
	
	boolean hasHisMsg(User user, Chat chat, Message last);
	
	int updates(String[] ids);
	
	void update(Message message);
	
	void delete(Message message);
}
