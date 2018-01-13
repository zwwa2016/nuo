package com.yinuo.mapper;

import com.yinuo.bean.Classes;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ClassesMapper extends MapperI<com.yinuo.bean.Classes>{
	
	public List<Classes> selectBySchoolId(@Param("schoolId") long schoolId, @Param("limit") int limit, @Param("offset") int offset);

	public int countBySchoolId(@Param("schoolId") long schoolId);
}
 