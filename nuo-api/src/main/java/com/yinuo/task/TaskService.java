package com.yinuo.task;

import com.yinuo.bean.Constant;
import com.yinuo.bean.Score;
import com.yinuo.bean.ScoreBatch;
import com.yinuo.service.ClassStatService;
import com.yinuo.service.ScoreBatchService;
import com.yinuo.service.ScoreService;
import com.yinuo.service.StudentStatService;
import com.yinuo.util.CommonUtil;
import org.omg.CORBA.COMM_FAILURE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	private static final long sleepTimes = 1000L * 60 * 5;

	private Logger logger = LoggerFactory.getLogger(TaskService.class);
	
	@Scheduled(cron="0/1 * * * * * ")
    public void taskCycle() throws Exception{

		int scoreBatchCount = 0;
		do {
			logger.info("execute taskCycle");
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

			if(scoreBatchCount == 0) {
				Thread.sleep(sleepTimes);
			}
		}while(true);
	}
	
}
