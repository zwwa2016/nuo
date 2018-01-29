package com.yinuo.service;

import com.yinuo.bean.*;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.mapper.ExamMapper;
import com.yinuo.util.CommonUtil;
import com.yinuo.util.DateTool;
import com.yinuo.util.MergerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class ExamService {
	
	@Autowired
	private ExamMapper examMapper;

	@Autowired
	private ClassesService classesService;

	@Autowired
	private ScoreBatchService scoreBatchService;
	
	public long insert(User loginUser, Exam exam) {
		checkPermission(loginUser, exam);
		CommonUtil.setDefaultValue(exam);

		exam.setManagerId(loginUser.getManager().getId());
		exam.setCreateTime(DateTool.standardSdf.format(new Date()));
		exam.setFixTime(Constant.Time.Zone);
		exam.setState(Constant.ExamState.Create);
		examMapper.insert(exam);

		List<ScoreBatch> scoreBatches = new ArrayList<ScoreBatch>();
		List<Classes> classes = classesService.selectBySchoolId(exam.getSchoolId(), exam.getGrade(), 1, Integer.MAX_VALUE);
		if(classes != null && classes.size() > 0) {
			for (Classes temp : classes) {
				for (String subject : exam.getSubjects().split(",")) {
					if (subject != null && subject.length() > 0) {
						ScoreBatch scoreBatch = new ScoreBatch(exam.getId(), exam.getSchoolId(), temp.getId(), Integer.parseInt(subject), Constant.ScoreType.Test);
						scoreBatches.add(scoreBatch);
					}
				}
			}
		}
		if(scoreBatches.size() > 0) {
			scoreBatchService.insertBatch(loginUser, scoreBatches);
		}
		return exam.getId();
	}
	
	public Exam selectOne(long id) {
		return examMapper.selectOne(id);
	}
	
	public void delete (User loginUser, long id) {
		int count = scoreBatchService.countByExamId(id);
		if(count > 0) {
			throw new InvalidArgumentException("请先删除该次考试归属的批次信息");
		}

		Exam exam = selectOne(id);
		checkPermission(loginUser, exam);
		examMapper.delete(id);
	}
	
	public void update(User loginUser, Exam exam) {
		Exam src = selectOne(exam.getId());

		exam = (Exam) MergerUtil.merger(src, exam);

		checkPermission(loginUser, exam);

		if(exam.getState() == Constant.ExamState.WaitStat && src.getState() != Constant.ExamState.WaitStat) {
			List<ScoreBatch> scoreBatches = scoreBatchService.selectByExamId(exam.getId(), 1, Integer.MAX_VALUE);

			if(scoreBatches!=null && scoreBatches.size() > 0) {
				List<Long> classIds = new ArrayList<Long>();
				for(ScoreBatch scoreBatch: scoreBatches) {
					if(scoreBatch.getState() != Constant.ScoreBatchState.Done) {
						classIds.add(scoreBatch.getClassId());
					}
				}
				if(classIds.size() > 0) {
					List<Classes> classes = classesService.selectListByIds(classIds);
					StringBuilder sb = new StringBuilder();
					for(Classes temp: classes) {
						sb.append(String.format("%d年级%d班, ", temp.getGrade(), temp.getNumber()));
					}
					throw new InvalidArgumentException("等待以下班级统计结束："+sb.toString());
				}
			}

			exam.setFixTime(Constant.Time.Now());
			exam.setFixManagerId(loginUser.getManager().getId());
		}
		examMapper.update(exam);
	}

	public List<Exam> selectBySchoolId(long schoolId, int page, int pageLength) {
		return examMapper.selectBySchoolId(schoolId, pageLength, (page-1)*pageLength);
	}

	public int countBySchoolId(long schoolId) {
		return examMapper.countBySchoolId(schoolId);
	}

	public List<Exam> selectByIds(List<Long> ids) {
		return examMapper.selectListByIds(ids);
	}

	public List<Exam> selectByState(int state, int limit) {
		return examMapper.selectByState(state, limit);
	}

	public void fixExam(long id) {
		Exam exam = examMapper.selectOne(id);
		exam.setState(Constant.ExamState.Done);
		exam.setFixTime(DateTool.standardSdf.format(new Date()));
		examMapper.update(exam);
	}

	public void checkPermission(User loginUser, Exam exam) {
		CommonUtil.checkNull(exam, "找不到的考试");

		if(exam.getSchoolId() == null || exam.getSchoolId() <= 0) {
			throw new InvalidArgumentException("学校不能为空");
		}

		//需要班级管理权限
		loginUser.checkLevel(Constant.Role.School, exam.getSchoolId(), 0L);
	}
}
