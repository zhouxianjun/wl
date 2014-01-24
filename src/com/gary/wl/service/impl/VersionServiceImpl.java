package com.gary.wl.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gary.dao.dto.SqlWhere;
import com.gary.wl.dao.VersionDao;
import com.gary.wl.entity.Version;
import com.gary.wl.service.VersionService;

@Service
@Transactional
public class VersionServiceImpl implements VersionService {

	@Autowired
	private VersionDao versionDao;
	
	public void setVersionDao(VersionDao versionDao) {
		this.versionDao = versionDao;
	}

	@Transactional(readOnly = true)
	@Override
	public Version findByUniqueCode(String type, String version) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("type"), type);
		where.put(new SqlWhere("version"), version);
		return versionDao.get(where);
	}

	@Transactional
	@Override
	public Serializable save(Version v) {
		// TODO Auto-generated method stub
		return versionDao.save(v);
	}

	@Transactional
	@Override
	public void update(Version v) {
		// TODO Auto-generated method stub
		versionDao.update(v);
	}

	@Transactional
	@Override
	public void saveOrUpdate(Version v) {
		// TODO Auto-generated method stub
		versionDao.saveOrUpdate(v);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Version> all() {
		// TODO Auto-generated method stub
		return versionDao.list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Version> all(String type) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("type"), type);
		return versionDao.list(where);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Version now(String type) {
		return versionDao.getFirst("from Version where type = '"+type+"' order by version desc ", null);
	}
}
