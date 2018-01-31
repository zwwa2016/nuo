package com.yinuo.bean;

import com.yinuo.util.DateTool;

import java.util.Date;

public class Constant {

	public static class UserStudentRelationship {

		public static final int create = 10;

		public static final int MaxTimes = 10;
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

	public static class Subject {

		//总分
		public static final int ALL = 127;

	}

	public static class ExamState {

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

		public static final int Work = 2;

		public static final int Test = 1;
	}

	public static class RankUpdateType {

		public static final int Sql = 1;

		public static final int Object = 2;
	}

	public static class JedisNames {

		public static final int OneDay = 60 * 60 * 24;

		public static final String Project = "nuo-api";

		public static final String Split = "-";

		public static final String UserStudent = Project + Split + "userStudent" + Split;

		public static final String ClassId = Project + Split + "schoolId" + Split;

		public static final String SchoolId = Project + Split + "classId" + Split;
	}

}
