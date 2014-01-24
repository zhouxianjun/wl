package com.gary.wl.service;

import java.io.Serializable;
import java.util.List;

import com.gary.wl.entity.Version;

public interface VersionService {
	public Version findByUniqueCode(String type, String version);
	
	public Serializable save(Version v);
	
	public void update(Version v);
	
	public void saveOrUpdate(Version v);
	
	public List<Version> all();
	
	public List<Version> all(String type);
	
	public Version now(String type);
}
