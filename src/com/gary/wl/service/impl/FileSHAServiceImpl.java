package com.gary.wl.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gary.dao.dto.SqlWhere;
import com.gary.wl.dao.FileSHADao;
import com.gary.wl.entity.FileSHA;
import com.gary.wl.service.FileSHAService;

@Service
@Transactional
public class FileSHAServiceImpl implements FileSHAService {

	@Autowired
	private FileSHADao fileSHADao;
	
	@Override
	public Serializable save(FileSHA fileSHA) {
		// TODO Auto-generated method stub
		return fileSHADao.save(fileSHA);
	}

	@Override
	public void saveOrUpdate(FileSHA fileSHA) {
		// TODO Auto-generated method stub
		fileSHADao.saveOrUpdate(fileSHA);
	}
	
	@Override
	public FileSHA get(String id) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("sha"), id);
		return fileSHADao.get(where);
	}

	@Override
	public void update(FileSHA fileSHA) {
		// TODO Auto-generated method stub
		fileSHADao.update(fileSHA);
	}

	@Override
	public void delete(FileSHA fileSHA) {
		// TODO Auto-generated method stub
		fileSHADao.delete(fileSHA);
	}

	public void setFileSHADao(FileSHADao fileSHADao) {
		this.fileSHADao = fileSHADao;
	}

}
