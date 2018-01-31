package com.yinuo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.yinuo.bean.UserStudent;

@Component
public interface UserStudentMapper extends MapperI<UserStudent>{

	public List<UserStudent> selectListByUserid(@Param("userId")long userId, @Param("studentId")long studentId);

	public int countListByUserid(@Param("userId")long userId, @Param("studentId")long studentId);

	public List<UserStudent> selectByStudentids(@Param("studentIds")List<Long> studentIds);

	public void deleteByStudentId(@Param("studentId") long studentId);
	
}
 