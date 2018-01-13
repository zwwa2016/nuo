package com.yinuo.service;

import com.yinuo.bean.Constant;
import com.yinuo.bean.ScoreBatch;
import com.yinuo.bean.User;
import com.yinuo.mapper.ScoreBatchMapper;
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
public class ScoreBatchService {
	
	@Autowired
	private ScoreBatchMapper scoreBatchMapper;
	
	public long insert(User loginUser, ScoreBatch scoreBatch) {
		loginUser.checkLevel(Constant.Role.Teacher, scoreBatch.getSchoolId(), scoreBatch.getClassId());
		CommonUtil.setDefaultValue(scoreBatch);

		scoreBatch.setManagerId(loginUser.getManager().getId());
		scoreBatch.setCreateTime(DateTool.standardSdf.format(new Date()));
		scoreBatchMapper.insert(scoreBatch);
		return scoreBatch.getId();
	}
	
	public ScoreBatch selectOne(long id) {
		return scoreBatchMapper.selectOne(id);
	}
	
	public void delete (User loginUser, long id) {
		ScoreBatch scoreBatch = selectOne(id);
		loginUser.checkLevel(Constant.Role.Teacher, scoreBatch.getSchoolId(), scoreBatch.getClassId());
		scoreBatchMapper.delete(id);
	}
	
	public void update(User loginUser, ScoreBatch scoreBatch) {
		ScoreBatch src = selectOne(scoreBatch.getId());

		scoreBatch = (ScoreBatch) MergerUtil.merger(src, scoreBatch);

		loginUser.checkLevel(Constant.Role.Teacher, scoreBatch.getSchoolId(), scoreBatch.getClassId());
		scoreBatchMapper.update(scoreBatch);
	}

	public List<ScoreBatch> selectBySchoolId(long schoolId, int page, int pageLength) {
		return scoreBatchMapper.selectBySchoolId(schoolId, pageLength, (page-1)*pageLength);
	}

	public int countBySchoolId(long schoolId) {
		return scoreBatchMapper.countBySchoolId(schoolId);
	}

	public List<ScoreBatch> selectByClassId(long classId, int page, int pageLength) {
		return scoreBatchMapper.selectByClassId(classId, pageLength, (page-1)*pageLength);
	}

	public int countByClassId(long classId) {
		return scoreBatchMapper.countByClassId(classId);
	}

	public List<ScoreBatch> selectByIds(List<Long> ids) {
		return scoreBatchMapper.selectListByIds(ids);
	}
}
