package com.yinuo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.yinuo.bean.Score;

@Component
public interface ScoreMapper extends MapperI<Score>{
	
	public List<Score> selectByStudentId(@Param("studentId")long studentId, @Param("type")int type, 
			@Param("limit")int limit, @Param("offset")int offset);
	
	public int countByStudentId(@Param("studentId")long studentId, @Param("type")int type);

	public List<Score> selectByClassId(@Param("classId")long classId, @Param("type")int type,
									   @Param("scoreBatchId")long scoreBatchId, @Param("limit")int limit, @Param("offset")int offset);

	public int countByClassId(@Param("classId")long classId, @Param("type")int type,
							  @Param("scoreBatchId")long scoreBatchId);
}
 