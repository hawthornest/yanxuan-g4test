package com.lynhaw.g4test.controller;

import com.lynhaw.g4test.service.LogIn;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yyhu3
 * @Date 2018-05-08 19:48
 */


@RestController
public class GetCookie {
    @Autowired
    LogIn logIn;
    @ApiOperation(value="获取登录cookie", notes="获取corp邮箱登录的有效cookie")
    @GetMapping("/yanxuan/login")
    public String openidLogin(String userName, String passWord)
    {
//        LogIn logIn = new LogIn();
        String logCookie = logIn.logInRequset(userName,passWord);
        return logCookie;
    }

}
