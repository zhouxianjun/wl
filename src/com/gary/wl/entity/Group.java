package com.gary.wl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import com.gary.wl.dto.UserDto;
import com.gary.wl.util.UserComparator;

@Entity
@Table(name = "wl_group")
public class Group {
	public static final Integer NOT_UPDATE = 1;
	
	public static final Integer CUSTOM = 2;
	
	private String id;
	
	private String name;
	
	private User user;
	
	private List<User> users;
	
	private Integer state;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "com.gary.wl.dao.DaoUUID")
	@GeneratedValue(generator = "system-uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonIgnore
	@ManyToMany(targetEntity = User.class, cascade = CascadeType.ALL)
	@JoinColumn(name="user_id")
	@Sort(type = SortType.COMPARATOR, comparator = UserComparator.class)
	public List<User> getUsers() {
		users = users == null ? new ArrayList<User>() : users;
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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
			dto.setNoteName(user.getNoteName(this.user.getId()));
			dto.setNoteNames(user.getNoteNames());
			list.add(dto);
		}
		return list;
	}
}
