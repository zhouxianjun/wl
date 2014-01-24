package com.gary.wl.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;

import com.gary.dao.result.Page;
import com.gary.wl.entity.User;

public interface UserService {
	Serializable save(User user);
	
	List<User> find(User user, Order order);
	
	List<User> find(User user);
	
	Page<User> find(User user, int pageSize, int page, boolean isOrder);
	
	User get(String id);
	
	User getByName(String name);
	
	User getByEmail(String email);
	
	Page<User> find(String name, String nickName, int pageSize, int page, boolean isOrder);
	
	void update(User user);
	
	void delete(User user);
	
	void init();
}
