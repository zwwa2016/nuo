package com.yinuo.task;

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

	private static final long sleepTimes = 1000L * 60 * 5;

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

		int limit = 500;
		long scoreIndexId = 0L;
		for(Exam exam: exams) {
			List<Score> scores = new ArrayList<Score>();
			scoreLoop: while(true) {
				List<Score> temp = scoreService.selectByExamId(exam.getId(), scoreIndexId, limit);
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
				//学生总分
				Map<Long, Score> scoreMap = new HashMap<Long, Score>();
				for(Score score: scores) {
					if(scoreMap.get(score.getStudentId()) == null) {
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
					}else {
						int allScore = scoreMap.get(score.getStudentId()).getScore() + score.getScore();
						scoreMap.get(score.getStudentId()).setScore(allScore);
					}
				}

				List<Score> allScores= new ArrayList<Score>();
				for(Long studentId: scoreMap.keySet()) {
					allScores.add(scoreMap.get(studentId));
				}

				Collections.sort(allScores, new Comparator<Score>() {
					@Override
					public int compare(Score o1, Score o2) {
						return o1.getScore() >= o2.getScore() ? 1 : -1;
					}
				});

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

				//学生统计
				List<Score> scores = scoreService.selectByClassId(scoreBatch.getClassId(), Constant.ScoreType.Test, scoreBatch.getId(), 1, Integer.MAX_VALUE);
				if(scores != null && scores.size() > 0) {
					for(Score score: scores) {
						studentStatService.stat(score.getStudentId(), scoreBatch.getSubject());
					}
				}
			}
			scoreBatchService.updateBatch(scoreBatchIds, Constant.ScoreBatchState.Done, new Date());
		}
		return scoreBatchCount;
	}
	
}
