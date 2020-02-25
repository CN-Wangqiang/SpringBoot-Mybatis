package com.wangqiang.mapper;

import com.wangqiang.dto.EmployeeDTO;
import com.wangqiang.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version : V1.0
 * @ClassName: EmployeeMapper
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/2/25 19:39
 */
@Mapper
@Repository
public interface EmployeeMapper {
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
