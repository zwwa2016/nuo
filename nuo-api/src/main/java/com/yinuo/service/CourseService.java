package com.yinuo.service;

import java.util.Date;
import java.util.List;

import com.yinuo.bean.Classes;
import com.yinuo.bean.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yinuo.bean.Course;
import com.yinuo.bean.User;
import com.yinuo.mapper.CourseMapper;
import com.yinuo.util.CommonUtil;
import com.yinuo.util.DateTool;
import com.yinuo.util.MergerUtil;

@Transactional(rollbackFor = Exception.class)
@Service
public class CourseService {
	
	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	private ClassesService classesService;
	
	public long insert(User loginUser, Course course) {
		checkLevel(loginUser, course.getClassId());
		CommonUtil.setDefaultValue(course);
		
		course.setCreateTime(DateTool.standardSdf.format(new Date()));
		course.setManagerId(loginUser.getManager().getId());
		courseMapper.insert(course);
		return course.getId();
	}
	
	public Course selectOne(long id) {
		return courseMapper.selectOne(id);
	}
	
	public void delete (User loginUser, long id) {
		Course course = courseMapper.selectOne(id);
		CommonUtil.checkNull(course, "找不到该课程");

		checkLevel(loginUser, course.getClassId());
		courseMapper.delete(id);
	}
	
	public void update(User loginUser, Course course) {
		Course src = selectOne(course.getId());
		
		course = (Course) MergerUtil.merger(src, course);

		checkLevel(loginUser, course.getClassId());
		courseMapper.update(course);
	}
	
	public List<Course> selectListByIds(List<Long> ids) {
		return courseMapper.selectListByIds(ids);
	}
	
	public List<Course> selectListByPage(int page, int pageLength) {
		return courseMapper.selectListByPage(pageLength, (page-1)*pageLength);
	}
	
	public Course selectByClassId(long classId) {
		return courseMapper.selectByClassId(classId);
	}

	public void checkLevel(User loginUser, long classId) {
		Classes classes = classesService.selectOne(classId);
		CommonUtil.checkNull(classes, "找不到该班级");
		loginUser.checkLevel(Constant.Role.Teacher, classes.getSchoolId(), classId);
	}
	
}
