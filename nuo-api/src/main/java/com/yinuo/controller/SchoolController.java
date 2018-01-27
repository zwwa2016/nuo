package com.yinuo.controller;

import com.yinuo.bean.Constant;
import com.yinuo.bean.School;
import com.yinuo.bean.User;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.service.ManagerClassService;
import com.yinuo.service.SchoolService;
import com.yinuo.util.CommonUtil;
import com.yinuo.validation.*;
import com.yinuo.view.ManagerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SchoolController {

	@Autowired
	private Validation validation;
	
	@Autowired
	private SchoolService service;

	@Autowired
    private ManagerClassService managerClassService;
	
	@NeedLogin
	@RequestMapping(value="/schools", method=RequestMethod.GET)
    public Object post(User loginUser, @RequestParam(defaultValue="0") long id,
					   @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int pageSize){
		Map<String,Object> result = new HashMap<String, Object>();
		int count = 0;
		List<School> list = new ArrayList<School>();
		
		if(id > 0) {
			School school = service.selectOne(id);
			CommonUtil.checkNull(school, "找不到该学校");
			list.add(school);
			count = 1;
		}else if(page > 0 && pageSize > 0){
			list = service.selectListByPage(page, pageSize);
			count = service.countAll();
		}else {
			throw new InvalidArgumentException("无效的查询参数");
		}

		result.put("data", list);
		result.put("count", count);
		return result;
    }

	@RoleSchool
	@NeedLogin
	@RequestMapping(value="/schools/managers", method=RequestMethod.GET)
	public Object getManagers(User loginUser){
		Map<String,Object> result = new HashMap<String, Object>();
		List<ManagerView> views = managerClassService.getClassManagers(loginUser, Constant.Role.School);
		result.put("data", views);
		return result;
	}
	
	@NeedLogin
	@RoleManager
	@RequestMapping(value="/schools", method=RequestMethod.DELETE)
    public Object post(User loginUser, @RequestParam long id){
		Map<String,Object> result = new HashMap<String, Object>();
		service.delete(loginUser, id);
		result.put("id", id);
		return result;
    }
	
	@NeedLogin
	@RoleManager
	@RequestMapping(value="/schools", method=RequestMethod.PUT)
    public Object put(User loginUser, @RequestBody String body){
		Map<String,Object> result = new HashMap<String, Object>();
		School student = validation.getObject(body, School.class, new String[]{"id"});
		
		service.update(loginUser, student);
		result.put("id", student.getId());
		return result;
	}
	
	@NeedLogin
	@RoleManager
	@RequestMapping(value="/schools", method=RequestMethod.POST)
    public Object get(User loginUser, @RequestBody String body){
		Map<String, Object> result=new HashMap<String, Object>();
		School school = validation.getObject(body, School.class, new String[]{"name"});
		service.insert(loginUser, school);
		result.put("id", school.getId());
        return result;
    }
}
