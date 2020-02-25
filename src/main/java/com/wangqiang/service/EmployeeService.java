package com.wangqiang.service;

import com.wangqiang.dto.EmployeeDTO;
import com.wangqiang.pojo.Employee;

import java.util.List;

/**
 * @version : V1.0
 * @ClassName: EmployeeService
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/2/25 20:13
 */
public interface EmployeeService {
    //查询全部员工信息
    List<EmployeeDTO> selectAllEmployeeDTO();
    //根据id查询员工信息
    Employee selectEmployeeById(int id);
    //添加一个员工信息
    int addEmployee(Employee employee);
    //修改一个员工信息
    int updateEmployee(Employee employee);
    //根据id删除员工信息
    int deleteEmployee(int id);
}
