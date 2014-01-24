package com.gary.wl.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import jsondate.DateJsonFull;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import com.gary.wl.dto.UserDto;

@Entity
@Table(name = "wl_chat")
public class Chat {
	private String id;
	
	private Integer type;
	
	private Set<User> users;
	
	private User createUser;
	
	private Date createDate;
	
	private Set<Message> messages;
	
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@JsonBackReference
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	public Set<User> getUsers() {
		users = users == null ? new HashSet<User>() : users;
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@JsonIgnore
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "createUser_id")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@JsonSerialize(using = DateJsonFull.class)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@OrderBy("date desc")
	@OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
	
	@Transient
	public UserDto getCreate(){
		UserDto dto = new UserDto();
		dto.setEmail(createUser.getEmail());
		dto.setFace(createUser.getUserExt().getFullFace());
		dto.setId(createUser.getId());
		dto.setLevel(createUser.getLogin().getLevel());
		dto.setName(createUser.getName());
		dto.setNickName(createUser.getUserExt().getNickName());
		dto.setSignature(createUser.getUserExt().getSignature());
		dto.setState(createUser.getState());
		dto.setOnline(createUser.getLogin().getOnline());
		dto.setIp(createUser.getLogin().getLastIp());
		dto.setNoteNames(createUser.getNoteNames());
		return dto;
	}
	
	@Transient
	public UserDto getTarget(){
		UserDto dto = new UserDto();
		if(type == Message.PRIVATE){
			for (User user : users) {
				if(user.getId() != createUser.getId()){
					dto.setEmail(user.getEmail());
					dto.setFace(user.getUserExt().getFullFace());
					dto.setId(user.getId());
					dto.setLevel(user.getLogin().getLevel());
					dto.setName(user.getName());
					dto.setNickName(user.getUserExt().getNickName());
					dto.setSignature(user.getUserExt().getSignature());
					dto.setState(user.getState());
					dto.setOnline(user.getLogin().getOnline());
					dto.setIp(user.getLogin().getLastIp());
				}
			}
		}else{
			dto.setId(id);
		}
		return dto;
	}
	
	@Transient
	public List<UserDto> getUsersDto(){
		List<UserDto> list = new ArrayList<UserDto>();
		for (User user : users) {
			UserDto dto = new UserDto();
			dto.setEmail(user.getEmail());
			dto.setFace(user.getUserExt().getFullFace());
			dto.setId(user.getId());
			dto.setLevel(user.getLogin().getLevel());
			dto.setName(user.getName());
			dto.setNickName(user.getUserExt().getNickName());
			dto.setSignature(user.getUserExt().getSignature());
			dto.setState(user.getState());
			dto.setOnline(user.getLogin().getOnline());
			dto.setIp(user.getLogin().getLastIp());
			list.add(dto);
		}
		return list;
	}
}
