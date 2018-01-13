package com.yinuo.view;

import com.yinuo.bean.Score;
import com.yinuo.bean.ScoreBatch;
import com.yinuo.bean.Student;
import com.yinuo.bean.UserStudent;
import com.yinuo.util.DateTool;

import java.util.HashMap;

public class ScoreView extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public ScoreView(Score score, ScoreBatch scoreBatch) {
		if(score == null) {
			return;
		}

		put("id", score.getId());
		put("type", score.getType());
		put("studentId", score.getStudentId());
		put("classId", score.getClassId());
		put("score", score.getScore());
		put("pic", score.getPic());
		put("scoreBatchId", score.getScoreBatchId());
		put("managerId", score.getManagerId());

		if(score.getCreateTime() != null) {
			put("createTime", DateTool.standardSdf.format(score.getCreateTime()));
		}

		if(scoreBatch != null) {
			put("scoreBatch", scoreBatch);
		}
	}
	
}
