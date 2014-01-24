package com.gary.wl.service;

import java.io.Serializable;

import com.gary.wl.entity.FileSHA;

public interface FileSHAService {
	Serializable save(FileSHA fileSHA);
	
	void saveOrUpdate(FileSHA fileSHA);
	
	FileSHA get(String id);
	
	void update(FileSHA fileSHA);
	
	void delete(FileSHA fileSHA);
}
