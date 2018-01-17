package com.yinuo.mapper;

import com.yinuo.bean.ScoreBatch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface ScoreBatchMapper extends MapperI<ScoreBatch>{

    public List<ScoreBatch> selectBySchoolId(@Param("schoolId") long schoolId, @Param("limit")int limit, @Param("offset")int offset);

    public int countBySchoolId(@Param("schoolId")long schoolId);

    public List<ScoreBatch> selectByParentId(@Param("parentId") long parentId, @Param("limit")int limit, @Param("offset")int offset);

    public int countByParentId(@Param("parentId")long parentId);

    public List<ScoreBatch> selectByClassId(@Param("classId")long classId, @Param("limit")int limit, @Param("offset")int offset);

    public int countByClassId(@Param("classId")long classId);

    public List<ScoreBatch> selectByState(@Param("state")int state, @Param("limit")int limit);

    public void updateBatch(@Param("ids")List<Long> ids, @Param("state")int state, @Param("fixTime")Date fixTime);
}
 