package com.yinuo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yinuo.bean.Constant;
import com.yinuo.bean.ManagerClass;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.service.ManagerClassService;
import com.yinuo.util.CommonUtil;
import com.yinuo.validation.RoleSchool;
import com.yinuo.validation.RoleTeacher;
import com.yinuo.view.ManagerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yinuo.bean.Classes;
import com.yinuo.bean.User;
import com.yinuo.service.ClassesService;
import com.yinuo.validation.NeedLogin;
import com.yinuo.validation.Validation;

@RestController
public class ClassesController {

	@Autowired
	private Validation validation;
	
	@Autowired
	private ClassesService service;

	@Autowired
	private ManagerClassService managerClassService;

	@NeedLogin
	@RequestMapping(value="/classes", method=RequestMethod.GET)
	public Object get(User loginUser, @RequestParam(defaultValue="0") long id, @RequestParam(defaultValue="0") long schoolId,
					  @RequestParam(defaultValue="0") int grade,@RequestParam(defaultValue="1") int page,
					   @RequestParam(defaultValue="20") int pageSize){
		Map<String,Object> result = new HashMap<String, Object>();
		int count = 0;
		List<Classes> list = new ArrayList<Classes>();

		if(id > 0) {
			Classes classes = service.selectOne(id);
			CommonUtil.checkNull(classes, "找不到该班级");
			list.add(service.selectOne(id));
			count = 1;
		}else if(schoolId > 0 && page > 0 && pageSize > 0){
			list = service.selectBySchoolId(schoolId, grade, page, pageSize);
			count = service.countBySchoolId(schoolId, grade);
		}else {
			throw new InvalidArgumentException("无效的查询参数");
		}

		result.put("data", list);
		result.put("count", count);
		return result;
	}

	@RoleTeacher
	@NeedLogin
	@RequestMapping(value="/classes/managers", method=RequestMethod.GET)
	public Object getManagers(User loginUser){
		Map<String,Object> result = new HashMap<String, Object>();
		List<ManagerView> views = managerClassService.getClassManagers(loginUser, Constant.Role.Teacher);
		result.put("data", views);
		return result;
	}
	
	@NeedLogin
	@RoleSchool
	@RequestMapping(value="/classes", method=RequestMethod.DELETE)
    public Object post(User loginUser, @RequestParam long id){
		Map<String,Object> result = new HashMap<String, Object>();
		service.delete(loginUser, id);
		result.put("id", id);
		return result;
    }
	
	@NeedLogin
	@RoleSchool
	@RequestMapping(value="/classes", method=RequestMethod.PUT)
    public Object put(User loginUser, @RequestBody String body){
		Map<String,Object> result = new HashMap<String, Object>();
		Classes student = validation.getObject(body, Classes.class, new String[]{"id"});
		
		service.update(loginUser, student);
		result.put("id", student.getId());
		return result;
	}
	
	@NeedLogin
	@RoleSchool
	@RequestMapping(value="/classes", method=RequestMethod.POST)
    public Object get(User loginUser, @RequestBody String body){
		Map<String, Object> result=new HashMap<String, Object>();
		Classes classes = validation.getObject(body, Classes.class, new String[]{"grade", "number", "schoolId"});
		service.insert(loginUser, classes);
		result.put("id", classes.getId());
        return result;
    }
}
