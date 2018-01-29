package com.yinuo.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yinuo.exception.InvalidHttpArgumentException;
import com.yinuo.validation.IsInt;
import com.yinuo.validation.IsString;


public class Score {

	private Long id;

	@IsInt(min=1, max=2)
	private Integer type;
	
	private Long studentId;

	private Integer subject;

	@IsInt(max = 1000, min = 0)
	private Integer score;
	
	@IsString(minLength=1, maxLength=255)
	private String pic;

	private Long examId;

	private Long scoreBatchId;

	private Long classId;

	private Long managerId;

	private Integer schoolRank;

	private Integer classRank;

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

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public void setCreateTime(String createTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.createTime = sdf.parse(createTime);
		} catch (ParseException e) {
			throw new InvalidHttpArgumentException("invalid params.createTime parse error.");
		}
	}

	public Long getScoreBatchId() {
		return scoreBatchId;
	}

	public void setScoreBatchId(Long scoreBatchId) {
		this.scoreBatchId = scoreBatchId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getExamId() {
		return examId;
	}

	public void setExamId(Long examId) {
		this.examId = examId;
	}

	public Integer getSubject() {
		return subject;
	}

	public void setSubject(Integer subject) {
		this.subject = subject;
	}

	public Integer getSchoolRank() {
		return schoolRank;
	}

	public void setSchoolRank(Integer schoolRank) {
		this.schoolRank = schoolRank;
	}

	public Integer getClassRank() {
		return classRank;
	}

	public void setClassRank(Integer classRank) {
		this.classRank = classRank;
	}

	public String toString() {
		try {
			return JSON.toJSONString(this);
		} catch (Exception e) {
		}
		return "json parse failed";
	}
}
