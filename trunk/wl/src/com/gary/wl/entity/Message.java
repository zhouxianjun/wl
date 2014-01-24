package com.gary.wl.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import com.gary.wl.dto.UserDto;
import com.gary.wl.util.DateJsonFull;

@Entity
@Table(name = "wl_message")
public class Message {
	public static final Integer SYSTEM = 0;
	public static final Integer PRIVATE = 1;
	public static final Integer GROUP = 2;
	public static final Integer FIREND_VALIDATION = 3;
	
	public static final Integer READ = 1;
	public static final Integer UNREAD = 0;
	public static final Integer FIREND_VALIDATION_YES = 2;
	public static final Integer FIREND_VALIDATION_NO = 3;
	
	private String id;
	
	private User from;
	
	private User to;
	
	private Chat chat;
	
	private String style;
	
	@Length(max = 255)
	@Column(length = 255)
	private String content;
	
	private Date date;
	
	private Integer type;
	
	private Integer state;
	
	@Column(length = 50)
	private String filePath;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "com.gary.wl.dao.DaoUUID")
	@GeneratedValue(generator = "system-uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	@ManyToOne(targetEntity = User.class, cascade = CascadeType.MERGE)
	@JoinColumn(name="fromuser_id")
	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	@JsonIgnore
	@ManyToOne(targetEntity = User.class, cascade = CascadeType.MERGE)
	@JoinColumn(name="touser_id")
	public User getTo() {
		return to;
	}

	public void setTo(User to) {
		this.to = to;
	}

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="chat_id")
	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JsonSerialize(using = DateJsonFull.class)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	@Transient
	public UserDto getFromDto(){
		UserDto dto = new UserDto();
		dto.setEmail(from.getEmail());
		dto.setFace(from.getUserExt().getFullFace());
		dto.setId(from.getId());
		dto.setLevel(from.getLogin().getLevel());
		dto.setName(from.getName());
		dto.setNickName(from.getUserExt().getNickName());
		dto.setSignature(from.getUserExt().getSignature());
		dto.setState(from.getState());
		dto.setIp(from.getLogin().getLastIp());
		dto.setNoteNames(from.getNoteNames());
		return dto;
	}
	
	@Transient
	public UserDto getToDto(){
		if(to != null){
			UserDto dto = new UserDto();
			dto.setEmail(to.getEmail());
			dto.setFace(to.getUserExt().getFullFace());
			dto.setId(to.getId());
			dto.setLevel(to.getLogin().getLevel());
			dto.setName(to.getName());
			dto.setNickName(to.getUserExt().getNickName());
			dto.setSignature(to.getUserExt().getSignature());
			dto.setState(to.getState());
			dto.setIp(to.getLogin().getLastIp());
			dto.setNoteNames(to.getNoteNames());
			return dto;
		}
		return null;
	}
}
