package com.yinuo.controller;

import com.yinuo.bean.ScoreBatch;
import com.yinuo.bean.User;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.service.ScoreBatchService;
import com.yinuo.util.CommonUtil;
import com.yinuo.validation.NeedLogin;
import com.yinuo.validation.RoleTeacher;
import com.yinuo.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ScoreBatchController {

	@Autowired
	private Validation validation;
	
	@Autowired
	private ScoreBatchService service;
	
	@NeedLogin
	@RequestMapping(value="/scoreBatchs", method=RequestMethod.GET)
    public Object post(User loginUser, @RequestParam(defaultValue="0") long id,
    		@RequestParam(defaultValue="0") long classId,@RequestParam(defaultValue="0") int type, @RequestParam(defaultValue="0") long examId, @RequestParam(defaultValue="1") int page,
    		@RequestParam(defaultValue="20") int pageSize){
		Map<String,Object> result = new HashMap<String, Object>();
		
		List<ScoreBatch> scores = new ArrayList<ScoreBatch>();
		int count = 0;
		if(id > 0) {
			ScoreBatch score = service.selectOne(id);
			CommonUtil.checkNull(score, "找不到该成绩批次");
			scores.add(score);
			count = 1;
		}else if(classId > 0 && page > 0 && pageSize > 0) {
			scores = service.selectByClassId(classId, type, page, pageSize);
			count = service.countByClassId(classId, type);
		}else if(examId > 0 && page > 0 && pageSize > 0) {
			scores = service.selectByExamId(examId, page, pageSize);
			count = service.countByExamId(examId);
		}else {
			throw new InvalidArgumentException("无效的参数");
		}
		result.put("data", scores);
		result.put("count", count);
		return result;
    }
	
	@NeedLogin
	@RoleTeacher
	@RequestMapping(value="/scoreBatchs", method=RequestMethod.DELETE)
    public Object post(User loginUser, @RequestParam long id){
		Map<String,Object> result = new HashMap<String, Object>();
		service.delete(loginUser, id);
		result.put("id", id);
		return result;
    }
	
	@NeedLogin
	@RoleTeacher
	@RequestMapping(value="/scoreBatchs", method=RequestMethod.PUT)
    public Object put(User loginUser, @RequestBody String body){
		Map<String,Object> result = new HashMap<String, Object>();
		ScoreBatch score = validation.getObject(body, ScoreBatch.class, new String[]{"id"});
		
		service.update(loginUser, score);
		result.put("id", score.getId());
		return result;
	}
	
	@NeedLogin
	@RoleTeacher
	@RequestMapping(value="/scoreBatchs", method=RequestMethod.POST)
    public Object get(User loginUser, @RequestBody String body){
		Map<String, Object> result=new HashMap<String, Object>();
		ScoreBatch score = validation.getObject(body, ScoreBatch.class, new String[]{});
		service.insert(loginUser, score);
		result.put("id", score.getId());
        return result;
    }
}