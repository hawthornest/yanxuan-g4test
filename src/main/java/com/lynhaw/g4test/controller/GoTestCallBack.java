package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSON;
import com.lynhaw.g4test.HandleResponse.HandleResponse;
import com.lynhaw.g4test.getproperties.GetProperties;
import com.lynhaw.g4test.mybatis.SqlInfoService.SysInfoServiceImpl;
import com.lynhaw.g4test.request.HttpsRquest;
import com.lynhaw.g4test.response.CallBackResponse;
import com.lynhaw.g4test.service.SendEmail;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author yyhu3
 * @Date 2018-05-03 21:12
 */
@RestController
public class GoTestCallBack {
    @Autowired
    SendEmail sendEmailTest;
    @Autowired
    GetProperties getProperties;
    @Autowired
    SysInfoServiceImpl sysInfoServiceImpl;

    Logger logger = Logger.getLogger(GoTestCallBack.class);
    @ApiOperation(value="提供回调", notes="提供给gotest使用的回调接口")
    @RequestMapping(value="/yanxuan/callback",method= RequestMethod.GET)
    public String callback(@RequestHeader(value="taskId",defaultValue = "") String taskId)
    {
        MDC.put("traceId", UUID.randomUUID().toString());
        CallBackResponse callBackResponse = new CallBackResponse();
        if (taskId==null || taskId.equals(""))
        {
            callBackResponse.setCode(400);
            callBackResponse.setMsg("未获取到有效的taskid");
        }
        else{
            logger.info("获取到的taskid为:"+taskId);
            HttpsRquest httpsRquest = new HttpsRquest();
            String responseResult = httpsRquest.httpsCallBackPost(taskId);
            HandleResponse handleResponse = new HandleResponse();
            callBackResponse.setCode(200);
            callBackResponse.setMsg("success");
            String addressees = sysInfoServiceImpl.getInfobytaskId(taskId).getAddressees();
            String sysBranch = sysInfoServiceImpl.getInfobytaskId(taskId).getSysBranch();
            String serverName = sysInfoServiceImpl.getInfobytaskId(taskId).getServerName();
            logger.info("获取到的收件人信息为:"+addressees);
            logger.info("获取到的服务名称为:"+serverName);
            logger.info("获取到的服务分支为:"+sysBranch);
            handleResponse.encapsuReportDetail(sysBranch,responseResult);
            sendEmailTest.sendMail(addressees,serverName);
        }
//        callBackResponse.setEmailSender(getProperties.emailSender);
        logger.info("返回包给gotest的返回包为:"+JSON.toJSONString(callBackResponse));
        MDC.remove("traceId");
        return JSON.toJSONString(callBackResponse);
    }
}
