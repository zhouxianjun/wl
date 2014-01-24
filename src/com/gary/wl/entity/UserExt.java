package com.gary.wl.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import com.gary.core.util.ValidateUtils;
import com.gary.framework.config.ApplicationContextHolder;
import com.gary.wl.dto.Config;

@Entity
@Table(name = "wl_user_ext")
public class UserExt {
	private String id;
	
	@Pattern(regexp = ValidateUtils.DATE_YYYY_MM_DD, message = "日期格式错误!必须为: YYYY-MM-DD格式")
	private String birthday;
	
	private String signature;
	
	private String nickName;
	
	private String face;
	
	private String qq;
	
	private String zodiac;
	
	private String constellation;
	
	private String blood;
	
	private String description;
	
	@Pattern(regexp = ValidateUtils.PHONE, message = "电话号码格式错误!")
	private String tel;
	
	@Pattern(regexp = ValidateUtils.MOBILE, message = "手机号码格式错误!")
	private String mobile;
	
	@Max(value = 2)
	@Min(value = 1)
	private Integer sex;
	
	private User user;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "com.gary.wl.dao.DaoUUID")
	@GeneratedValue(generator = "system-uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@JsonProperty("sface")
	public String getFace() {
		return face;
	}
	
	@Transient
	@JsonProperty("face")
	public String getFullFace(){
		return face != null ? ApplicationContextHolder.getBean("config", Config.class).getUSER_FACE_PATH() + face : null;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@JsonBackReference
	@OneToOne(mappedBy = "userExt")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	@Transient
	public char getSexMemo(){
		return sex == null || sex == 2 ? '男' : '女';
	}

	public String getZodiac() {
		return zodiac;
	}

	public void setZodiac(String zodiac) {
		this.zodiac = zodiac;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getBlood() {
		return blood;
	}

	public void setBlood(String blood) {
		this.blood = blood;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
