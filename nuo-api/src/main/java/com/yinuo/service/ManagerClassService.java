package com.yinuo.service;

import com.yinuo.bean.*;
import com.yinuo.exception.InvalidArgumentException;
import com.yinuo.mapper.ManagerClassMapper;
import com.yinuo.mapper.ManagerMapper;
import com.yinuo.util.CommonUtil;
import com.yinuo.util.DateTool;
import com.yinuo.util.MergerUtil;
import com.yinuo.view.ManagerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional(rollbackFor = Exception.class)
@Service
public class ManagerClassService {
	
	@Autowired
	private ManagerClassMapper managerClassMapper;

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private ClassesService classesService;
	
	public long insert(User loginUser, ManagerClass managerClass) {
		CommonUtil.setDefaultValue(managerClass);

		managerClass.setCreateTime(DateTool.standardSdf.format(new Date()));
		managerClassMapper.insert(managerClass);
		
		return managerClass.getId();
	}
	
	public ManagerClass selectOne(long id) {
		return managerClassMapper.selectOne(id);
	}
	
	public void delete (User loginUser, long id) {
		managerClassMapper.delete(id);
	}
	
	public void update(User loginUser, ManagerClass managerClass) {
		ManagerClass src = selectOne(managerClass.getId());
		
		managerClass = (ManagerClass) MergerUtil.merger(src, managerClass);
		managerClassMapper.update(managerClass);
	}

	public List<ManagerClass> selectListByIds(List<Long> ids) {
		return managerClassMapper.selectListByIds(ids);
	}

	public List<ManagerClass> selectByRole(int role, long schoolId, long classId, int page, int pageSize) {
		return managerClassMapper.selectByRole(role, schoolId, classId, (page-1)*pageSize, pageSize);
	}

	public int countByRole(int role, long schoolId, long classId) {
		return managerClassMapper.countByRole(role, schoolId, classId);
	}

	public List<ManagerClass> selectByManagerId(long managerId) {
		return managerClassMapper.selectByManagerId(managerId);
	}

	public List<ManagerView> getClassManagers(User loginUser, int role) {
		List<ManagerView> views = new ArrayList<ManagerView>();
		List<ManagerClass> managerClasses = selectByManagerId(loginUser.getManager().getId());
		if(managerClasses == null || managerClasses.size() <= 0) {
			throw new InvalidArgumentException("无管理权限");
		}
		List<Long> classIds = new ArrayList<Long>();
		List<Long> schoolIds = new ArrayList<Long>();
		for(ManagerClass managerClass: managerClasses) {
			if(managerClass.getRole() == Constant.Role.School) {
				schoolIds.add(managerClass.getSchoolId());
			}
			if(managerClass.getRole() == Constant.Role.Teacher) {
				classIds.add(managerClass.getClassId());
			}
		}

		if(role == Constant.Role.School) {
			if(schoolIds == null || schoolIds.size() <= 0) {
				throw new InvalidArgumentException("无学校管理权限");
			}
			List<School> schools = schoolService.selectListByIds(schoolIds);
			for(School school: schools) {
				views.add(new ManagerView(school, null));
			}
		}else if(role == Constant.Role.Teacher) {
			if((schoolIds == null || schoolIds.size() <= 0) && (classIds == null || classIds.size()<=0)) {
				throw new InvalidArgumentException("无班级管理权限");
			}
			List<Classes> allClass = new ArrayList<Classes>();
			List<Long> allClassIds = new ArrayList<Long>();
			//补充班级列表
			if(schoolIds != null && schoolIds.size() > 0) {
				for(Long schoolId: schoolIds) {
					List<Classes> classes = classesService.selectBySchoolId(schoolId, 0, 1, Integer.MAX_VALUE);
					if(classes != null && classes.size()>0) {
						for(Classes temp: classes) {
							allClass.add(temp);
							allClassIds.add(temp.getId());
						}
					}
				}
			}
			List<Long> appendClassIds = new ArrayList<Long>();
			if(classIds != null && classIds.size()> 0) {
				for(Long classId: classIds) {
					if(!allClassIds.contains(classId)) {
						appendClassIds.add(classId);
						allClassIds.add(classId);
					}
				}
				if(appendClassIds.size()>0) {
					List<Classes> appends = classesService.selectListByIds(appendClassIds);
					allClass.addAll(appends);
				}
			}

			Map<Long, List<Classes>> classMap = convert2Map(allClass);
			List<Long> needSchooldIds = new ArrayList<Long>();
			for(Long schoolId: classMap.keySet()) {
				needSchooldIds.add(schoolId);
			}
			List<School> schools = schoolService.selectListByIds(needSchooldIds);
			for(School school: schools) {
				views.add(new ManagerView(school, classMap.get(school.getId())));
			}
		}

		return views;
	}

	public Map<Long, List<Classes>> convert2Map(List<Classes> classes) {
		Map<Long, List<Classes>> map = new HashMap<Long, List<Classes>>();
		if(classes != null && classes.size() > 0) {
			for(Classes temp: classes) {
				if(map.get(temp.getSchoolId()) == null) {
					map.put(temp.getSchoolId(), new ArrayList<Classes>());
				}
				map.get(temp.getSchoolId()).add(temp);
			}
		}
		return map;
	}
}
