package com.yinuo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.yinuo.bean.Score;

@Component
public interface ScoreMapper extends MapperI<Score>{
	
	public List<Score> selectByStudentId(@Param("studentId")long studentId, @Param("type")int type, @Param("subject")int subject,
			@Param("limit")int limit, @Param("offset")int offset);
	
	public int countByStudentId(@Param("studentId")long studentId, @Param("type")int type, @Param("subject")int subject);

	public List<Score> selectByScoreBatchId(@Param("scoreBatchId")long scoreBatchId, @Param("type")int type,
									   @Param("limit")int limit, @Param("offset")int offset);

	public int countByScoreBatchId(@Param("scoreBatchId")long scoreBatchId, @Param("type")int type);

	public List<Score> selectByExamId(@Param("examId")long examId, @Param("subject")int subject, @Param("id")long id, @Param("limit")int limit);

	public void deleteByExamId(@Param("examId")long examId, @Param("subject")int subject);

	public void insertBatch(@Param("scores")List<Score> scores);

	public int updateRank( @Param("id")long id, @Param("schoolRank")int schoolRank, @Param("classRank")int classRank);

	public void deleteByStudentId(@Param("studentId") long studentId);
}
 