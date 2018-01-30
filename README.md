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
						/users?nickName=凭栏处、潇潇雨歇

		{
		    "code": 200,
		    "cost": 0.776,
		    "loginUser": {
		        "id": 22,
		        "createTime": "2018-01-09 21:17:16",
		        "updateTime": "2018-01-09 21:17:16",
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

学生信息/增删改需要老师及以上权限

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
						/students?id=1	/按照ID查询

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

学校信息/增删改需要管理员权限
	
	新增：request/post: /schools

		{
			"name": "abc",
			"type": 1
		}

	删除：request/delete: /schools?id=1
	
	修改：request/put: /schools

		{
			"id": 1,
			"name": "abc2",
			"type": 1
		}

	查询：request/get: /schools?id=4
						/schools?page=1&pageSize=10

		{
		    "count": 3,
		    "data": [
		        {
		            "id": 4,
		            "name": "abc2",
		            "type": 1,
		            "managerId": 1,
		            "createTime": "2018-01-13 16:09:39"
		        },
		        {
		            "id": 3,
		            "name": "abc",
		            "type": 1,
		            "managerId": 1,
		            "createTime": "2018-01-13 16:09:16"
		        },
		        {
		            "id": 2,
		            "name": "abc",
		            "type": 1,
		            "managerId": 1,
		            "createTime": "2018-01-13 16:06:57"
		        }
		    ],
		    "code": 200,
		    "cost": 0.307
		}

	查询：request/get: /schools/managers 查询我所拥有的学校管理权限
	需要至少学校管理权限

	{
	    "data": [
	        {
	            "id": 1,
	            "createTime": "2018-01-13 16:06:57",
	            "managerId": 1,
	            "name": "固始县永和小学"
	        },
	        {
	            "id": 3,
	            "createTime": "2018-01-13 16:09:16",
	            "managerId": 1,
	            "name": "abc"
	        }
	    ],
	    "code": 200,
	    "cost": 0.448
	}

班级信息/增删改需要学校管理权限
	
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

	查询：request/get: /classes?id=4
						/classes?schoolId=1&grade=1&page=1&pageSize=10

		{
		    "count": 2,
		    "data": [
		        {
		            "id": 3,
		            "grade": 1,
		            "number": 1,
		            "schoolId": 1,
		            "managerId": 1,
		            "createTime": "2018-01-11 22:49:00"
		        },
		        {
		            "id": 4,
		            "grade": 2,
		            "number": 2,
		            "schoolId": 1,
		            "managerId": 1,
		            "createTime": "2018-01-13 14:56:51"
		        }
		    ],
		    "code": 200,
		    "cost": 1.718
		}

	查询：request/get: /classes/managers 查询我所拥有的班级管理权限
	需要至少班级管理权限

	{
	    "data": [
	        {
	            "classes": [
	                {
	                    "id": 1,
	                    "grade": 1,
	                    "number": 1,
	                    "schoolId": 1,
	                    "managerId": 1,
	                    "createTime": "2018-01-11 22:49:00"
	                }
	            ],
	            "id": 1,
	            "createTime": "2018-01-13 16:06:57",
	            "managerId": 1,
	            "name": "固始县永和小学"
	        },
	        {
	            "classes": [
	                {
	                    "id": 6,
	                    "grade": 1,
	                    "number": 1,
	                    "schoolId": 3,
	                    "managerId": 1,
	                    "createTime": "2018-01-13 14:56:51"
	                },
	                {
	                    "id": 7,
	                    "grade": 1,
	                    "number": 2,
	                    "schoolId": 3,
	                    "managerId": 1,
	                    "createTime": "1970-01-01 08:00:00"
	                }
	            ],
	            "id": 3,
	            "createTime": "2018-01-13 16:09:16",
	            "managerId": 1,
	            "name": "abc"
	        }
	    ],
	    "code": 200,
	    "cost": 0.448
	}

考试/增删改需要学校权限
	
	新增：request/post: /exams
		
		{
			"name": "2018年1月份统考",
			"schoolId": 3,
			"grade": 1,
			"subjects": "1,2,3"
		}

	删除：request/delete: /exams?id=1

	修改：request/put: /exams

		{
			"id": 1,
			"state": 2
		}

	查询：request/get: /exams?schoolId=1&page=1&pageSize=10
						/exams?id=1

		{
		    "count": 1,
		    "data": [
		        {
		            "id": 2,
		            "name": "2018年1月份统考",
					"schoolId": 3,
					"grade": 1,
					"subjects": "1,2,3",
					"state": 1,
		            "managerId": 0,
		            "createTime": "2018-01-13 16:51:07",
		            "fixTime": "2018-01-13 16:51:07",
		            "fixManagerId": 1
		        }
		    ],
		    "code": 200,
		    "cost": 0.288
		}


考试批次/增删改需要老师权限
	
	新增：request/post: /scoreBatchs
		
		{
			"examId": 1,	
			"schoolId": 1,	
			"classId": 1,
			"subject": 1,
			"type":1
		}

	删除：request/delete: /scoreBatchs?id=1

	修改：request/put: /scoreBatchs

		{
			"id": 1,
			"state": 2
		}

	查询：request/get: /scoreBatchs?schoolId=1&type=1&page=1&pageSize=10
					  /scoreBatchs?id=1
					  /scoreBatchs?classId=1&type=1&page=1&pageSize=10
					  /scoreBatchs?examId=1&page=1&pageSize=10

		{
		    "count": 1,
		    "data": [
		        {
		            "id": 2,
		            "examId": 1,	
					"schoolId": 1,	
					"classId": 1,
					"subject": 1
		            "managerId": 0,
		            "createTime": "2018-01-13 16:51:07",
		            "fixTime": "2018-01-13 16:51:07",
		            "fixManagerId": 1,
		            "type": 1
		        }
		    ],
		    "code": 200,
		    "cost": 0.288
		}


分数信息/增删改需要老师权限

	新增：request/post: /scores
		
		{
		    "type":1,
		    "studentId":8,
		    "score":87,
		    "pic":"http://www.baidu.com/1.jpg",
		    "scoreBatchId":2
		}

		type>1考试，2作业

	删除：request/delete: /scores?id=1

	修改：request/put: /scores

		{
		    "id":8,
		    "type":1,
		    "studentId":8,
		    "score":89,
		    "pic":"http://www.baidu.com/1.jpg",
		    "scoreBatchId":2
		}

	查询：request/get: /scores?studentId=1&type=1&page=1&pageSize=10	//type可选
					  /scores?id=1
					  /scores?scoreBatchId=1&type=1&page=1&pageSize=10	//type可选

		{
		    "count": 1,
		    "data": [
		        {
		            "id": 9,
		            "createTime": "2018-01-13 17:18:44",
		            "classId": 3,
		            "studentId": 8,
		            "scoreBatchId": 2,
		            "managerId": 0,
		            "score": 88,
		            "scoreBatch": {
		                "id": 2,
		                "name": "期中考试语文20170812",
		                "schoolId": 1,
		                "classId": 1,
		                "managerId": 0,
		                "createTime": "2018-01-13 16:51:07",
		                "schoolRank":1,
		                "classRank":1
		            },
		            "pic": "http://www.baidu.com/1.jpg",
		            "type": 1
		        }
		    ],
		    "code": 200,
		    "cost": 0.3
		}

统计逻辑：将scoreBatchs接口状态置为2，每隔5分钟自动执行更新学生，班级统计。以及scoreBatchs状态和时间。

班级统计/查询

	查询：request/get: /classStats?schoolId=1&scoreBatchId=3&classId=1&page=1&pageSize=10	//scoreBatchId,classId 可选

		{
		    "count": 1,
		    "data": [
		        {
		            "id": 3,
		            "subject": 1,
		            "scoreBatchId": 3,
		            "schoolId": 1,
		            "classId": 1,
		            "highScore": 90,
		            "averageScore": 90,
		            "medianScore": 90,
		            "createTime": "2018-01-17 22:29:23"
		        }
		    ],
		    "code": 200,
		    "cost": 0.568
		}


学生统计/查询

	查询：request/get: /studentStats?studentId=14&subject=1&page=1&pageSize=10	//subject 可选

		{
		    "count": 1,
		    "data": [
		        {
		            "id": 1,
		            "studentId": 14,
		            "subject": 1,
		            "highScore": 90,
		            "averageScore": 90,
		            "medianScore": 90,
		            "createTime": "2018-01-17 22:29:29"
		        }
		    ],
		    "code": 200,
		    "cost": 0.447
		}

课程信息

	新增：request/post: /courses

		{
            "classId": 1,
            "pic": "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1874612018,2337305222&fm=27&gp=0.jpg"
        }

	删除：request/delete: /courses?id=1
	
	修改：request/put: /courses

		{
            "id": 4,
            "classId": 1,
            "pic": "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1874612018,2337305222&fm=27&gp=0.jpg"
        }

	查询：request/get: /courses

		按照班级时间查询 /courses?classId=1
		按照ID查询 /courses?id=4

		{
		    "count": 1,
		    "data": [
		        {
		            "id": 4,
		            "classId": 1,
		            "pic": "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1874612018,2337305222&fm=27&gp=0.jpg",
		            "managerId": 1,
		            "createTime": "2018-01-17 22:48:53"
		        }
		    ],
		    "code": 200,
		    "cost": 0.509
		}

创建空权限的管理者/增删改需要学校管理权限
	
	新增：request/post: /managers
		
		{
			"name": "张一峰",	
			"userId": 1
		}

	修改：request/post: /managers
		
		{
			"id", 1,
			"name": "张一峰",	
			"userId": 1
		}


权限绑定/增删改需要超级管理员权限
	
	新增：request/post: /managerClass
		
		{
			"managerId": 1,	
			"schoolId": 1,	
			"classId": 1,
			"role": 1
		}

	删除：request/delete: /managerClass?id=1

	修改：request/put: /managerClass

		{
			"id": 1,
			"managerId": 1,	
			"schoolId": 1,	
			"classId": 1,
			"role": 1
		}

	查询：request/get: /managerClass?schoolId=1&classId=1&role=1&page=1&pageSize=10 role必传


		{
		    "count": 1,
		    "data": [
		        {
		            "id": 2,
		            "managerId": 1,	
					"schoolId": 1,	
					"classId": 1,
					"role": 1,
					"manager": {
						"name": "张一峰",	
						"userId": 1
					}
		        }
		    ],
		    "code": 200,
		    "cost": 0.288
		}


权限绑定/增删改需要超级管理员权限
	
	新增：request/post: /managerClass
		
		{
			"managerId": 1,	
			"schoolId": 1,	
			"classId": 1,
			"role": 1
		}

	删除：request/delete: /managerClass?id=1

	修改：request/put: /managerClass

		{
			"id": 1,
			"managerId": 1,	
			"schoolId": 1,	
			"classId": 1,
			"role": 1
		}

	查询：request/get: /managerClass?schoolId=1&page=1&pageSize=10

		{
		    "count": 1,
		    "data": [
		        {
		            "id": 2,
		            "managerId": 1,	
					"schoolId": 1,	
					"classId": 1,
					"role": 1
		        }
		    ],
		    "code": 200,
		    "cost": 0.288
		}

图片上传：
	
	新增：request/post: /files
		form-data
		file: xxx.jpg

		response:
		{
		    "cost": 0.182,
		    "code": 200,
		    "url": "https://www.kehue.com/19db8ea2af2d48dcb0accd4edffddba1.jpeg"
		}






