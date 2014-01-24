package com.gary.wl.entity;

import java.io.Serializable;


public class VersionId implements Serializable {
	
	private static final long serialVersionUID = 2013955329389989670L;

	private String type;
	
	private String version;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override 
    public boolean equals(Object obj) { 
        if(obj instanceof VersionId){ 
        	VersionId pk=(VersionId)obj; 
            if(this.type.equals(pk.type)&&this.version.equals(pk.version)){ 
                return true; 
            } 
        } 
        return false; 
    }

    @Override 
    public int hashCode() { 
        return super.hashCode(); 
    }
}
