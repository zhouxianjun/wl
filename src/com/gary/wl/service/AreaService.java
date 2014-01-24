package com.gary.wl.service;

import java.io.Serializable;

import com.gary.wl.entity.Area;
import com.gary.wl.entity.User;

public interface AreaService {
	Serializable save(Area area);
	
	Area get(String id);
	
	Area getByIp(String ip);
	
	void update(Area area);
	
	void delete(Area area);
	
	boolean haveUser(Area area, User user);
	
	boolean haveUser(Area area, String userid);
	
	void addUser(Area area, User user);
	
	void removeUser(Area area, User user);
	
	void removeUser(Area area, String userid);
}
