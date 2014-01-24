package com.gary.wl.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.validator.constraints.Email;

import com.gary.wl.util.ChatComparator;

@Entity
@Table(name = "wl_user")
public class User {
	public static final Integer ONLINE = 1;
	public static final Integer OFFLINE = 0;
	
	private String id;
	
	@Pattern(regexp = "^[a-zA-Z0-9_]{3,16}$")
	@NotNull
	private String name;
	
	@NotNull
	private String password;
	
	@Email
	@NotNull
	private String email;
	
	private Integer state;
	
	private UserExt userExt;
	
	private Set<Group> groups;
	
	private List<Chat> chats;
	
	private Area area;
	
	private Login login;
	
	private Set<NoteName> noteNames;
	
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

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@JsonManagedReference
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ext_id")
	public UserExt getUserExt() {
		return userExt;
	}

	public void setUserExt(UserExt userExt) {
		this.userExt = userExt;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@OrderBy("state asc")
	public Set<Group> getGroups() {
		groups = groups == null ? new HashSet<Group>() : groups;
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	@JsonManagedReference
	@ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
	@OrderBy("createDate desc")
	@Sort(type = SortType.COMPARATOR, comparator = ChatComparator.class)
	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	@JsonManagedReference
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "area_id")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@JsonManagedReference
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "login_id")
	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
	public Set<NoteName> getNoteNames() {
		noteNames = noteNames == null ? new HashSet<NoteName>() : noteNames;
		return noteNames;
	}

	public void setNoteNames(Set<NoteName> noteNames) {
		this.noteNames = noteNames;
	}
	
	@Transient
	public String getNoteName(String userid){
		for (NoteName noteName : noteNames) {
			if(noteName.getBelongs().getId().equals(userid)){
				return noteName.getName();
			}
		}
		return null;
	}
}
