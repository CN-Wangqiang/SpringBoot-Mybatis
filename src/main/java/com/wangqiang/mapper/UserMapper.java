package com.wangqiang.mapper;

import com.wangqiang.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @version : V1.0
 * @ClassName: UserMapper
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/2/25 19:58
 */
@Mapper
@Repository
public interface UserMapper {
    User selectPasswordByName(@Param("userName") String userName,@Param("password") String password);
}
