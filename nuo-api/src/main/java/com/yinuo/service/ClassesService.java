package com.yinuo.service;

import java.util.Date;
import java.util.List;

import com.yinuo.bean.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yinuo.bean.Classes;
import com.yinuo.bean.User;
import com.yinuo.mapper.ClassesMapper;
import com.yinuo.util.CommonUtil;
import com.yinuo.util.DateTool;
import com.yinuo.util.MergerUtil;

@Transactional(rollbackFor = Exception.class)
@Service
public class ClassesService {
	
	@Autowired
	private ClassesMapper classesMapper;
	
	public long insert(User loginUser, Classes classes) {
		loginUser.checkLevel(Constant.Role.School, classes.getSchoolId(), 0L);

		CommonUtil.setDefaultValue(classes);

		classes.setManagerId(loginUser.getManager().getId());
		classes.setCreateTime(DateTool.standardSdf.format(new Date()));
		classesMapper.insert(classes);
		return classes.getId();
	}
	
	public Classes selectOne(long id) {
		return classesMapper.selectOne(id);
	}
	
	public void delete (User loginUser, long id) {
		Classes classes = selectOne(id);
		loginUser.checkLevel(Constant.Role.School, classes.getSchoolId(), classes.getId());

		classesMapper.delete(id);
	}
	
	//只能自己修改自己的数据场景
	public void update(User loginUser, Classes classes) {
		Classes src = selectOne(classes.getId());
		
		classes = (Classes) MergerUtil.merger(src, classes);

		loginUser.checkLevel(Constant.Role.School, classes.getSchoolId(), 0L);
		classesMapper.update(classes);
	}
	
	public List<Classes> selectListByIds(List<Long> ids) {
		return classesMapper.selectListByIds(ids);
	}

	public List<Classes> selectListByPage(int page, int pageLength) {
		return classesMapper.selectListByPage(pageLength, (page-1)*pageLength);
	}
	
	public List<Classes> selectBySchoolId(long schoolId, int page, int pageLength) {
		return classesMapper.selectBySchoolId(schoolId, pageLength, (page-1)*pageLength);
	}

	public int countBySchoolId(long schoolId) {
		return classesMapper.countBySchoolId(schoolId);
	}
	
}
