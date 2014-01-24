package com.gary.wl.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gary.dao.dto.SqlWhere;
import com.gary.wl.dao.LoginDao;
import com.gary.wl.entity.Area;
import com.gary.wl.entity.Login;
import com.gary.wl.service.AbstractService;
import com.gary.wl.service.LoginService;

@Service
@Transactional
public class LoginServiceImpl extends AbstractService implements LoginService {

	@Autowired
	private LoginDao loginDao;
	
	@Override
	public Serializable save(Login login) {
		// TODO Auto-generated method stub
		return loginDao.save(login);
	}

	@Override
	public Login get(String id) {
		// TODO Auto-generated method stub
		Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
		where.put(new SqlWhere("id"), id);
		return loginDao.get(where);
	}

	@Override
	public void update(Login login) {
		// TODO Auto-generated method stub
		loginDao.update(login);
	}

	@Override
	public void delete(Login login) {
		// TODO Auto-generated method stub
		loginDao.delete(login);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Long> onlineAreaList(int type, String value) {
		// TODO Auto-generated method stub
		Map<String, Long> onlines = new HashMap<String, Long>();
		List<Login> list = null;
		Criteria criteria = loginDao.getCriteria();
		Area area = new Area();
		switch (type) {
		case 0:
			Map<SqlWhere, Object> where = new HashMap<SqlWhere, Object>();
			where.put(new SqlWhere("online"), true);
			list = loginDao.list(where);
			if(list != null){
				onlines.put("count", (long) list.size());
				for (Login login : list) {
					area = login.getArea();
					if(area != null){
						String countrySN = area.getCountrySN();
						countrySN = countrySN == null ? "未知" : countrySN;
						Long number = onlines.get(countrySN);
						number = number == null ? 0L : number;
						onlines.put(countrySN, ++number);
					}
				}
			}
			break;
		case 1:
			area.setCountrySN(value);
			criteria.add(Restrictions.eq("online", true));
			createCriteria(criteria, area, null);
			list = criteria.list();
			if(list != null){
				onlines.put("count", (long) list.size());
				for (Login login : list) {
					area = login.getArea();
					if(area != null){
						String provinceSN = area.getProvinceSN();
						provinceSN = provinceSN == null ? "未知" : value + "_" + provinceSN.substring(0,2);
						Long number = onlines.get(provinceSN);
						number = number == null ? 0L : number;
						onlines.put(provinceSN, ++number);
					}
				}
			}
			break;
		case 2:
			String[] sz = value.split("_");
			area.setCountrySN(sz[0]);
			area.setProvinceSN(sz[1] + "0000");
			criteria.add(Restrictions.eq("online", true));
			createCriteria(criteria, area, null);
			list = criteria.list();
			if(list != null){
				onlines.put("count", (long) list.size());
				for (Login login : list) {
					area = login.getArea();
					if(area != null){
						String citySN = area.getCitySN();
						citySN = citySN == null ? "未知" : value + "_" + citySN.substring(2,4);
						Long number = onlines.get(citySN);
						number = number == null ? 0L : number;
						onlines.put(citySN, ++number);
					}
				}
			}
			break;
		default:
			break;
		}
		return onlines;
	}
	
	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}
}
