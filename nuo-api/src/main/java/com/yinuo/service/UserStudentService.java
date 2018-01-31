package com.yinuo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.yinuo.bean.Constant;
import com.yinuo.bean.Student;
import com.yinuo.bean.User;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yinuo.bean.UserStudent;
import com.yinuo.mapper.UserStudentMapper;
import com.yinuo.util.CommonUtil;
import com.yinuo.util.DateTool;

@Transactional(rollbackFor = Exception.class)
@Service
public class UserStudentService {
	
	@Autowired
	private UserStudentMapper userStudentMapper;

	@Autowired
	private StudentService studentService;

	@Autowired
	private JedisUtil jedis;
	
	public long insert(User loginUser, UserStudent userStudent) {

		int count = countListByUserid(loginUser.getId(), userStudent.getStudentId());
		if(count > 0) {
			throw new InvalidArgumentException("你已经绑定过该学生");
		}

        count = countListByUserid(loginUser.getId(), 0L);
        if(count > Constant.UserStudentRelationship.MaxTimes) {
            throw new InvalidArgumentException(String.format("超过绑定次数：%d", Constant.UserStudentRelationship.MaxTimes));
        }

		Student student = studentService.selectOne(userStudent.getStudentId());
		if(student == null) {
			throw new InvalidArgumentException("找不到的学生");
		}

		userStudent.setSchoolId(student.getSchoolId());
		userStudent.setClassId(student.getClassId());
		CommonUtil.setDefaultValue(userStudent);
		
		userStudent.setCreateTime(DateTool.standardSdf.format(new Date()));
		userStudentMapper.insert(userStudent);
		jedis.del(Constant.JedisNames.UserStudent + loginUser.getId());
		return userStudent.getId();
	}
	
	public List<UserStudent> selectListByPage(int page, int pageSize) {
		return userStudentMapper.selectListByPage(pageSize, (page-1)*pageSize);
	}
	
	public UserStudent selectOne(long id) {
		return userStudentMapper.selectOne(id);
	}

	public void delete (User loginUser, long id) {

		UserStudent userStudent = selectOne(id);
		if(userStudent.getUserId().longValue() != loginUser.getId().longValue()) {
			throw new InvalidArgumentException("无法删除不是自己的数据");
		}

		userStudentMapper.delete(id);
		jedis.del(Constant.JedisNames.UserStudent + loginUser.getId());
	}

	public List<UserStudent> selectCacheByUserid(long userId) {
		String key = Constant.JedisNames.UserStudent + userId;

		List<UserStudent> cacheList = jedis.getArray(key, UserStudent.class);
		if(cacheList == null) {
			cacheList = selectListByUserid(userId, 0L);
			if(cacheList != null && cacheList.size() > 0) {
				jedis.set(key, JSON.toJSONString(cacheList), Constant.JedisNames.OneDay);
			}
		}
		return cacheList;
	}

	public List<UserStudent> selectListByUserid(long userId, long studentId) {
		return userStudentMapper.selectListByUserid(userId, studentId);
	}

	public int countListByUserid(long userId, long studentId) {
		return userStudentMapper.countListByUserid(userId, studentId);
	}

	public List<UserStudent> selectByStudentids(List<Long> studentIds) {
		return userStudentMapper.selectByStudentids(studentIds);
	}

	public void deleteByStudentId(long studentId) {
		List<UserStudent> uss = selectListByUserid(0L, studentId);
		if(uss == null || uss.size()<=0) {
			return;
		}
		userStudentMapper.deleteByStudentId(studentId);
		for(UserStudent us: uss) {
			jedis.del(Constant.JedisNames.UserStudent + us.getUserId());
		}
	}
}
