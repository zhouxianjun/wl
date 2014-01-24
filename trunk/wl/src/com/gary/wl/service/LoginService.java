package com.gary.wl.service;

import java.io.Serializable;
import java.util.Map;

import com.gary.wl.entity.Login;

public interface LoginService {
	Serializable save(Login login);
	
	Login get(String id);
	
	void update(Login login);
	
	void delete(Login login);
	
	Map<String, Long> onlineAreaList(int type, String value);
}
