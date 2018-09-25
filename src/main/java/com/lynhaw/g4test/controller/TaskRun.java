package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.enumeration.CommonEnum;
import com.lynhaw.g4test.mybatis.SqlInfoService.SysInfoServiceImpl;
import com.lynhaw.g4test.request.HttpsRquest;
import com.lynhaw.g4test.response.CallBackResponse;
import com.lynhaw.g4test.service.PublicMethod;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author yyhu3
 * @Date 2018-05-05 15:38
 */

@RestController
public class TaskRun {
    @Autowired
    private SysInfoServiceImpl sysInfoServiceImpl;
    Logger logger = Logger.getLogger(TaskRun.class);
    @ApiOperation(value="执行测试用例", notes="根据测试集id执行用例")
//    @ApiImplicitParam(name = "testId", value = "测试集id", required = true, dataType = "String")
    @GetMapping("/yanxuan/taskrun")
    public String executeTtestCase(String testId, String environmentId, String callBackUrl,int sysId,String sysBranch,String iSNeed,String swaggInfo)
    {
        MDC.put("traceId", UUID.randomUUID().toString());
        CallBackResponse implementRespons = new CallBackResponse();
        HttpsRquest httpsRquest = new HttpsRquest();
        CommonEnum commonEnum = CommonEnum.valueOf(iSNeed.toUpperCase());
        int isNeed = 0;
        switch (commonEnum)
        {
            case NONEED:
                break;
            case NEED:
                isNeed = 1;
                PublicMethod publicMethod = new PublicMethod();
                logger.info(String.format("输入的请求参数为:requestUrl=%s&testId=%s",swaggInfo,testId));
                int code = publicMethod.ReportNoCoverHeader(swaggInfo,testId);
                JSONObject noCoverInterJson = new JSONObject();
                noCoverInterJson.put("code",code);
                logger.info(String.format("返回包为:%s",noCoverInterJson.toJSONString()));
        }
        String runResponse = httpsRquest.httpsTaskRunPost(testId,environmentId,callBackUrl);
        JSONObject runResponseJson = JSON.parseObject(runResponse);
        logger.info("执行测试用例返回包为:"+JSON.toJSONString(runResponse));
        int responseData = runResponseJson.getInteger("code");
        String errorMsg = runResponseJson.getString("msg");
        String taskId = runResponseJson.getJSONArray("data").getJSONObject(0).getString("taskId");
        logger.info("执行用例获取的taskid为:"+taskId);
        logger.info("是否需要获取覆盖度:"+isNeed);
        int updateResult = sysInfoServiceImpl.updateInfo(taskId,sysBranch,isNeed,sysId);
        logger.info("当前执行的测试分支为:"+sysBranch);
        logger.info("更新数据库结果为:"+updateResult);
        implementRespons.setCode(responseData);
        implementRespons.setMsg(errorMsg);
        implementRespons.setTaskId(taskId);
        MDC.remove("traceId");
        return JSON.toJSONString(implementRespons);
    }
}
