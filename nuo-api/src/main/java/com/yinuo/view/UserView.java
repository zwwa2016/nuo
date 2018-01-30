package com.yinuo.view;

import java.util.HashMap;

import com.yinuo.bean.ManagerClass;
import com.yinuo.bean.User;
import com.yinuo.util.DateTool;

public class UserView extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public UserView(User user) {
		if(user == null) {
			return;
		}
		
		put("id", user.getId());
		put("sex", user.getSex());
		put("wechatNickname", user.getWechatNickname());
		put("wechatOpenid", user.getWechatOpenid());
		put("birthday", user.getBirthday());

		if(user.getCreateTime() != null) {
			put("createTime", DateTool.standardSdf.format(user.getCreateTime()));
		}

		if(user.getCreateTime() != null) {
			put("updateTime", DateTool.standardSdf.format(user.getUpdateTime()));
		}

		put("avatarUrl", user.getAvatarUrl());
		put("city", user.getCity());
		put("country", user.getCountry());
		put("province", user.getProvince());
		put("manager", user.getManager());
		put("managerClass", user.getManagerClassList());
		put("role", 0);
		if(user.getManagerClassList() != null && user.getManagerClassList().size() > 0) {
			int role = Integer.MAX_VALUE;
			for(ManagerClass managerClass: user.getManagerClassList()) {
				if(managerClass.getRole().intValue() < role) {
					role = managerClass.getRole().intValue();
				}
			}
			put("role", role);
		}
	}
	
}
