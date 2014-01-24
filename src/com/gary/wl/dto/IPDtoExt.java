package com.gary.wl.dto;

import com.gary.core.dto.IPDto;

public class IPDtoExt extends IPDto {
	private String countrySN;
	
	private String provinceSN;
	
	private String citySN;

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
}
