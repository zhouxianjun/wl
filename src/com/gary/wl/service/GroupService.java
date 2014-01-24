package com.gary.wl.service;

import java.io.Serializable;
import java.util.List;

import com.gary.wl.entity.Group;
import com.gary.wl.entity.User;

public interface GroupService {
	Serializable save(Group group);
	
	Group get(String id);
	
	List<Group> list(User user);
	
	void update(Group group);
	
	void delete(Group group);
	
	boolean haveUser(Group group, User user);
	
	boolean haveUser(Group group, String userid);
	
	void addUser(Group group, User user);
	
	void removeUser(Group group, User user);
	
	void removeUser(Group group, String userid);
}
