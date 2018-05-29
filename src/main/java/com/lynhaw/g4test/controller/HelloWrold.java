package com.lynhaw.g4test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yyhu3
 * @Date 2018-05-29 20:24
 */

@RestController
public class HelloWrold {
    @GetMapping("/helloworld")
    public String helloWrold()
    {
        return "helloworld";
    }
}
