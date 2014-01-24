package com.gary.wl.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gary.dao.dto.SqlWhere;
import com.gary.wl.dao.AreaDao;
import com.gary.wl.entity.Area;
import com.gary.wl.entity.User;
import com.gary.wl.service.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaDao areaDao;
	
	@Override
	public Serializable save(Area area) {
		// TODO Auto-generated method stub
		return areaDao.save(area);
	}

	@Override
	public Area get(String id) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("id"), id);
		return areaDao.get(where);
	}

	@Override
	public void update(Area area) {
		// TODO Auto-generated method stub
		areaDao.update(area);
	}

	@Override
	public void delete(Area area) {
		// TODO Auto-generated method stub
		areaDao.delete(area);
	}
	
	public void setAreaDao(AreaDao areaDao) {
		this.areaDao = areaDao;
	}

	@Override
	public boolean haveUser(Area area, String userid) {
		// TODO Auto-generated method stub
		Set<User> users = area.getUsers();
		if(users != null && users.size() > 0){
			for (User user : users) {
				if(user != null && user.getId().equals(userid)){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean haveUser(Area area, User user) {
		// TODO Auto-generated method stub
		return haveUser(area, user.getId());
	}

	@Override
	public void addUser(Area area, User user) {
		// TODO Auto-generated method stub
		if (!haveUser(area, user)) {
			area.getUsers().add(user);
			update(area);
		}
	}

	@Override
	public void removeUser(Area area, String userid) {
		// TODO Auto-generated method stub
		Set<User> users = area.getUsers();
		if(users != null && users.size() > 0){
			for (User u : users) {
				if(u != null && u.getId().equals(userid)){
					users.remove(u);
					update(area);
					return;
				}
			}
		}
	}

	@Override
	public void removeUser(Area area, User user) {
		// TODO Auto-generated method stub
		removeUser(area, user.getId());
	}

	@Override
	public Area getByIp(String ip) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("ip"), ip);
		where.put(new SqlWhere("login_id"), null);
		return areaDao.get(where);
	}
}
