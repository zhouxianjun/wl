package com.gary.wl.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gary.dao.dto.SqlOrderBy;
import com.gary.dao.dto.SqlWhere;
import com.gary.wl.dao.GroupDao;
import com.gary.wl.entity.Group;
import com.gary.wl.entity.User;
import com.gary.wl.service.GroupService;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupDao groupDao;
	
	@Override
	public Serializable save(Group group) {
		// TODO Auto-generated method stub
		return groupDao.save(group);
	}

	@Override
	public Group get(String id) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("id"), id);
		return groupDao.get(where);
	}

	@Override
	public void update(Group group) {
		// TODO Auto-generated method stub
		groupDao.update(group);
	}

	@Override
	public void delete(Group group) {
		// TODO Auto-generated method stub
		groupDao.delete(group);
	}
	
	@Override
	public boolean haveUser(Group group, String userid) {
		// TODO Auto-generated method stub
		List<User> users = group.getUsers();
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
	public boolean haveUser(Group group, User user) {
		// TODO Auto-generated method stub
		return haveUser(group, user.getId());
	}

	@Override
	public void addUser(Group group, User user) {
		// TODO Auto-generated method stub
		if (!haveUser(group, user)) {
			group.getUsers().add(user);
			update(group);
		}
	}

	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Override
	public void removeUser(Group group, String userid) {
		// TODO Auto-generated method stub
		List<User> users = group.getUsers();
		if(users != null && users.size() > 0){
			for (User u : users) {
				if(u != null && u.getId().equals(userid)){
					users.remove(u);
					update(group);
					return;
				}
			}
		}
	}

	@Override
	public void removeUser(Group group, User user) {
		// TODO Auto-generated method stub
		removeUser(group, user.getId());
	}

	@Override
	public List<Group> list(User user) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("user_id"), user.getId());
		return groupDao.list(where, new SqlOrderBy[]{new SqlOrderBy("state", SqlOrderBy.ASC)});
	}
}
