package com.yinuo.mapper;

import com.yinuo.bean.ClassStat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ClassStatMapper extends MapperI<ClassStat>{
	
	public List<ClassStat> selectBySchoolId(@Param("schoolId") long schoolId, @Param("scoreBatchId") long scoreBatchId,
                                            @Param("classId") long classId, @Param("limit") int limit, @Param("offset") int offset);

	public int countBySchoolId(@Param("schoolId") long schoolId, @Param("scoreBatchId") long scoreBatchId,
							   @Param("classId") long classId);

	public void deleteByBatchId(@Param("classId")long classId, @Param("scoreBatchId")long scoreBatchId);
}
 