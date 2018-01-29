package com.yinuo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yinuo.bean.*;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.util.CommonUtil;
import com.yinuo.view.ScoreView;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yinuo.mapper.ScoreMapper;
import com.yinuo.util.DateTool;
import com.yinuo.util.MergerUtil;

@Transactional(rollbackFor = Exception.class)
@Service
public class ScoreService {
	
	@Autowired
	private ScoreMapper scoreMapper;

	@Autowired
	private StudentService studentService;

	@Autowired
	private ClassesService classesService;

	@Autowired
	private ScoreBatchService scoreBatchService;
	
	public long insert(User loginUser, Score score) {
		CommonUtil.setDefaultValue(score);

		Student student = studentService.selectOne(score.getStudentId());
		CommonUtil.checkNull(student, "找不到该学生");

		score.setClassId(student.getClassId());
		loginUser.checkLevel(Constant.Role.Teacher, student.getSchoolId(), student.getClassId());

		if(score.getType() == Constant.ScoreType.Test) {

			ScoreBatch scoreBatch = null;
			if(score.getScoreBatchId() != null && score.getScoreBatchId() > 0) {
				scoreBatch = scoreBatchService.selectOne(score.getScoreBatchId());
			}
			CommonUtil.checkNull(scoreBatch, "找不到考试批次");
			score.setExamId(scoreBatch.getExamId());
			score.setSubject(scoreBatch.getSubject());

		}
		score.setCreateTime(DateTool.standardSdf.format(new Date()));
		scoreMapper.insert(score);
		return score.getId();
	}
	
	public Score selectOne(long id) {
		return scoreMapper.selectOne(id);
	}
	
	public void delete (User loginUser, long id) {
		Score score = selectOne(id);
		Classes classes = classesService.selectOne(score.getClassId());
		CommonUtil.checkNull(classes, "找不到该班级");
		loginUser.checkLevel(Constant.Role.Teacher, classes.getSchoolId(), classes.getId());

		scoreMapper.delete(id);
	}
	
	public void update(User loginUser, Score score) {
		Score src = selectOne(score.getId());
		
		score = (Score) MergerUtil.merger(src, score);

		Classes classes = classesService.selectOne(score.getClassId());
		CommonUtil.checkNull(classes, "找不到该班级");
		loginUser.checkLevel(Constant.Role.Teacher, classes.getSchoolId(), classes.getId());

		score.setManagerId(loginUser.getManager().getId());
		scoreMapper.update(score);
	}
	
	public List<Score> selectListByIds(List<Long> ids) {
		return scoreMapper.selectListByIds(ids);
	}

	public List<Score> selectByStudentId(long studentId, int type, int subject, int page, int pageSize) {
		return scoreMapper.selectByStudentId(studentId, type, subject, pageSize, (page-1) * pageSize);
	}

	public int countByStudentId(long studentId, int type, int subject) {
		return scoreMapper.countByStudentId(studentId, type, subject);
	}


	public List<Score> selectByScoreBatchId(long scoreBatchId, int type, int page, int pageSize) {
		return scoreMapper.selectByScoreBatchId(scoreBatchId, type, pageSize, (page-1) * pageSize);
	}

	public int countByScoreBatchId(long scoreBatchId, int type) {
		return scoreMapper.countByScoreBatchId(scoreBatchId, type);
	}

	public List<Score> selectByExamId(long examId, int subject, long id, int limit) {
		return scoreMapper.selectByExamId(examId, subject, id, limit);
	}

	public void deleteByExamId(long examId, int subject) {
		scoreMapper.deleteByExamId(examId, subject);
	}

	public void insertBatch(List<Score> scores) {
		scoreMapper.insertBatch(scores);
	}

	public int updateRank(long id, int schoolRank, int classRank) {
		return scoreMapper.updateRank(id, schoolRank, classRank);
	}
	public List<ScoreView> convert2View(List<Score> scores) {
		List<ScoreView> views = new ArrayList<ScoreView>();
		if(scores != null && scores.size() > 0) {
			List<Long> scoreBatchIds = CommonUtil.entity(scores, "scoreBatchId", Long.class);
			List<ScoreBatch> scoreBatches = scoreBatchService.selectByIds(scoreBatchIds);
			Map<Long, ScoreBatch> scoreBatchMap = CommonUtil.entityMap(scoreBatches, "id", Long.class);
			for(Score score: scores) {
				views.add(new ScoreView(score, scoreBatchMap.get(score.getScoreBatchId())));
			}
		}
		return views;
	}

}
