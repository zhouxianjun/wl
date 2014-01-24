package com.gary.wl.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gary.dao.dto.SqlWhere;
import com.gary.dao.result.Page;
import com.gary.wl.dao.UserDao;
import com.gary.wl.entity.Area;
import com.gary.wl.entity.Login;
import com.gary.wl.entity.User;
import com.gary.wl.entity.UserExt;
import com.gary.wl.service.AbstractService;
import com.gary.wl.service.UserService;

@Service
@Transactional
public class UserServiceImpl extends AbstractService implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public Serializable save(User user) {
		// TODO Auto-generated method stub
		return userDao.save(user);
	}

	@Override
	public User get(String id) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("id"), id);
		return userDao.get(where);
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		userDao.update(user);
	}

	@Override
	public void delete(User user) {
		// TODO Auto-generated method stub
		userDao.delete(user);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User getByName(String name) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("name"), name);
		return userDao.get(where);
	}

	@Override
	public User getByEmail(String email) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("email"), email);
		return userDao.get(where);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> find(User user, Order order) {
		// TODO Auto-generated method stub
		Criteria criteria = userDao.getCriteria();
		Map<String, String> where = new HashMap<String, String>();
		where.put("memo", "like");
		where.put("nickName", "like");
		Area area = user.getArea();
		UserExt userExt = user.getUserExt();
		createCriteria(criteria, area, where);
		createCriteria(criteria, userExt, where);
		criteria.addOrder(order);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> find(User user) {
		Criteria criteria = userDao.getCriteria();
		Map<String, String> where = new HashMap<String, String>();
		where.put("memo", "like");
		where.put("nickName", "like");
		Area area = user.getArea();
		UserExt userExt = user.getUserExt();
		createCriteria(criteria, area, where);
		createCriteria(criteria, userExt, where);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<User> find(User user, int pageSize, int page, boolean isOrder) {
		// TODO Auto-generated method stub
		Criteria criteria = userDao.getCriteria();
		Area area = user.getArea();
		Map<String, String> where = new HashMap<String, String>();
		where.put("memo", "like");
		where.put("nickName", "like");
		UserExt userExt = user.getUserExt();
		createCriteria(criteria, area, where);
		createCriteria(criteria, userExt, where);
		int count = ((Long)(criteria.setProjection(Projections.rowCount()).uniqueResult())).intValue();
		criteria.setProjection(null);
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		if(isOrder){
			Login login = new Login();
			createCriteria(criteria, login, null);
			criteria.addOrder(Order.desc("_login.online"));
		}
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		Page<User> p = new Page<User>();
		p.setCount(count);
		p.setItems(criteria.list());
		p.setPageSize(pageSize);
		p.setPageNum(page);
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<User> find(String name, String nickName, int pageSize,
			int page, boolean isOrder) {
		// TODO Auto-generated method stub
		Criteria criteria = userDao.getCriteria();
		if(!StringUtils.isBlank(name)){
			criteria.add(Restrictions.eq("name", name));
		}
		if(!StringUtils.isBlank(nickName)){
			UserExt userExt = new UserExt();
			userExt.setNickName(nickName);
			createCriteria(criteria, userExt, null);
		}
		int count = ((Long)(criteria.setProjection(Projections.rowCount()).uniqueResult())).intValue();
		criteria.setProjection(null);
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		if(isOrder){
			Login login = new Login();
			createCriteria(criteria, login, null);
			criteria.addOrder(Order.desc("_login.online"));
		}
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		Page<User> p = new Page<User>();
		p.setCount(count);
		p.setItems(criteria.list());
		p.setPageSize(pageSize);
		p.setPageNum(page);
		return p;
	}

}
