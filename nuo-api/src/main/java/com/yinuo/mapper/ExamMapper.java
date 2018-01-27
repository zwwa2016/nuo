package com.yinuo.mapper;

import com.yinuo.bean.Exam;
import com.yinuo.bean.ScoreBatch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ExamMapper extends MapperI<Exam>{

    public List<Exam> selectBySchoolId(@Param("schoolId") long schoolId, @Param("limit") int limit, @Param("offset") int offset);

    public int countBySchoolId(@Param("schoolId") long schoolId);

    public List<Exam> selectByState(@Param("state") int state, @Param("limit") int limit);


}
 