package com.yinuo.mapper;

import com.yinuo.bean.Course;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface CourseMapper extends MapperI<Course>{

	public Course selectByClassId(@Param("classId")long classId);
}
 