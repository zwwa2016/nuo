# nuo


接口设计

环境ip：47.100.24.40 端口：80
base url: www.kehue.com/nuo/

wechat登陆
	request/get: /wechatLogin?code=${res.code}

		{
			"id": "e4911dba-e7f7-4e08-a59c-4d243355098d",
			"loginUser": {
				"birthday": "1970-01-01 08:00:00",
				"createTime": "2017-11-08 20:48:18",
				"id": 1,
				"sex": 0,
				"wechatNickname": "",
				"wechatOpenid": "onIX60AX6xG8RsqnPr1b",
				"avatarUrl": "https://wx.qlogo.cn/mmopen/vi_32/loVyFiaRialvJRVMTqMWSxnGVj4ibSEAgiasLRicIGFlcoibA5mYGqabHzzoPJH4w3HZDsrXRiaQ1QBNYUfHSEywwmtQQ/0",
				"city": "",
				"country": "China",
				"province": "Beijing",
				"role":1,
				"schoolId":1,
				"classId":1
			}
		}
	
		以后每次请求，在header中带上，key:ticket, value:${id}

用户信息

	更新：request/put: /users

		{
			"id": 1,
			"birthday": "1970-01-01 08:00:00",
			"sex": 1,	//1男，2女
			"wechatNickname": "微信昵称",
			"avatarUrl": "https://wx.qlogo.cn/mmopen/vi_32/loVyFiaRialvJRVMTqMWSxnGVj4ibSEAgiasLRicIGFlcoibA5mYGqabHzzoPJH4w3HZDsrXRiaQ1QBNYUfHSEywwmtQQ/0",
			"city": "",
			"country": "China",
			"province": "Beijing"
		}

	response:
		{
			"id": 1
		}

	查询：request/get: /users

		{
		    "code": 200,
		    "cost": 0.776,
		    "loginUser": {
		        "id": 22,
		        "createTime": "2018-01-09 21:17:16",
		        "birthday": 0,
		        "wechatNickname": "凭栏处、潇潇雨歇",
		        "sex": 1,
		        "manager": {
		            "id": 1,
		            "name": "cls",
		            "userId": 22,
		            "managerId": 1,
		            "createTime": "1970-01-01 08:00:00"
		        },
		        "avatarUrl": "https://wx.qlogo.cn/mmopen/vi_32/icKVaJJ7o9BLp6B70D7gm7gDtWB9QDeibwbn5oNuwrnc0rWsiaPnDUBW8icly1aK1IDOTzl7uE7aZsHYKsI6WficxeA/0",
		        "province": "Beijing",
		        "managerClass": [
		            {
		                "id": 1,
		                "managerId": 1,
		                "schoolId": 1,
		                "classId": 1,
		                "createId": 1,
		                "role": 1,
		                "createTime": "1970-01-01 08:00:00"
		            }
		        ],
		        "wechatOpenid": "oUG0W0b1mJkwrVKDtfIJcIAMEYZw",
		        "country": "China",
		        "city": ""
		    }
		}

学生信息

	创建：request/post: /students

		{
			"name": "abcd",
			"birthday": "1993-08-11 00:00:00",
			"sex": 1,
			"pic": "www.pic.com/pic.jpg",
			"schoolId": 1,
			"classId": 1
		}

	更新：request/put: /students
		{
			"id": 8,
			"name": "abc",
			"birthday": "1993-08-11 00:00:00",
			"sex": 1,
			"pic": "www.pic.com/pic.jpg",
			"classId": 0,
			"schoolId": 1
		}

	删除：request/delete: /students?id=1

	查询：request/get: 	/students?page=1&pageLength=10	/查询当前登录者绑定的信息
						/students?name=abc	/按照学生名查询
						/students?classId=3	/按照班级ID查询，需要至少老师权限

		response: 
		{
		    "count": 1,
		    "data": [
		        {
		            "id": 8,
		            "createTime": "2018-01-11 22:18:59",
		            "birthday": "1993-08-11 00:00:00",
		            "sex": 1,
		            "classId": 3,
		            "managerId": 1,
		            "name": "abc",
		            "schoolId": 1,
		            "pic": "www.pic.com/pic.jpg",
		            "userStudent": {
		                "id": 42,
		                "studentId": 8,
		                "userId": 22,
		                "relationship": 10,
		                "schoolId": 1,
		                "classId": 1,
		                "createTime": "2018-01-11 22:18:59"
		            }
		        }
		    ],
		    "code": 200,
		    "cost": 0.336
		}

绑定学生信息
	
	绑定：request/post: /userStudents

		{
			"studentId": 8,
			"relationship": 2	//关系，1自己，2父亲，3母亲，4爷爷，5奶奶，6姑姑，7姑父，8姨，9姨父, 10创建者
		}
	
	解绑：request/delete: /userStudents?id=43


//以下未测试-----------------------------------

班级信息
	
	新增：request/post: /classes

		{
			"grade": 1,		//年纪1-6
			"number": 2,	//班号01-99
			"schoolId": 1	//归属学校ID
		}

	删除：request/delete: /classes?id=1
	
	修改：request/put: /classes

		{
			"id": 1,
			"grade": 1,		//年纪1-6
			"number": 2,	//班号01-99
			"schoolId": 1	//归属学校ID
		}

	查询：request/get: /classes?id=1
						/classes?page=1&pageSize=10

		{
		    "count": 1,
		    "data": [
		        {
		            "id": 1,
		            "grade": 1,
		            "number": 2,
		            "schoolId": 1,
		            "createTime": "2017-12-07 20:46:07"
		        }
		    ],
		    "code": 200,
		    "cost": 5.054
		}

课程信息

	新增：request/post: /courses

		{
			"name": "语文课",	
			"beginTime": "2017-01-01 08:00:00",		
			"endTime": "2017-01-01 08:00:00",	
			"classId": 1,	
			"remark": "很厉害的外教课程"
		}

	删除：request/delete: /courses?id=1
	
	修改：request/put: /courses

		{
			"id": 1,
			"name": "语文课",	
			"beginTime": "2017-01-01 08:00:00",		
			"endTime": "2017-01-01 08:00:00",	
			"classId": 1,	
			"remark": "很厉害的外教课程"
		}

	查询：request/get: /courses

		按照班级时间查询 /courses?classId=1&beginTime=2017-01-01 00:00:00&endTime=2018-01-01 00:00:00
		按照ID查询 /courses?id=1
		按照学生id查询 /courses?studentId=1&beginTime=2017-01-01 00:00:00&endTime=2018-01-01 00:00:00

		{
		    "count": 2,
		    "data": [
		        {
		            "id": 1,
		            "name": "语文课",
		            "beginTime": "2017-01-01 08:00:00",
		            "endTime": "2017-01-01 08:00:00",
		            "classId": 1,
		            "teacherId": 1,
		            "remark": "很厉害的外教课程",
		            "createTime": "2017-12-08 20:42:14"
		        },
		        {
		            "id": 2,
		            "name": "语文课2",
		            "beginTime": "2017-01-01 08:00:00",
		            "endTime": "2017-01-01 08:00:00",
		            "classId": 1,
		            "teacherId": 1,
		            "remark": "很厉害的外教课程",
		            "createTime": "2017-12-08 20:42:21"
		        }
		    ],
		    "code": 200,
		    "cost": 1.648
		}

分数信息

	新增：request/post: /scores
		
		{
			"name": "期中考试语文20170812",	
			"type": 1,		
			"studentId": 1,	
			"score": 87,	
			"pic": "http://www.baidu.com/1.jpg"
		}

		type>1考试，2作业

	删除：request/delete: /scores?id=1

	修改：request/put: /scores

		{
			"id": 1,
			"name": "期中考试语文20170812",	
			"type": 1,		
			"studentId": 1,	
			"score": 87,	
			"pic": "http://www.baidu.com/1.jpg"
		}

	查询：request/get: /scores?studentId=1&type=1&page=1&pageSize=10
						/scores?id=1

		{
		    "count": 2,
		    "data": [
		        {
		            "id": 2,
		            "name": "期中考试数学1",
		            "type": 1,
		            "studentId": 1,
		            "score": 87,
		            "pic": "http://www.baidu.com/1.jpg",
		            "createTime": "2017-12-09 01:28:48"
		        },
		        {
		            "id": 3,
		            "name": "期中考试语文1",
		            "type": 1,
		            "studentId": 1,
		            "score": 87,
		            "pic": "http://www.baidu.com/1.jpg",
		            "createTime": "2017-12-09 01:28:51"
		        }
		    ],
		    "code": 200,
		    "cost": 0.359
		}

图片上传：
	
	新增：request/post: /files
		form-data
		file: xxx.jpg

		response:
		{
		    "cost": 0.182,
		    "code": 200,
		    "url": "https://www.kehue.com/nuo/19db8ea2af2d48dcb0accd4edffddba1.jpeg"
		}





