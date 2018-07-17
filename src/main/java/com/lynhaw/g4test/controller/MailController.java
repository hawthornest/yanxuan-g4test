package com.lynhaw.g4test.controller;

import com.lynhaw.g4test.getproperties.GetProperties;
import com.lynhaw.g4test.service.SendEmail;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yyhu3
 * @Date 2018-05-08 20:31
 */

@RestController
public class MailController {

    @Autowired
    SendEmail sendEmailTest;

    @Autowired
    GetProperties getProperties;

    @ApiOperation(value="发送邮件", notes="将测试报告发送邮件给收件人")
    @GetMapping("/sendemail")
    public String sendEmail(String addressees,String serverName)
    {
        sendEmailTest.sendMail(addressees,serverName);
        return getProperties.emailSender;
    }
}
