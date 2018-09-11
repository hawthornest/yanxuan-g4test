package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.mybatis.beans.ServerBeans;
import com.lynhaw.g4test.service.PublicMethod;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @Author yyhu3
 * @Date 2018-08-09 11:02
 */
@RestController
public class ToolController {
    @Autowired
    private PublicMethod publicMethod;
    Logger logger = Logger.getLogger(ToolController.class);
    @GetMapping("/yanxuan/getrandom")
    public String getInfolimit(){
        MDC.put("traceId", UUID.randomUUID().toString());
        long currentTime = System.currentTimeMillis();
        String randomString = publicMethod.getRandomString(15);
        JSONObject jsonSysInfo = new JSONObject();
        jsonSysInfo.put("code",200);
        jsonSysInfo.put("randomData",currentTime+randomString);
        logger.info("获取到的随机数为:"+ currentTime+randomString);
        MDC.remove("traceId");
        return jsonSysInfo.toJSONString();
    }
}
