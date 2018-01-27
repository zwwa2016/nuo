package com.yinuo.view;

import com.yinuo.bean.Classes;
import com.yinuo.bean.School;
import com.yinuo.util.DateTool;

import java.util.HashMap;
import java.util.List;

public class ManagerView extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public ManagerView(School school, List<Classes> classes) {
		if(school == null) {
			return;
		}
		
		put("id", school.getId());

		put("name", school.getName());
		put("managerId", school.getManagerId());

		if(school.getCreateTime() != null) {
			put("createTime", DateTool.standardSdf.format(school.getCreateTime()));
		}

		if(classes != null && classes.size() > 0) {
			put("classes", classes);
		}
	}
}
