package com.gary.wl.dto;

import java.util.Set;

import com.gary.wl.entity.NoteName;

public class UserDto {
	private String id;
	
	private String name;
	
	private String nickName;
	
	private String email;
	
	private Integer state;
	
	private String signature;
	
	private String face;
	
	private Boolean online;
	
	private Long level;
	
	private String ip;
	
	private String noteName;
	
	private Set<NoteName> noteNames;

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public Boolean getOnline() {
		return online;
	}

	public void setOnline(Boolean online) {
		this.online = online;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public Set<NoteName> getNoteNames() {
		return noteNames;
	}

	public void setNoteNames(Set<NoteName> noteNames) {
		this.noteNames = noteNames;
	}
}
