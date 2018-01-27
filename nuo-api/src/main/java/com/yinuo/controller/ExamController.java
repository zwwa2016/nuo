package com.yinuo.controller;

import com.yinuo.bean.Exam;
import com.yinuo.bean.User;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.service.ExamService;
import com.yinuo.util.CommonUtil;
import com.yinuo.validation.NeedLogin;
import com.yinuo.validation.RoleSchool;
import com.yinuo.validation.RoleTeacher;
import com.yinuo.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExamController {

	@Autowired
	private Validation validation;
	
	@Autowired
	private ExamService service;
	
	@NeedLogin
	@RequestMapping(value="/exams", method=RequestMethod.GET)
    public Object post(User loginUser, @RequestParam(defaultValue="0") long id, @RequestParam(defaultValue="0") long schoolId,
    		@RequestParam(defaultValue="1") int page,
    		@RequestParam(defaultValue="20") int pageSize){
		Map<String,Object> result = new HashMap<String, Object>();
		
		List<Exam> exams = new ArrayList<Exam>();
		int count = 0;
		if(id > 0) {
			Exam exam = service.selectOne(id);
			CommonUtil.checkNull(exam, "找不到该次考试");
			exams.add(exam);
			count = 1;
		}else if(schoolId > 0 && page > 0 && pageSize > 0) {
			exams = service.selectBySchoolId(schoolId, page, pageSize);
			count = service.countBySchoolId(schoolId);
		}else {
			throw new InvalidArgumentException("无效的参数");
		}
		result.put("data", exams);
		result.put("count", count);
		return result;
    }
	
	@NeedLogin
	@RoleSchool
	@RequestMapping(value="/exams", method=RequestMethod.DELETE)
    public Object post(User loginUser, @RequestParam long id){
		Map<String,Object> result = new HashMap<String, Object>();
		service.delete(loginUser, id);
		result.put("id", id);
		return result;
    }
	
	@NeedLogin
	@RoleSchool
	@RequestMapping(value="/exams", method=RequestMethod.PUT)
    public Object put(User loginUser, @RequestBody String body){
		Map<String,Object> result = new HashMap<String, Object>();
		Exam exam = validation.getObject(body, Exam.class, new String[]{"id"});
		
		service.update(loginUser, exam);
		result.put("id", exam.getId());
		return result;
	}
	
	@NeedLogin
	@RoleSchool
	@RequestMapping(value="/exams", method=RequestMethod.POST)
    public Object get(User loginUser, @RequestBody String body){
		Map<String, Object> result=new HashMap<String, Object>();
		Exam exam = validation.getObject(body, Exam.class, new String[]{"name", "schoolId", "grade", "subjects"});
		service.insert(loginUser, exam);
		result.put("id", exam.getId());
        return result;
    }
}