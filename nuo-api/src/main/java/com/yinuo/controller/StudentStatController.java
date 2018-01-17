package com.yinuo.controller;

import com.yinuo.bean.StudentStat;
import com.yinuo.bean.User;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.service.StudentStatService;
import com.yinuo.util.CommonUtil;
import com.yinuo.validation.NeedLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StudentStatController {

	@Autowired
	private StudentStatService service;
	
	@NeedLogin
	@RequestMapping(value="/studentStats", method=RequestMethod.GET)
    public Object post(User loginUser, @RequestParam(defaultValue="0") long id, @RequestParam(defaultValue="0") long studentId,
					   @RequestParam(defaultValue="0") int subject,
					   @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int pageSize){
		Map<String,Object> result = new HashMap<String, Object>();
		int count = 0;
		List<StudentStat> list = new ArrayList<StudentStat>();
		
		if(id > 0) {
			StudentStat studentStat = service.selectOne(id);
			CommonUtil.checkNull(studentStat, "找不到该学生统计信息");
			list.add(service.selectOne(id));
			count = 1;
		}else if(studentId > 0 && page > 0 && pageSize > 0){
			list = service.selectByStudentId(studentId, subject, page, pageSize);
			count = service.countByStudentId(studentId, subject);
		}else {
			throw new InvalidArgumentException("无效的查询参数");
		}
		
		result.put("data", list);
		result.put("count", count);
		return result;
    }
}
