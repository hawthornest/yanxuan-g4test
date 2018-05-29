package com.lynhaw.g4test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;

/**
 * @Author yyhu3
 * @Date 2018-05-29 20:24
 */

@RestController
public class HelloWrold {
    @GetMapping("/helloworld")
    public String helloWrold(String input)
    {
        Logger logger = Logger.getLogger(HelloWrold.class);
        logger.info("输入与输出为:"+input);
        return input;
    }
}
