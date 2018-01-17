package com.yinuo.mapper;

import com.yinuo.bean.ClassStat;
import com.yinuo.bean.StudentStat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StudentStatMapper extends MapperI<StudentStat>{

	public List<StudentStat> selectByStudentId(@Param("studentId") long studentId, @Param("subject") int subject,
                                             @Param("limit") int limit, @Param("offset") int offset);

	public int countByStudentId(@Param("studentId") long studentId, @Param("subject") int subject);

	public void deleteByStudentId(@Param("studentId")long studentId, @Param("subject")int subject);
}
 