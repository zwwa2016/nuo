package com.yinuo.service;

import com.yinuo.bean.Constant;
import com.yinuo.bean.Score;
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

		if(scoreBatch.getType() == Constant.ScoreType.Test) {
			scoreBatch.setState(Constant.ScoreBatchState.Create);
		}else if(scoreBatch.getType() == Constant.ScoreType.Work){
			scoreBatch.setState(Constant.ScoreBatchState.Done);
			scoreBatch.setExamId(0L);
		}else {
			throw new InvalidArgumentException("必须选择类型");
		}

		scoreBatch.setManagerId(loginUser.getManager().getId());
		scoreBatch.setCreateTime(DateTool.standardSdf.format(new Date()));
		scoreBatch.setFixTime(Constant.Time.Zone);
		scoreBatchMapper.insert(scoreBatch);
		return scoreBatch.getId();
	}

	public void insertBatch(User loginUser, List<ScoreBatch> scoreBatches) {
		if(scoreBatches == null || scoreBatches.size() <= 0) {
			return;
		}

		for(ScoreBatch scoreBatch: scoreBatches) {
			checkPermission(loginUser, scoreBatch);
			CommonUtil.setDefaultValue(scoreBatch);
			scoreBatch.setManagerId(loginUser.getManager().getId());
			scoreBatch.setCreateTime(DateTool.standardSdf.format(new Date()));
			scoreBatch.setFixTime(Constant.Time.Zone);
			scoreBatch.setState(Constant.ScoreBatchState.Create);
		}
		scoreBatchMapper.insertBatch(scoreBatches);
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
			scoreBatch.setFixManagerId(loginUser.getManager().getId());
		}
		scoreBatchMapper.update(scoreBatch);
	}
	public List<ScoreBatch> selectByClassId(long classId, int type, int page, int pageLength) {
		return scoreBatchMapper.selectByClassId(classId, type, pageLength, (page-1)*pageLength);
	}

	public int countByClassId(long classId, int type) {
		return scoreBatchMapper.countByClassId(classId, type);
	}

	public List<ScoreBatch> selectByExamId(long examId, int page, int pageLength) {
		return scoreBatchMapper.selectByExamId(examId, pageLength, (page-1)*pageLength);
	}

	public int countByExamId(long examId) {
		return scoreBatchMapper.countByExamId(examId);
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

		//需要班级管理权限
		loginUser.checkLevel(Constant.Role.Teacher, scoreBatch.getSchoolId(), scoreBatch.getClassId());
	}
}
