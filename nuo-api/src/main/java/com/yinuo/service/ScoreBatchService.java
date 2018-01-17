package com.yinuo.service;

import com.yinuo.bean.Constant;
import com.yinuo.bean.ScoreBatch;
import com.yinuo.bean.User;
import com.yinuo.exception.InvalidArgumentException;
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
		checkPermission(loginUser, scoreBatch);
		CommonUtil.setDefaultValue(scoreBatch);

		scoreBatch.setManagerId(loginUser.getManager().getId());
		scoreBatch.setCreateTime(DateTool.standardSdf.format(new Date()));
		scoreBatch.setManagerId(0L);
		scoreBatch.setFixTime(Constant.Time.Zone);
		scoreBatch.setState(Constant.ScoreBatchState.Create);
		scoreBatchMapper.insert(scoreBatch);
		return scoreBatch.getId();
	}
	
	public ScoreBatch selectOne(long id) {
		return scoreBatchMapper.selectOne(id);
	}
	
	public void delete (User loginUser, long id) {
		ScoreBatch scoreBatch = selectOne(id);
		checkPermission(loginUser, scoreBatch);
		scoreBatchMapper.delete(id);
	}
	
	public void update(User loginUser, ScoreBatch scoreBatch) {
		ScoreBatch src = selectOne(scoreBatch.getId());

		scoreBatch = (ScoreBatch) MergerUtil.merger(src, scoreBatch);

		checkPermission(loginUser, scoreBatch);

		if(scoreBatch.getState() == Constant.ScoreBatchState.WaitStat && src.getState() != Constant.ScoreBatchState.WaitStat) {
			scoreBatch.setFixTime(Constant.Time.Now());
		}
		scoreBatchMapper.update(scoreBatch);
	}

	public List<ScoreBatch> selectBySchoolId(long schoolId, int page, int pageLength) {
		return scoreBatchMapper.selectBySchoolId(schoolId, pageLength, (page-1)*pageLength);
	}

	public int countBySchoolId(long schoolId) {
		return scoreBatchMapper.countBySchoolId(schoolId);
	}


	public List<ScoreBatch> selectByParentId(long parentId, int page, int pageLength) {
		return scoreBatchMapper.selectByParentId(parentId, pageLength, (page-1)*pageLength);
	}

	public int countByParentId(long parentId) {
		return scoreBatchMapper.countByParentId(parentId);
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

	public List<ScoreBatch> selectByState(int state, int limit) {
		return scoreBatchMapper.selectByState(state, limit);
	}

	public void updateBatch(List<Long> ids, int state, Date fixTime) {
		scoreBatchMapper.updateBatch(ids, state, fixTime);
	}

	public void checkPermission(User loginUser, ScoreBatch scoreBatch) {
		CommonUtil.checkNull(scoreBatch, "找不到的批次");

		if(scoreBatch.getSchoolId() == null || scoreBatch.getSchoolId() <= 0) {
			throw new InvalidArgumentException("学校不能为空");
		}

		//需要学校管理员权限
		if(scoreBatch.getClassId() == null || scoreBatch.getClassId() <= 0) {
			scoreBatch.setParentId(0L);
			loginUser.checkLevel(Constant.Role.School, scoreBatch.getSchoolId(), 0L);
		}else {
			//仅需要班级管理权限
			loginUser.checkLevel(Constant.Role.Teacher, scoreBatch.getSchoolId(), scoreBatch.getClassId());
		}
	}
}
