package com.yinuo.service;

import com.yinuo.bean.School;
import com.yinuo.bean.Constant;
import com.yinuo.bean.User;
import com.yinuo.mapper.SchoolMapper;
import com.yinuo.util.CommonUtil;
import com.yinuo.util.DateTool;
import com.yinuo.util.MergerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class SchoolService {
	
	@Autowired
	private SchoolMapper schoolMapper;
	
	public long insert(User loginUser, School school) {
		loginUser.checkLevel(Constant.Role.Manager, 0L, 0L);

		CommonUtil.setDefaultValue(school);

		school.setManagerId(loginUser.getManager().getId());
		school.setCreateTime(DateTool.standardSdf.format(new Date()));
		schoolMapper.insert(school);
		return school.getId();
	}
	
	public School selectOne(long id) {
		return schoolMapper.selectOne(id);
	}
	
	public void delete (User loginUser, long id) {
		loginUser.checkLevel(Constant.Role.Manager, 0L, 0L);

		schoolMapper.delete(id);
	}
	
	//只能自己修改自己的数据场景
	public void update(User loginUser, School school) {
		School src = selectOne(school.getId());
		
		school = (School) MergerUtil.merger(src, school);

		loginUser.checkLevel(Constant.Role.Manager, 0L, 0L);
		schoolMapper.update(school);
	}
	
	public List<School> selectListByIds(List<Long> ids) {
		return schoolMapper.selectListByIds(ids);
	}

	public List<School> selectListByPage(int page, int pageLength) {
		return schoolMapper.selectListByPage(pageLength, (page-1)*pageLength);
	}

	public int countAll() {
		return schoolMapper.countAll();
	}
	
}
