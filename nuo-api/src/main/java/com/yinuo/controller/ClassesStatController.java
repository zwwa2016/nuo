package com.yinuo.controller;

import com.yinuo.bean.ClassStat;
import com.yinuo.bean.User;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.service.ClassStatService;
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
public class ClassesStatController {

	@Autowired
	private ClassStatService service;
	
	@NeedLogin
	@RequestMapping(value="/classStats", method=RequestMethod.GET)
    public Object post(User loginUser, @RequestParam(defaultValue="0") long id, @RequestParam(defaultValue="0") long schoolId,
					   @RequestParam(defaultValue="0") long scoreBatchId, @RequestParam(defaultValue="0") long classId,
					   @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int pageSize){
		Map<String,Object> result = new HashMap<String, Object>();
		int count = 0;
		List<ClassStat> list = new ArrayList<ClassStat>();
		
		if(id > 0) {
			ClassStat classStat = service.selectOne(id);
			CommonUtil.checkNull(classStat, "找不到该班级统计信息");
			list.add(service.selectOne(id));
			count = 1;
		}else if(schoolId > 0 && page > 0 && pageSize > 0){
			list = service.selectBySchoolId(schoolId, scoreBatchId, classId, page, pageSize);
			count = service.countBySchoolId(schoolId, scoreBatchId, classId);
		}else {
			throw new InvalidArgumentException("无效的查询参数");
		}
		
		result.put("data", list);
		result.put("count", count);
		return result;
    }
}
