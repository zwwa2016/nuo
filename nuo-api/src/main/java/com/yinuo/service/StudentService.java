package com.yinuo.service;

import com.yinuo.bean.*;
import com.yinuo.bean.Constant.UserStudentRelationship;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.mapper.StudentMapper;
import com.yinuo.util.CommonUtil;
import com.yinuo.util.DateTool;
import com.yinuo.util.MergerUtil;
import com.yinuo.view.StudentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional(rollbackFor = Exception.class)
@Service
public class StudentService {
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private UserStudentService userStudentService;

	@Autowired
	private ClassesService classesService;

	@Autowired
	private ScoreService scoreService;
	
	public long insert(User loginUser, Student student) {
		loginUser.checkLevel(Constant.Role.Teacher, student.getSchoolId(), student.getClassId());

		CommonUtil.setDefaultValue(student);
		
		student.setCreateTime(DateTool.standardSdf.format(new Date()));
		student.setManagerId(loginUser.getManager().getId());
		studentMapper.insert(student);

		return student.getId();
	}
	
	public Student selectOne(long id) {
		return studentMapper.selectOne(id);
	}
	
	public void delete (User loginUser, long id) {
		Student student = studentMapper.selectOne(id);
		loginUser.checkLevel(Constant.Role.Teacher, student.getSchoolId(), student.getClassId());

		userStudentService.deleteByStudentId(id);
		scoreService.deleteByStudentId(id);

		studentMapper.delete(id);
	}
	
	//只能自己修改自己的数据场景
	public void update(User loginUser, Student student) {
		Student src = selectOne(student.getId());
		loginUser.checkLevel(Constant.Role.Teacher, src.getSchoolId(), src.getClassId());
		
		student = (Student) MergerUtil.merger(src, student);
		studentMapper.update(student);
	}

	public List<Student> selectListByIds(List<Long> ids) {
		return studentMapper.selectListByIds(ids);
	}


	public List<Student> selectByName(String name) {
		return studentMapper.selectByName(name);
	}

	public int countByName(String name) {
		return studentMapper.countByName(name);
	}

	public List<Student> selectByClassid(User loginUser, long classId, int page, int pageSize) {
		//需要校验权限
		Classes classes = classesService.selectOne(classId);
		if(classes == null) {
			throw new InvalidArgumentException("找不到的班级ID");
		}
		loginUser.checkLevel(Constant.Role.Teacher, classes.getSchoolId(), classId);

		return studentMapper.selectByClassid(classId, pageSize, (page-1)*pageSize);
	}

	public int countByClassid(long classId) {
		return studentMapper.countByClassid(classId);
	}

	public List<StudentView> convert2View(List<Student> students, Map<Long, UserStudent> ussMap) {
		if(ussMap == null) {
			ussMap = new HashMap<Long, UserStudent>();
		}
		List<StudentView> view = new ArrayList<StudentView>();
		if(students != null && !students.isEmpty()) {
			for(Student student: students) {
				view.add(new StudentView(student, ussMap.get(student.getId())));
			}
		}
		return view;
	}
}
