package com.gary.wl.service;

import java.io.Serializable;

import com.gary.wl.entity.NoteName;
import com.gary.wl.entity.User;

public interface NoteNameService {
	Serializable save(NoteName noteName);
	
	NoteName get(String id);
	
	NoteName get(User b, User tag);
	
	void update(NoteName noteName);
	
	void delete(NoteName noteName);
}
