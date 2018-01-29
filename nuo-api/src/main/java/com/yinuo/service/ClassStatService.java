package com.yinuo.service;

import com.yinuo.bean.ClassStat;
import com.yinuo.bean.Score;
import com.yinuo.bean.ScoreBatch;
import com.yinuo.mapper.ClassStatMapper;
import com.yinuo.util.DateTool;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class ClassStatService {
	
	@Autowired
	private ClassStatMapper classStatMapper;

	@Autowired
	private ScoreBatchService scoreBatchService;

	@Autowired
	private ScoreService scoreService;
	
	public void stat(long classId, long scoreBatchId) {
		if(classId <= 0L || scoreBatchId <= 0L) {
			return ;
		}

		ScoreBatch scoreBatch = scoreBatchService.selectOne(scoreBatchId);

		List<Score> scores = scoreService.selectByScoreBatchId(scoreBatchId, 0, 1, Integer.MAX_VALUE);
		if(scores != null && scores.size() > 0) {

			int high = 0;
			int total = 0;
			int count = 0;
			int average = 0;
			List<Integer> median = new ArrayList<Integer>();

			ClassStat classStat = new ClassStat();
			classStat.setScoreBatchId(scoreBatchId);
			classStat.setSubject(scoreBatch.getSubject());
			classStat.setSchoolId(scoreBatch.getSchoolId());
			classStat.setClassId(scoreBatch.getClassId());

			for(Score score: scores) {
				count++;
				if(high < score.getScore().intValue()) {
					high = score.getScore().intValue();
				}
				total += score.getScore().intValue();
				average = total / count;
				median.add(score.getScore().intValue());
			}
			classStat.setHighScore(high);
			classStat.setAverageScore(average);

			//取中位数
			//偶数
			if(median.size() % 2 == 0) {
				int beginIndex = (median.size()-1) / 2;
				int endIndex = (median.size()-1) / 2 + 1;
				int medianScore = (median.get(beginIndex).intValue() + median.get(endIndex).intValue()) / 2;
				classStat.setMedianScore(medianScore);
			}else {
				int beginIndex = (median.size()-1) / 2;
				int medianScore = median.get(beginIndex).intValue();
				classStat.setMedianScore(medianScore);
			}
			classStat.setCreateTime(DateTool.standardSdf.format(new Date()));
			deleteByBatchId(classId, scoreBatchId);
			classStatMapper.insert(classStat);
		}
	}

	public void deleteByBatchId(long classId, long scoreBatchId) {
		classStatMapper.deleteByBatchId(classId, scoreBatchId);
	}

	public List<ClassStat> selectBySchoolId(long schoolId, long scoreBatchId, long classId, int page, int pageLength) {
		return classStatMapper.selectBySchoolId(schoolId, scoreBatchId, classId, pageLength, (page-1) * pageLength);
	}

	public int countBySchoolId(long schoolId, long scoreBatchId, long classId) {
		return classStatMapper.countBySchoolId(schoolId, scoreBatchId, classId);
	}

	public ClassStat selectOne(long id) {
		return classStatMapper.selectOne(id);
	}
}
