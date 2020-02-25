package com.wangqiang.service;

import com.wangqiang.mapper.DepartmentMapper;
import com.wangqiang.pojo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version : V1.0
 * @ClassName: DepartmentServiceImpl
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/2/25 21:37
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> selectAllDepartment() {
        return departmentMapper.selectAllDepartment();
    }

}
