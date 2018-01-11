package com.yinuo.mapper;

import com.yinuo.bean.UserStudent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.yinuo.bean.Student;

import java.util.List;

@Component
public interface StudentMapper extends MapperI<Student>{

    public List<Student> selectByName(@Param("name") String name);

    public int countByName(@Param("name") String name);

    public List<Student> selectByClassid(@Param("classId")long classId, @Param("limit")int limit, @Param("offset")int offset);

    public int countByClassid(@Param("classId")long classId);

}
 