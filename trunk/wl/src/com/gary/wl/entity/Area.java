package com.gary.wl.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import com.gary.core.util.ValidateUtils;

@Entity
@Table(name = "wl_area")
public class Area {
	private String id;
	
	private String country;
	
	private String countrySN;
	
	private String province;
	
	private String provinceSN;
	
	private String city;
	
	private String citySN;
	
	private String isp;
	
	@Pattern(regexp = ValidateUtils.IP, message = "IP地址格式错误!")
	private String ip;

	@Pattern(regexp = ValidateUtils.ADDRESS)
	@Column(length = 100)
	private String memo;
	
	private Set<User> users;
	
	private Login login;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "com.gary.wl.dao.DaoUUID")
	@GeneratedValue(generator = "system-uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@JsonBackReference
	@OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
	public Set<User> getUsers() {
		users = users == null ? new HashSet<User>() : users;
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getCountrySN() {
		return countrySN;
	}

	public void setCountrySN(String countrySN) {
		this.countrySN = countrySN;
	}

	public String getProvinceSN() {
		return provinceSN;
	}

	public void setProvinceSN(String provinceSN) {
		this.provinceSN = provinceSN;
	}

	public String getCitySN() {
		return citySN;
	}

	public void setCitySN(String citySN) {
		this.citySN = citySN;
	}

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "login_id")
	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
}
