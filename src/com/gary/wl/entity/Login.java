package com.gary.wl.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import jsondate.DateJsonFull;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "wl_login")
public class Login {
	private String id;
	
	private Boolean online;
	
	private Date lastLogin;
	
	private Date lastLogout;
	
	private Long totalOnlineHour;
	
	private Long level;
	
	private String lastIp;
	
	private User user;
	
	private Area area;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "com.gary.wl.dao.DaoUUID")
	@GeneratedValue(generator = "system-uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getOnline() {
		return online;
	}

	public void setOnline(Boolean online) {
		this.online = online;
	}

	@JsonSerialize(using = DateJsonFull.class)
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@JsonSerialize(using = DateJsonFull.class)
	public Date getLastLogout() {
		return lastLogout;
	}

	public void setLastLogout(Date lastLogout) {
		this.lastLogout = lastLogout;
	}

	public Long getTotalOnlineHour() {
		return totalOnlineHour;
	}

	public void setTotalOnlineHour(Long totalOnlineHour) {
		this.totalOnlineHour = totalOnlineHour;
	}

	public Long getLevel() {
		return level == null ? null : (totalOnlineHour == null ? 0 : totalOnlineHour) / (5 + 5 * (level == null ? 0 : level));
	}

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	@JsonBackReference
	@OneToOne(mappedBy = "login", cascade = CascadeType.ALL)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	@OneToOne(mappedBy = "login", cascade = CascadeType.ALL)
	public Area getArea() {
		if(area == null && user != null){
			area = new Area();
			area.setLogin(this);
		}
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}
