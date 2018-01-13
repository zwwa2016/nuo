package com.yinuo.mapper;

import com.yinuo.bean.ScoreBatch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ScoreBatchMapper extends MapperI<ScoreBatch>{


    public List<ScoreBatch> selectBySchoolId(@Param("schoolId") long schoolId, @Param("limit")int limit, @Param("offset")int offset);

    public int countBySchoolId(@Param("schoolId")long schoolId);

    public List<ScoreBatch> selectByClassId(@Param("classId")long classId, @Param("limit")int limit, @Param("offset")int offset);

    public int countByClassId(@Param("classId")long classId);
}
 