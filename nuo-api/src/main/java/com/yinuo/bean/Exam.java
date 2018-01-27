package com.yinuo.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yinuo.exception.InvalidHttpArgumentException;
import com.yinuo.validation.IsInt;
import com.yinuo.validation.IsString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Exam {

	private Long id;

	@IsString(maxLength = 15, minLength = 1)
	private String name;

	@IsInt(min = 1, max = Integer.MAX_VALUE)
	private Long schoolId;

	@IsInt(min = 1, max = 127)
	private Integer grade;

	@IsString(maxLength = 45, minLength = 1)
	private String subjects;

	private Integer state;

	private Long fixManagerId;

	private Long managerId;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date fixTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.createTime = sdf.parse(createTime);
		} catch (ParseException e) {
			throw new InvalidHttpArgumentException("invalid params.createTime parse error.");
		}
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getFixManagerId() {
		return fixManagerId;
	}

	public void setFixManagerId(Long fixManagerId) {
		this.fixManagerId = fixManagerId;
	}

	public Date getFixTime() {
		return fixTime;
	}

	public void setFixTime(String fixTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.fixTime = sdf.parse(fixTime);
		} catch (ParseException e) {
			throw new InvalidHttpArgumentException("invalid params.createTime parse error.");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String toString() {
		try {
			return JSON.toJSONString(this);
		} catch (Exception e) {
		}
		return "json parse failed";
	}
}
