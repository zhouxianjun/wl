package com.gary.wl.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import com.gary.wl.dto.UserDto;

@Entity
@Table(name = "wl_note_name")
public class NoteName {
	private String name;
	
	private String id;
	
	private User target;
	
	private User belongs;
	
	private Date createDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "com.gary.wl.dao.DaoUUID")
	@GeneratedValue(generator = "system-uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="target_id")
	public User getTarget() {
		return target;
	}

	public void setTarget(User target) {
		this.target = target;
	}

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = User.class)
	@JoinColumn(name = "belongs_id")
	public User getBelongs() {
		return belongs;
	}

	public void setBelongs(User belongs) {
		this.belongs = belongs;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Transient
	public UserDto getBelongsDto(){
		UserDto dto = new UserDto();
		dto.setEmail(belongs.getEmail());
		dto.setFace(belongs.getUserExt().getFullFace());
		dto.setId(belongs.getId());
		dto.setLevel(belongs.getLogin().getLevel());
		dto.setName(belongs.getName());
		dto.setNickName(belongs.getUserExt().getNickName());
		dto.setSignature(belongs.getUserExt().getSignature());
		dto.setState(belongs.getState());
		dto.setOnline(belongs.getLogin().getOnline());
		dto.setIp(belongs.getLogin().getLastIp());
		return dto;
	}
	
	@Transient
	public UserDto getTag(){
		UserDto dto = new UserDto();
		dto.setEmail(target.getEmail());
		dto.setFace(target.getUserExt().getFullFace());
		dto.setId(target.getId());
		dto.setLevel(target.getLogin().getLevel());
		dto.setName(target.getName());
		dto.setNickName(target.getUserExt().getNickName());
		dto.setSignature(target.getUserExt().getSignature());
		dto.setState(target.getState());
		dto.setOnline(target.getLogin().getOnline());
		dto.setIp(target.getLogin().getLastIp());
		return dto;
	}
}
