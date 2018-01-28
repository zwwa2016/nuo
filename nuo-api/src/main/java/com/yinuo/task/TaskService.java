package com.yinuo.task;

import com.yinuo.bean.Constant;
import com.yinuo.bean.Exam;
import com.yinuo.bean.Score;
import com.yinuo.bean.ScoreBatch;
import com.yinuo.service.*;
import com.yinuo.util.CommonUtil;
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
		int count = exams == null ? 0 : exams.size();
		for(Exam exam: exams) {
			List<ScoreBatch> scoreBatches = scoreBatchService.selectByExamId(exam.getId(), 1, Integer.MAX_VALUE);
			if(scoreBatches != null && scoreBatches.size()>0) {
				List<Score> scores = new ArrayList<Score>();
				//按照班级分组，每个班级单独处理
				Map<Long, List<ScoreBatch>> classMap = new HashMap<Long, List<ScoreBatch>>();
				for(ScoreBatch scoreBatch: scoreBatches) {
					if(classMap.get(scoreBatch.getClassId()) == null) {
						classMap.put(scoreBatch.getClassId(), new ArrayList<ScoreBatch>());
					}
					classMap.get(scoreBatch.getClassId()).add(scoreBatch);
				}

				//分班统计不同学生总分



			}



		}



		return count;
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
