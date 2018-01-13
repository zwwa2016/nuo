package com.yinuo.mapper;

import com.yinuo.bean.School;
import org.springframework.stereotype.Component;

@Component
public interface SchoolMapper extends MapperI<School>{

	public int countAll();
}
 