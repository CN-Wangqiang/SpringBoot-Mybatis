package com.wangqiang.service;

import com.wangqiang.mapper.UserMapper;
import com.wangqiang.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version : V1.0
 * @ClassName: UserServiceImpl
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/2/25 21:32
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectPasswordByName(String userName,String password) {
        return userMapper.selectPasswordByName(userName,password);
    }
}
