package com.wangqiang.controller;

import com.wangqiang.pojo.User;
import com.wangqiang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * @version : V1.0
 * @ClassName: LoginController
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/2/25 21:40
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        HttpSession session,
                        Model model){
        User user = userService.selectPasswordByName(username, password);
        if ( user != null){
            //登录成功！
            session.setAttribute("username",user.getUserName());
            //登录成功！防止表单重复提交，我们重定向
            return "redirect:/main.html";
        }else {
            //登录失败！存放错误信息
            model.addAttribute("msg","用户名或密码错误");
            return "index";
        }
    }


    @GetMapping("/user/loginOut")
    public String loginOut(HttpSession session){
        session.invalidate();
        return "redirect:/index.html";
    }
}
