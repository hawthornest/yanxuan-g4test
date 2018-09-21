package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.service.PublicMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * @Author yyhu3
 * @Date 2018-09-20 10:16
 */

@RestController
public class NoCoverInterface {
    PublicMethod publicMethod;
    Logger logger = Logger.getLogger(String.valueOf(NoCoverInterface.class));
    @GetMapping("/noCoverInterface")
    public String noCoverInterface(String requestUrl,String testId)
    {
        PublicMethod publicMethod = new PublicMethod();
        logger.info(String.format("输入的请求参数为:requestUrl=%s&testId=%s",requestUrl,testId));
        int code = publicMethod.ReportNoCoverHeader(requestUrl,testId);
        JSONObject noCoverInterJson = new JSONObject();
        noCoverInterJson.put("code",code);
        logger.info(String.format("返回包为:%s",noCoverInterJson.toJSONString()));
        return noCoverInterJson.toJSONString();
    }
}
