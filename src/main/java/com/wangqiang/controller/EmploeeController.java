package com.wangqiang.controller;

import com.wangqiang.dto.EmployeeDTO;
import com.wangqiang.pojo.Department;
import com.wangqiang.pojo.Employee;
import com.wangqiang.service.DepartmentService;
import com.wangqiang.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;

/**
 * @version : V1.0
 * @ClassName: EmploeeController
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/2/25 21:48
 */
@Controller
public class EmploeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    //查询所有员工，返回列表页面
    @GetMapping("/emp")
    public String list(Model model){
        Collection<EmployeeDTO> employees = employeeService.selectAllEmployeeDTO();
//        将结果放在请求中
        model.addAttribute("emps",employees);
        return "emp/list.html";
    }

    //to员工添加页面
    @GetMapping("/add")
    public String toAdd(Model model){
        //查出所有的部门，提供选择
        Collection<Department> departments = departmentService.selectAllDepartment();
        model.addAttribute("departments",departments);
        return "emp/add.html";
    }

    //员工添加功能，使用post接收
    @PostMapping("/add")
    public String add(Employee employee){
        //保存员工信息
        employeeService.addEmployee(employee);
        //回到员工列表页面，可以使用redirect或者forward
        return "redirect:/emp";
    }

    //to员工修改页面
    @GetMapping("/emp/{id}")
    public String toUpdateEmp(@PathVariable("id") Integer id, Model model){
        //根据id查出来员工
        Employee employee = employeeService.selectEmployeeById(id);
        //将员工信息返回页面
        model.addAttribute("emp",employee);
        //查出所有的部门，提供修改选择
        Collection<Department> departments = departmentService.selectAllDepartment();
        model.addAttribute("departments",departments);

        return "emp/update.html";
    }

    @PostMapping("/updateEmp")
    public String updateEmp(Employee employee){
        employeeService.updateEmployee(employee);
        //回到员工列表页面
        return "redirect:/emp";
    }

    @GetMapping("/delEmp/{id}")
    public String deleteEmp(@PathVariable("id")Integer id){
        //根据id删除员工
        employeeService.deleteEmployee(id);
        return "redirect:/emp";
    }


}
