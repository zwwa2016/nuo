package com.yinuo.mapper;

import com.yinuo.bean.ScoreBatch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface ScoreBatchMapper extends MapperI<ScoreBatch>{

    public List<ScoreBatch> selectBySchoolId(@Param("schoolId") long schoolId,@Param("type") int type, @Param("limit")int limit, @Param("offset")int offset);

    public int countBySchoolId(@Param("schoolId")long schoolId,@Param("type") int type);

    public List<ScoreBatch> selectByClassId(@Param("classId")long classId,@Param("type") int type, @Param("limit")int limit, @Param("offset")int offset);

    public int countByClassId(@Param("classId")long classId,@Param("type") int type);

    public List<ScoreBatch> selectByExamId(@Param("examId")long examId, @Param("limit")int limit, @Param("offset")int offset);

    public int countByExamId(@Param("examId")long examId);

    public List<ScoreBatch> selectByState(@Param("state")int state, @Param("limit")int limit);

    public void updateBatch(@Param("ids")List<Long> ids, @Param("state")int state, @Param("fixTime")Date fixTime);

    public void insertBatch(@Param("scoreBatches")List<ScoreBatch> scoreBatches);
}
 