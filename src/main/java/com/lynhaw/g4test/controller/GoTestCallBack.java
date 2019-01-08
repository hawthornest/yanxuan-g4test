package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

import java.text.SimpleDateFormat;
import java.util.Date;
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
            int[ ]  operatResult=new int[2];
            logger.info("获取到的taskid为:"+taskId);
            int callback = sysInfoServiceImpl.getInfobytaskId(taskId).getCallback();
            HttpsRquest httpsRquest = new HttpsRquest();
            if (callback==1)
            {
                String failRetry = httpsRquest.httpsTaskRerun(taskId);
                callBackResponse.setCode(200);
                callBackResponse.setMsg("success");
                logger.info("执行失败用例重试结果为:"+failRetry);
                if (failRetry.contains("9502"))
                {
                    logger.info("不需要重试不会主动触发回调逻辑:"+sysInfoServiceImpl.updateCallBackStatusByTaskId(2,taskId));
                    callback = sysInfoServiceImpl.getInfobytaskId(taskId).getCallback();
                }
                //TODO
            }
            if(callback!=1)
            {
                String responseResult = httpsRquest.httpsCallBackPost(taskId);
                HandleResponse handleResponse = new HandleResponse();
                callBackResponse.setCode(200);
                callBackResponse.setMsg("success");
                String addressees = sysInfoServiceImpl.getInfobytaskId(taskId).getAddressees();
                String sysBranch = sysInfoServiceImpl.getInfobytaskId(taskId).getSysBranch();
                String serverName = sysInfoServiceImpl.getInfobytaskId(taskId).getServerName();
                int updateTimeResult = sysInfoServiceImpl.updateCallBackTimeByTaskId(taskId);
                logger.info("更新操作时间结果为:"+updateTimeResult);
                int isNeed = sysInfoServiceImpl.getInfobytaskId(taskId).getIsNeed();
                String domainName = sysInfoServiceImpl.getInfobytaskId(taskId).getDomainName();
                String commitInfo = sysInfoServiceImpl.getInfobytaskId(taskId).getCommitInfo().replaceAll(">","&gt;").replaceAll("<","&lt;");
                logger.info("获取到的代码提交信息为:"+commitInfo);
                String speAuthor;
                if (commitInfo.equals("commitInfo"))
                {
                    commitInfo = "未成功获取到提交记录,请查看部署脚本,是否正确添加git log";
                    speAuthor = "未成功获取到";
                }
                else
                {
                    logger.info("首次分割[0]:"+commitInfo.split("Author: ")[1].split("Date")[0]);
                    logger.info("首次分割[1]:"+commitInfo.split("Author: ")[2].split("Date")[0]);
                    speAuthor = commitInfo.split("Author: ")[1].split("Date")[0] + "&" + commitInfo.split("Author: ")[2].split("Date")[0].replaceAll("&gt;",">").replaceAll("&lt;","<");
                }
                logger.info("获取到的收件人信息为:"+addressees);
                logger.info("获取到的服务名称为:"+serverName);
                logger.info("获取到的服务分支为:"+sysBranch);
                logger.info("获取到的服务是否需要检验接口覆盖度为:"+isNeed);
                logger.info("获取到的域名为:"+domainName);
                logger.info("此次编译关注开发为:"+speAuthor);
                operatResult = handleResponse.encapsuReportDetail(sysBranch,responseResult,isNeed,domainName,commitInfo);
                sendEmailTest.sendMail(addressees,serverName);
                //发送泡泡报警
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                UUID uuid = UUID.randomUUID();
                JSONObject testReportResult = new JSONObject();
                testReportResult.put("testSysBranch",sysBranch);
                testReportResult.put("testServerName",serverName);
                testReportResult.put("passCount",operatResult[0]);
                testReportResult.put("failCount",operatResult[1]);
                testReportResult.put("speAuthor",speAuthor);
                logger.info(String.format(("time=%s,module=g4test,topic=g4TestReportDone, id=%s, message=%s"),df.format(new Date()).toString(),uuid.toString(),testReportResult.toJSONString()));
            }

        }
//        callBackResponse.setEmailSender(getProperties.emailSender);
        logger.info("更新数据库回调状态结果为:"+sysInfoServiceImpl.updateCallBackStatusByTaskId(2,taskId));
        logger.info("返回包给gotest的返回包为:"+JSON.toJSONString(callBackResponse));
        MDC.remove("traceId");
        return JSON.toJSONString(callBackResponse);
    }
}
