package com.yinuo.bean;

import com.yinuo.util.DateTool;

import java.util.Date;

public class Constant {

	public static class UserStudentRelationship {

		public static final int create = 10;

		public static final int MaxTimes = 3;
	}

	public static class Role {

		public static final int Manager = 1;

		public static final int School = 2;

		public static final int Teacher = 3;
	}

	public static class ScoreBatchState {

		public static final int Create = 1;

		public static final int WaitStat = 2;

		public static final int Done = 3;
	}

	public static class Time {

		public static final String Zone = "1970-01-01 08:00:00";

		public static final String Now() {
			Date now = new Date();
			return DateTool.standardSdf.format(now);
		}
	}


	public static class ScoreType {

		public static final int Work = 1;

		public static final int Test = 1;
	}

}
