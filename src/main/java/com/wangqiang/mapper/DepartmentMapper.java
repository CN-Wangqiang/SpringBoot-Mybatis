package com.wangqiang.mapper;

import com.wangqiang.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version : V1.0
 * @ClassName: Department
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/2/25 19:59
 */
@Mapper
@Repository
public interface DepartmentMapper {
    List<Department> selectAllDepartment();
}
