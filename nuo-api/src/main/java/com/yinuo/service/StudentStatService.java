package com.yinuo.service;

import com.yinuo.bean.Constant;
import com.yinuo.bean.Score;
import com.yinuo.bean.StudentStat;
import com.yinuo.mapper.StudentStatMapper;
import com.yinuo.util.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class StudentStatService {
	
	@Autowired
	private ScoreService scoreService;

	@Autowired
	private StudentStatMapper studentStatMapper;


	public void stat(long studentId, int subject) {
		List<Score> scores = scoreService.selectByStudentId(studentId, Constant.ScoreType.Test, subject, 1, Integer.MAX_VALUE);
		if(scores != null && scores.size() > 0) {

			int high = 0;
			int total = 0;
			int count = 0;
			int average = 0;
			List<Integer> median = new ArrayList<Integer>();

			StudentStat studentStat = new StudentStat();
			studentStat.setStudentId(studentId);
			studentStat.setSubject(subject);

			for(Score score: scores) {
				count++;
				if(high < score.getScore().intValue()) {
					high = score.getScore().intValue();
				}
				total += score.getScore().intValue();
				average = total / count;
				median.add(score.getScore().intValue());
			}
			studentStat.setHighScore(high);
			studentStat.setAverageScore(average);

			//取中位数
			//偶数
			if(median.size() % 2 == 0) {
				int beginIndex = (median.size()-1) / 2;
				int endIndex = (median.size()-1) / 2 + 1;
				int medianScore = (median.get(beginIndex).intValue() + median.get(endIndex).intValue()) / 2;
				studentStat.setMedianScore(medianScore);
			}else {
				int beginIndex = (median.size()-1) / 2;
				int medianScore = median.get(beginIndex).intValue();
				studentStat.setMedianScore(medianScore);
			}
			studentStat.setCreateTime(DateTool.standardSdf.format(new Date()));
			deleteByStudentId(studentId, subject);
			studentStatMapper.insert(studentStat);
		}

	}

	public void deleteByStudentId(long studentId, int subject) {
		studentStatMapper.deleteByStudentId(studentId, subject);
	}

	public StudentStat selectOne(long id) {
		return studentStatMapper.selectOne(id);
	}

	public List<StudentStat> selectByStudentId(long studentId, int subject, int page, int pageLength) {
		return studentStatMapper.selectByStudentId(studentId, subject, pageLength, (page-1) * pageLength);
	}

	public int countByStudentId(long studentId, int subject) {
		return studentStatMapper.countByStudentId(studentId, subject);
	}

}
