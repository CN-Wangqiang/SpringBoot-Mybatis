package com.wangqiang.pojo;

import lombok.Data;

import java.sql.Date;


/**
 * @version : V1.0
 * @ClassName: Employee
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/2/25 19:31
 */
@Data
public class Employee {
    private int id;
    private String employeeName;
    private String email;
    private int gender;
    private int departmentId;
    private Date date;
}
