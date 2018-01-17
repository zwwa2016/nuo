package com.yinuo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yinuo.bean.Student;
import com.yinuo.service.StudentService;
import com.yinuo.util.CommonUtil;
import com.yinuo.validation.RoleTeacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yinuo.bean.Course;
import com.yinuo.bean.User;
import com.yinuo.service.CourseService;
import com.yinuo.util.DateTool;
import com.yinuo.validation.NeedLogin;
import com.yinuo.validation.Validation;

@RestController
public class CourseController {

	@Autowired
	private Validation validation;
	
	@Autowired
	private CourseService service;

	@Autowired
	private StudentService studentService;

	private Logger logger = LoggerFactory.getLogger(CourseController.class);
	
	@NeedLogin
	@RequestMapping(value="/courses", method=RequestMethod.GET)
    public Object post(User loginUser, @RequestParam(defaultValue="0") long id, @RequestParam(defaultValue="0") long classId,
    		@RequestParam(defaultValue="1") int page,@RequestParam(defaultValue="20") int pageSize){
		Map<String,Object> result = new HashMap<String, Object>();
		int count = 0;
		List<Course> list = new ArrayList<Course>();
		
		if(id > 0) {
			Course course = service.selectOne(id);
			CommonUtil.checkNull(course, "找不到该课程");
			list.add(course);
			count = 1;
		}else if(classId > 0){
			Course course = service.selectByClassId(classId);
			CommonUtil.checkNull(course, "找不到该课程");
			list.add(course);
			count = 1;
		}
		
		result.put("data", list);
		result.put("count", count);
		return result;
    }
	
	@NeedLogin
	@RoleTeacher
	@RequestMapping(value="/courses", method=RequestMethod.DELETE)
    public Object post(User loginUser, @RequestParam long id){
		Map<String,Object> result = new HashMap<String, Object>();
		service.delete(loginUser, id);
		result.put("id", id);
		return result;
    }
	
	@NeedLogin
	@RoleTeacher
	@RequestMapping(value="/courses", method=RequestMethod.PUT)
    public Object put(User loginUser, @RequestBody String body){
		Map<String,Object> result = new HashMap<String, Object>();
		Course course = validation.getObject(body, Course.class, new String[]{"id"});
		
		service.update(loginUser, course);
		result.put("id", course.getId());
		return result;
	}
	
	@NeedLogin
	@RoleTeacher
	@RequestMapping(value="/courses", method=RequestMethod.POST)
    public Object get(User loginUser, @RequestBody String body){
		Map<String, Object> result=new HashMap<String, Object>();
		Course course = validation.getObject(body, Course.class, new String[]{"classId"});
		service.insert(loginUser, course);
		result.put("id", course.getId());
        return result;
    }
}
