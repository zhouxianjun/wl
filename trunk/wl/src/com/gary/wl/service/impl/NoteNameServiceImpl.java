package com.gary.wl.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gary.dao.dto.SqlWhere;
import com.gary.wl.dao.NoteNameDao;
import com.gary.wl.entity.NoteName;
import com.gary.wl.entity.User;
import com.gary.wl.service.AbstractService;
import com.gary.wl.service.NoteNameService;

@Service
@Transactional
public class NoteNameServiceImpl extends AbstractService implements NoteNameService {

	@Autowired
	private NoteNameDao noteNameDao;
	
	@Override
	public Serializable save(NoteName noteName) {
		// TODO Auto-generated method stub
		return noteNameDao.save(noteName);
	}

	@Override
	public NoteName get(String id) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("id"), id);
		return noteNameDao.get(where);
	}

	@Override
	public void update(NoteName noteName) {
		// TODO Auto-generated method stub
		noteNameDao.update(noteName);
	}

	@Override
	public void delete(NoteName noteName) {
		// TODO Auto-generated method stub
		noteNameDao.delete(noteName);
	}
	
	@Override
	public NoteName get(User b, User tag) {
		// TODO Auto-generated method stub
		NoteName noteName = new NoteName();
		noteName.setBelongs(b);
		noteName.setTarget(tag);
		List<?> list = noteNameDao.findByEntity(noteName);
		return list != null && list.size() > 0 ? (NoteName)list.get(0) : null;
	}

	public void setNoteNameDao(NoteNameDao noteNameDao) {
		this.noteNameDao = noteNameDao;
	}
}
