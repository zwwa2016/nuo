package com.yinuo.service;

import com.alibaba.fastjson.JSON;
import com.yinuo.bean.Classes;
import com.yinuo.bean.Constant;
import com.yinuo.bean.User;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.mapper.ClassesMapper;
import com.yinuo.util.CommonUtil;
import com.yinuo.util.DateTool;
import com.yinuo.util.JedisUtil;
import com.yinuo.util.MergerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class ClassesService {
	
	@Autowired
	private ClassesMapper classesMapper;

	@Autowired
	private StudentService studentService;

	@Autowired
	private JedisUtil jedis;
	
	public long insert(User loginUser, Classes classes) {
		loginUser.checkLevel(Constant.Role.School, classes.getSchoolId(), 0L);

		CommonUtil.setDefaultValue(classes);

		classes.setManagerId(loginUser.getManager().getId());
		classes.setCreateTime(DateTool.standardSdf.format(new Date()));
		classesMapper.insert(classes);
		return classes.getId();
	}
	
	public Classes selectOne(long id) {
		Classes classes = jedis.get(Constant.JedisNames.ClassId + id, Classes.class);
		if(classes == null) {
			classes = classesMapper.selectOne(id);
			if(classes != null) {
				jedis.set(Constant.JedisNames.ClassId + id, JSON.toJSONString(classes), Constant.JedisNames.OneDay);
			}
		}

		return classes;
	}
	
	public void delete (User loginUser, long id) {
		Classes classes = selectOne(id);
		CommonUtil.checkNull(classes, "找不到该班级");
		loginUser.checkLevel(Constant.Role.School, classes.getSchoolId(), classes.getId());

		if(studentService.countByClassid(id) > 0) {
			throw new InvalidArgumentException("请先删除学生");
		}

		classesMapper.delete(id);
		jedis.del(Constant.JedisNames.ClassId + id);
	}
	
	//只能自己修改自己的数据场景
	public void update(User loginUser, Classes classes) {
		Classes src = selectOne(classes.getId());
		
		classes = (Classes) MergerUtil.merger(src, classes);

		loginUser.checkLevel(Constant.Role.School, classes.getSchoolId(), 0L);
		classesMapper.update(classes);
		jedis.del(Constant.JedisNames.ClassId + classes.getId());
	}
	
	public List<Classes> selectListByIds(List<Long> ids) {
		return classesMapper.selectListByIds(ids);
	}

	public List<Classes> selectListByPage(int page, int pageLength) {
		return classesMapper.selectListByPage(pageLength, (page-1)*pageLength);
	}
	
	public List<Classes> selectBySchoolId(long schoolId, int grade, int page, int pageLength) {
		return classesMapper.selectBySchoolId(schoolId, grade, pageLength, (page-1)*pageLength);
	}

	public int countBySchoolId(long schoolId, int grade) {
		return classesMapper.countBySchoolId(schoolId, grade);
	}
	
}
