package com.gary.wl.service;

import java.io.Serializable;
import java.util.List;

import com.gary.wl.entity.Chat;
import com.gary.wl.entity.User;

public interface ChatService {
	Serializable save(Chat chat);
	
	Chat get(String id);
	
	void update(Chat chat);
	
	void delete(Chat chat);
	
	Chat isHave(User user, User toUser);
	
	List<Chat> list(String userid, int count);
	
	List<Chat> list(User user, int count);
}
