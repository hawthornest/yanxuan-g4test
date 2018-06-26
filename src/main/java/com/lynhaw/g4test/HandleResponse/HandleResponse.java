package com.lynhaw.g4test.HandleResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;


/**
 * @Author yyhu3
 * @Date 2018-05-03 15:21
 */

public class HandleResponse {
    private String connent = "<div style='width: 1170px;margin-left: 20%'> \n" +
            "<h1>接口测试的结果</h1>";
    Logger logger = Logger.getLogger(HandleResponse.class);
    private int passCount = 0;
    private int failCount = 0;
    private String header = "<p> </p> \n" +
            "        <table border='2'cellspacing='1' cellpadding='1' width='1500'align=\"center\" > \n" +
            "        <tr > \n" +
            "            <td ><strong>测试集ID </strong></td> \n" +
            "            <td><strong>测试集名字</strong></td> \n" +
            "            <td><strong>测试类型</strong></td> \n" +
            "            <td><strong>测试用例id</strong></td> \n" +
            "            <td><strong>测试用例名</strong></td> \n" +
            "            <td><strong>创建时间</strong></td> \n" +
            "            <td><strong>执行结果</strong></td> \n" +
            "        </tr>";

    public String ReportFileHeader(String titles)
    {
        String title = String.format("<!DOCTYPE html> \n" +
                "    <html lang=\"en\"> \n" +
                "    <head> \n" +
                "        <meta charset=\"UTF-8\"> \n" +
                "        <title>%s</title> \n" +
                "        <style type=\"text/css\"> \n" +
                "            td{ width:40px; height:50px;text-align: center;}  p.margin {margin-left:200px;}\n" +
                "        </style> \n" +
                "    </head> \n" +
                "    <body> ",titles);
        return title;
    }

    public String timeStamp2Date(long seconds) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    public String reportSummary(long starttime, long endtime, int passge, int fail)
    {
        String summary = String.format(" <p class=\"margin\"><strong>开始时间:</strong> %s</p> \n" +
                "        <p class=\"margin\"><strong>结束时间:</strong> %s</p> \n" +
                "        <p class=\"margin\"><strong>耗时:</strong> %s ms</p> \n" +
                "        <p class=\"margin\"><strong>结果:</strong> \n" +
                "            <span >Pass: <strong >%s</strong> \n" +
                "            Fail: <strong >%s</strong> \n" +
                "                    </span></p>                   \n" +
                "                <p class=\"margin\" ><strong>测试详情如下：</strong></p>  </div> ",timeStamp2Date(starttime),timeStamp2Date(endtime),(endtime-starttime),passge,fail);
        return summary;
    }

    public String reportResultHandle(String result)
    {
        String htlResult = "<td bgcolor=\"#8b0000\">error</td>";
        if (result.equals("pass"))
        {
            htlResult = "<td bgcolor=\"green\">pass</td>";
        }
        else if (result.equals("fail"))
        {
            htlResult = "<td bgcolor=\"fail\">fail</td>";
        }
        return htlResult;
    }

    public String reportDetails(int suiteId, String suiteName, String testType, int testCaseId, String testName, String createTime, String testResult)
    {
        String testDetail = String.format("<tr> \n" +
                "            <td>%s</td> \n" +
                "            <td>%s</td> \n" +
                "            <td>%s</td> \n" +
                "            <td>%s</td> \n" +
                "            <td>%s</td> \n" +
                "            <td>%s</td> \n" +
                "            %s" +
                "        </tr>",suiteId,suiteName,testType,testCaseId,testName,createTime,reportResultHandle(testResult));
        return testDetail;
    }


    public PrintWriter cyclicStat(int suiteId, String suiteName, String testType, JSONArray testinfos, PrintWriter responseResultPrintWriter, String checkResult)
    {
        for(int sceneInfosLength = 0;sceneInfosLength < testinfos.size();sceneInfosLength++)
        {
            if (testinfos.getJSONObject(sceneInfosLength).getInteger("check")==1)
            {
                checkResult = "pass";
                this.passCount = this.passCount + 1;
            }
            else
            {
                checkResult = "fail";
                this.failCount = this.failCount + 1;
            }
            logger.info("checkResult:"+checkResult);
            responseResultPrintWriter.println(reportDetails(suiteId,suiteName,testType,testinfos.getJSONObject(sceneInfosLength).getInteger("id"),testinfos.getJSONObject(sceneInfosLength).getString("name"),timeStamp2Date(testinfos.getJSONObject(sceneInfosLength).getLong("createTime")),checkResult));
        }
        return responseResultPrintWriter;
    }

    public void encapsuReportDetail(String obtainedResults)
    {

        logger.info("返回包:"+obtainedResults);
        File responseResultFile=new File("responseResult.html");
        try {
            String checkResult = "fail";
            FileWriter responseResultFileWriter =  new FileWriter(responseResultFile, false);
            PrintWriter responseResultPrintWriter = new PrintWriter(responseResultFileWriter);
            responseResultPrintWriter.println(ReportFileHeader("接口测试结果"));

            JSONObject obtainedResultsObj=JSON.parseObject(obtainedResults);
            if (obtainedResultsObj.getInteger("code")==200)
            {
                responseResultPrintWriter.println(header);
                int suiteId = obtainedResultsObj.getJSONObject("data").getInteger("id");
                logger.info("suiteId:"+suiteId);
                String suiteName = obtainedResultsObj.getJSONObject("data").getString("suiteName");
                logger.info("suiteName:"+suiteName);
                JSONArray sceneInfos = obtainedResultsObj.getJSONObject("data").getJSONArray("sceneInfo");
                JSONArray apiCaseInfos = obtainedResultsObj.getJSONObject("data").getJSONArray("apiCaseInfo");
                logger.info("sceneInfos:"+sceneInfos.toJSONString());
                logger.info("apiCaseInfos:"+apiCaseInfos.toJSONString());
                responseResultPrintWriter = cyclicStat(suiteId,suiteName,"场景测试集",sceneInfos,responseResultPrintWriter,checkResult);
                responseResultPrintWriter = cyclicStat(suiteId,suiteName,"接口测试集",apiCaseInfos,responseResultPrintWriter,checkResult);
                long startTime = obtainedResultsObj.getJSONObject("data").getLong("startDate");
                long endTime = obtainedResultsObj.getJSONObject("data").getLong("endDate");
                responseResultPrintWriter.println(reportSummary(startTime,endTime,this.passCount,this.failCount));
            }
            else
            {
                responseResultPrintWriter.println(reportSummary(0,0,this.passCount,this.failCount));
                String errorMsg = String.format(" <p class=\"margin\"><strong>执行错误原因:</strong> %s</p> \n</div> ",obtainedResultsObj.getString("msg"));
                responseResultPrintWriter.println(errorMsg);
            }
            responseResultPrintWriter.flush();
            responseResultFileWriter.flush();
            responseResultPrintWriter.close();
            responseResultFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        HandleResponse handleResponse = new HandleResponse();
//        handleResponse.encapsuReportDetail("{\"code\":200,\"msg\":\"OK\",\"data\":{\"id\":704,\"suiteName\":\"tob选仓临时测试集\",\"sceneInfo\":[{\"id\":2557,\"name\":\"tob订单调度成功\",\"createUser\":\"hzhuyuanyuan@corp.netease.com\",\"createTime\":1524809285000,\"subProjectId\":2196,\"check\":1},{\"id\":2541,\"name\":\"toB选仓\",\"createUser\":\"hzzhengyunpeng@corp.netease.com\",\"createTime\":1524731475000,\"subProjectId\":2196,\"check\":0}],\"apiCaseInfo\":[{\"id\":41641,\"name\":\"tob选仓失败\",\"createUser\":\"hzhuyuanyuan@corp.netease.com\",\"createTime\":1524722301000,\"subProjectId\":2196,\"url\":\"/smart-ipc/api/v1/omsapi/mps/receiveMsg\",\"check\":0}],\"cid\":\"1i1284\",\"executorName\":\"smartipc测试环境\",\"startDate\":1524906981999,\"endDate\":1524906987474,\"userName\":\"hzhuyuanyuan@corp.netease.com\",\"taskId\":\"d147d0eb-4ac4-11e8-a4d4-6d0a0c2e1ab9\",\"elapse\":5475}}");
        handleResponse.encapsuReportDetail("{\"code\":200,\"msg\":\"OK\",\"data\":{\"id\":704,\"suiteName\":\"tob选仓临时测试集\",\"sceneInfo\":[],\"apiCaseInfo\":[{\"id\":41641,\"name\":\"tob选仓失败\",\"createUser\":\"hzhuyuanyuan@corp.netease.com\",\"createTime\":1524722301000,\"subProjectId\":2196,\"url\":\"/smart-ipc/api/v1/omsapi/mps/receiveMsg\",\"check\":1}],\"cid\":\"1i1284\",\"executorName\":\"smartipc测试环境\",\"startDate\":1524906981999,\"endDate\":1524906987474,\"userName\":\"hzhuyuanyuan@corp.netease.com\",\"taskId\":\"d147d0eb-4ac4-11e8-a4d4-6d0a0c2e1ab9\",\"elapse\":5475}}");
    }
}
