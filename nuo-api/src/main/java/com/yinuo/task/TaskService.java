package com.yinuo.task;

import com.alibaba.fastjson.JSON;
import com.yinuo.bean.Constant;
import com.yinuo.bean.Exam;
import com.yinuo.bean.Score;
import com.yinuo.bean.ScoreBatch;
import com.yinuo.service.*;
import com.yinuo.util.CommonUtil;
import com.yinuo.util.DateTool;
import org.omg.CORBA.COMM_FAILURE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TaskService {

	@Autowired
	private ScoreBatchService scoreBatchService;

	@Autowired
	private ClassStatService classStatService;

	@Autowired
	private StudentStatService studentStatService;

	@Autowired
	private ScoreService scoreService;

	@Autowired
	private ExamService examService;

	private static final long sleepTimes = 1000L * 60 * 1;

	private Logger logger = LoggerFactory.getLogger(TaskService.class);
	
	@Scheduled(cron="0/1 * * * * * ")
    public void taskCycle() throws Exception{

		int scoreBatchCount = 0;
		int examStatCount = 0;
		do {
			logger.info("execute taskCycle");
			scoreBatchCount = classStudentStat();
			examStatCount = examStat();

			if(scoreBatchCount == 0 && examStatCount == 0) {
				Thread.sleep(sleepTimes);
			}
		}while(true);
	}

	/**
	 * 统计学生总分
	 * @return
	 */
	private int examStat() {

		List<Exam> exams = examService.selectByState(Constant.ExamState.WaitStat, 10);
		if(exams == null || exams.size() <= 0) {
			return 0;
		}

		int limit = 3;
		long scoreIndexId = 0L;
		for(Exam exam: exams) {
			//删除本批次总分成绩，再添加
			scoreService.deleteByExamId(exam.getId(), Constant.Subject.ALL);

			List<Score> scores = new ArrayList<Score>();
			scoreLoop: while(true) {
				List<Score> temp = scoreService.selectByExamId(exam.getId(), 0, scoreIndexId, limit);
				if(temp == null || temp.size() <= 0) {
					break scoreLoop;
				}
				for(Score score: temp) {
					if(score.getType() == Constant.ScoreType.Test) {
						scores.add(score);
					}
				}
				scoreIndexId = temp.get(temp.size()-1).getId();
			}

			if(scores.size() > 0) {
				String now = DateTool.standardSdf.format(new Date());
				//更新分科rank
				updateRank(scores, Constant.RankUpdateType.Sql);

				//学生总分
				Map<Long, Score> allScoreMap = new HashMap<Long, Score>();
				for(Score score: scores) {
					if(allScoreMap.get(score.getStudentId()) == null) {
						Score studentAllScore = new Score();
						studentAllScore.setSubject(Constant.Subject.ALL);
						studentAllScore.setExamId(exam.getId());
						studentAllScore.setManagerId(0L);
						studentAllScore.setClassId(score.getClassId());
						studentAllScore.setCreateTime(now);
						studentAllScore.setPic("");
						studentAllScore.setScore(score.getScore());
						studentAllScore.setScoreBatchId(0L);
						studentAllScore.setStudentId(score.getStudentId());
						studentAllScore.setType(Constant.ScoreType.Test);
						allScoreMap.put(score.getStudentId(), studentAllScore);
					}else {
						int allScore = allScoreMap.get(score.getStudentId()).getScore() + score.getScore();
						allScoreMap.get(score.getStudentId()).setScore(allScore);
					}
				}

				List<Score> allScores= new ArrayList<Score>();
				for(Long studentId: allScoreMap.keySet()) {
					allScores.add(allScoreMap.get(studentId));
				}

				Collections.sort(allScores, new Comparator<Score>() {
					@Override
					public int compare(Score o1, Score o2) {
						return o1.getScore() >= o2.getScore() ? -1 : 1;
					}
				});

				//更新总分rank
				updateRank(allScores, Constant.RankUpdateType.Object);
				//批量插入
				List<Score> temp = new ArrayList<Score>();
				for(Score score: allScores) {
					temp.add(score);
					if(temp.size() % limit == 0) {
						scoreService.insertBatch(temp);
						temp = new ArrayList<Score>();
					}
				}
				if(temp.size() > 0) {
					scoreService.insertBatch(temp);
				}
			}

			exam.setState(Constant.ExamState.Done);
			examService.fixExam(exam.getId());
			logger.info("exam state convert to done, id/name: {}/{}", exam.getId(), exam.getName());
		}
		return exams.size();
	}

	/**
	 * 学生班级统计
	 * @return
	 */
	private int classStudentStat() {
		int scoreBatchCount = 0;
		List<ScoreBatch> scoreBatches = scoreBatchService.selectByState(Constant.ScoreBatchState.WaitStat, 10);
		scoreBatchCount = scoreBatches == null ? 0 : scoreBatches.size();
		if(scoreBatchCount > 0) {
			List<Long> scoreBatchIds = new ArrayList<Long>();
			scoreBatchLoop: for(ScoreBatch scoreBatch: scoreBatches) {
				if(scoreBatch.getClassId() == null || scoreBatch.getClassId().intValue() == 0) {
					continue scoreBatchLoop;
				}
				scoreBatchIds.add(scoreBatch.getId());
				//班级统计
				classStatService.stat(scoreBatch.getClassId(), scoreBatch.getId());
				logger.info("class stat done, classId/scoreBatchId: {}/{}", scoreBatch.getClassId(), scoreBatch.getId());

				//学生统计
				List<Score> scores = scoreService.selectByScoreBatchId(scoreBatch.getId(), Constant.ScoreType.Test, 1, Integer.MAX_VALUE);
				if(scores != null && scores.size() > 0) {
					for(Score score: scores) {
						studentStatService.stat(score.getStudentId(), scoreBatch.getSubject());
						logger.info("student stat done, studentId/subject: {}/{}", score.getStudentId(), scoreBatch.getSubject());
					}
				}
			}
			scoreBatchService.updateBatch(scoreBatchIds, Constant.ScoreBatchState.Done, new Date());
		}
		return scoreBatchCount;
	}

	private void updateRank(List<Score> scores, int type) {
		if(scores == null || scores.size() <= 0) {
			return;
		}
		Collections.sort(scores, new Comparator<Score>() {
			@Override
			public int compare(Score o1, Score o2) {
				if(o1.getSubject().intValue() == o2.getSubject().intValue()) {
					return o1.getScore() >= o2.getScore() ? -1 : 1;
				}
				return o1.getSubject() >= o2.getSubject() ? 1 : -1;
			}
		});

		long classId = -1L;
		int subject = -1,schoolScore = -1,schoolRank = -1,
				schoolIndex = -1;

		Map<String, Integer> classScore = new HashMap<String, Integer>();
		Map<String, Integer> classRank = new HashMap<String, Integer>();
		Map<String, Integer> classIndex = new HashMap<String, Integer>();

		for(Score score: scores) {

			String key = score.getClassId() + "_" + score.getSubject();

			if(classScore.get(key) == null) {
				classScore.put(key, -1);
				classRank.put(key, 0);
				classIndex.put(key, 0);
			}

			//更换科目，初始化参数
			if(score.getSubject().intValue() != subject) {
				classId = -1;
				subject = score.getSubject();
				schoolScore = -1;
				schoolRank = 0;
				schoolIndex = 0;
			}

			if(score.getScore().intValue() != schoolScore) {
				schoolRank = schoolIndex + 1;
			}

			if(score.getScore().intValue() != classScore.get(key).intValue()) {
				classRank.put(key, classIndex.get(key) + 1);
			}

			if(type == Constant.RankUpdateType.Sql) {
				scoreService.updateRank(score.getId(), schoolRank, classRank.get(key));
			}else if(type == Constant.RankUpdateType.Object) {
				score.setSchoolRank(schoolRank);
				score.setClassRank(classRank.get(key));
			}
			schoolScore = score.getScore();
			classScore.put(key, score.getScore());
			schoolIndex++;
			classIndex.put(key, classIndex.get(key) + 1);
		}



	}

}
