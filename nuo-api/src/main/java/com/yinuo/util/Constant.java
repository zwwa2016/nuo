package com.yinuo.util;

/**
 * 系统配置，一经定义只能添加，不能修改！
 */
public class Constant {
	
	public static class OpCode {
		public static final short SUCCESS = 200;
		public static final short INVALID_PARAMS = 400;
		public static final short NEED_AUTHORIZATION = 401;
		public static final short NOT_PERMITED = 403;
		public static final short RESOURCE_NOT_FOUND = 404;
		public static final short INTERNAL_EXCEPTION = 500;
	}
	
	public static class RedisNameSpace {
		public static final String NAMESPACE = "nuo-api";
		
		public static final String LOGIN = NAMESPACE+"_login_user_id_";
		
		public static final String OPENID = NAMESPACE+"_openid_";
		
		//登陆有效期
		public static final String LOGIN_URL = "http://www.kehue.com/login.html";
		
		//登陆有效期
		public static final int LOGIN_TIME = 60 * 30;
	}
	
}























