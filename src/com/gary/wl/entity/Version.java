package com.gary.wl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gary.framework.config.ApplicationContextHolder;
import com.gary.wl.dto.Config;

@Entity
@Table(name = "wl_version")
@IdClass(VersionId.class)
public class Version {
	private String type;
	
	private String version;
	
	private String path;
	
	private String memo;
	
	@Id
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Id
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMemo() {
		return memo != null ? memo.replaceAll("\\r\\n", "<br>") : null;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Transient
	public String getUrl() {
		int index = path.lastIndexOf("\\");
		index = index < 0 ? path.lastIndexOf("/") : index;
		String name = index < 0 ? path : path.substring(index + 1);
		return ApplicationContextHolder.getBean("config", Config.class).getVERSION_PATH() + type + "/" + name;
	}
}
