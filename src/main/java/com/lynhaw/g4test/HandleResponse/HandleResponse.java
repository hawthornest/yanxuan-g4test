package com.lynhaw.g4test.HandleResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.*;
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
                "        </style> \n" +"<script type=\"text/javascript\" src=\"http://www.sz886.com/js/jquery-1.9.1.min.js\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "$(document).ready(function(e) { \n" +
                "    $(\".btn\").click(function(){\n" +
                "         if(!$(\".div\").is(\":visible\")){\n" +
                "          $(\".div\").show(); \n" +
                "         }else{ \n" +
                "          $(\".div\").hide(); \n" +
                "        }\n" +
                "    });\n" +
                "       \n" +
                "});\n" +
                "</script>"+
                "    </head> \n" +
                "    <body> ",titles);
        return title;
    }

    public String timeStamp2Date(long seconds) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    public String reportSummary(long starttime, long endtime, int passge, int fail,String sysBranch,int isNeed,String domainName,String commitInfo)
    {
        String coverResult = "";
        String summary = "";
        String speAuthor = "";
        if (commitInfo.equals("commitInfo"))
        {
            commitInfo = "未成功获取到提交记录,请查看部署脚本,是否正确添加git log";
            speAuthor = "未成功获取到";
        }
        else
        {
            speAuthor = commitInfo.split("Author: ")[1].split("Date")[0] + " & " + commitInfo.split("Author: ")[2].split("Date")[0];
             }
        summary += String.format(" <p class=\"margin\"><strong>分支上最近两条代码提交信息为:</strong> %s</p> \n" +
                        " <p class=\"margin\"><strong><font color=\"#FF0000\">需要特殊关注这封邮件的开发: %s</font></strong></p> \n" +
                " <p class=\"margin\"><strong>测试分支为:</strong> %s</p> \n" +
                " <p class=\"margin\"><strong>开始时间:</strong> %s</p> \n" +
                "        <p class=\"margin\"><strong>结束时间:</strong> %s</p> \n" +
                "        <p class=\"margin\"><strong>耗时:</strong> %s ms</p> \n" +
                "        <p class=\"margin\"><strong>结果:</strong> \n" +
                "            <span >Pass: <strong >%s</strong> \n" +
                "            Fail: <strong >%s</strong> \n" +
                "                    </span></p>                   \n",commitInfo,speAuthor,sysBranch,timeStamp2Date(starttime),timeStamp2Date(endtime),(endtime-starttime),passge,fail) ;
        if (isNeed==2||isNeed==3)
        {
            for (int retryTime = 0; retryTime < 3;retryTime++)
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://" + domainName + "/index.html");
                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    coverResult = EntityUtils.toString(entity, "UTF-8");
                    if (coverResult.contains("table"))
                    {
                        logger.info("代码覆盖度响应内容为:" + coverResult);
                        coverResult = coverResult.split("<table")[1].split("table>")[0];
                        coverResult = "<table " + "border='2'cellspacing='1' cellpadding='1' width='1500'align=\"center\"" + coverResult;
                        coverResult = coverResult + "table>";
                        summary += "<p class=\"margin\" ><strong>代码覆盖度详情如下：</strong></p><p> </p>";
                        summary += coverResult;
                        logger.info("替换前的字符串:" + summary);
                        summary = summary.replace("href=\"", "href=\"http://" + domainName+"/");
                        summary = summary.replace("jacoco-resources/redbar.gif", "http://" + domainName + "/jacoco-resources/redbar.gif");
                        summary = summary.replace("jacoco-resources/greenbar.gif", "http://" + domainName + "/jacoco-resources/greenbar.gif");
                        logger.info("替换后的字符串:" + summary);
                        break;
                    }
                    else{
                        logger.info("尝试第"+retryTime+"次");
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        summary += String.format(
                "                <p class=\"margin\" ><strong>用例执行详情如下：</strong></p>  </div> ");
        return summary;
    }

    public String ReportNoCoverHeader(String noCover)
    {
        String noCoverList = "<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>div5px</title>\n" +
                "<script type=\"text/javascript\" src=\"http://www.sz886.com/js/jquery-1.9.1.min.js\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "$(document).ready(function(e) { \n" +
                "    $(\".btn\").click(function(){\n" +
                "         if(!$(\".div\").is(\":visible\")){\n" +
                "          $(\".div\").show(); \n" +
                "         }else{ \n" +
                "          $(\".div\").hide(); \n" +
                "        }\n" +
                "    });\n" +
                "       \n" +
                "});\n" +
                "</script>\n" +
                "</head>\n" +
                " \n" +
                "<body>\n" +
                "<p class=\"btn\"><strong>>未覆盖的接口如下:<strong></p>\n" +
                "<hr/>\n" +
                "<p class=\"div\" hidden>"+noCover+"</p>\n" +
                "</body>\n" +
                "</html>";
        return noCoverList;
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
            }
            else
            {
                checkResult = "fail";
            }
            logger.info("checkResult:"+checkResult);
            responseResultPrintWriter.println(reportDetails(suiteId,suiteName,testType,testinfos.getJSONObject(sceneInfosLength).getInteger("id"),testinfos.getJSONObject(sceneInfosLength).getString("name"),timeStamp2Date(testinfos.getJSONObject(sceneInfosLength).getLong("createTime")),checkResult));
        }
        return responseResultPrintWriter;
    }

    public void getPass(JSONArray testinfos)
    {
        for(int sceneInfosLength = 0;sceneInfosLength < testinfos.size();sceneInfosLength++)
        {
            if (testinfos.getJSONObject(sceneInfosLength).getInteger("check")==1)
            {
                this.passCount = this.passCount + 1;
            }
            else
            {
                this.failCount = this.failCount + 1;
            } }
    }

    public int[] encapsuReportDetail(String sysBranch,String obtainedResults,int isNeed,String domainName,String commitInfo)
    {
        int[ ]  operatResult=new int[2];
        logger.info("测试分支为:"+sysBranch);
        logger.info("返回包:"+obtainedResults);
        logger.info("获取到的服务是否需要检验接口覆盖度为:"+isNeed);
        File responseResultFile=new File("/home/webapps/yanxuan-g4test/responseResult.html");
//        File responseResultFile=new File("responseResult.html");
        String coverData = "";
        if (isNeed==1)
        {
            logger.info("进入接口覆盖处理");
//            File coverInterFile = new File("E:\\resultfile\\result.html");
            File coverInterFile = new File("/home/webapps/yanxuan-g4test/result.html");
            if (coverInterFile.isFile()&&coverInterFile.exists())
            {
                try {
                    InputStreamReader coverInterFileData = new InputStreamReader(new FileInputStream(coverInterFile),"utf-8");
                    BufferedReader coverInterReader = new BufferedReader(coverInterFileData);
                    String lineTxt = null;
                    while((lineTxt=coverInterReader.readLine())!=null)
                    {
                        coverData = coverData + lineTxt;
                        logger.info("每行的覆盖接口情况为:"+coverData);
                    }
                    coverInterReader.close();
                    coverInterFileData.close();
                    logger.info("获取到的接口覆盖度内容为:"+coverData);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        File responseResultFile=new File("responseResult.html");
        try {
            String checkResult = "fail";
//            FileWriter responseResultFileWriter =  new FileWriter(responseResultFile, false);
            BufferedWriter responseResultFileWriter = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (responseResultFile,false),"UTF-8"));
            PrintWriter responseResultPrintWriter = new PrintWriter(responseResultFileWriter);
            responseResultPrintWriter.println(ReportFileHeader("接口测试结果"));
            JSONObject obtainedResultsObj=JSON.parseObject(obtainedResults);
            responseResultPrintWriter.println(coverData);
            if (obtainedResultsObj.getInteger("code")==200)
            {
                int suiteId = obtainedResultsObj.getJSONObject("data").getInteger("id");
                logger.info("suiteId:"+suiteId);
                String suiteName = obtainedResultsObj.getJSONObject("data").getString("suiteName");
                logger.info("suiteName:"+suiteName);
                JSONArray sceneInfos = obtainedResultsObj.getJSONObject("data").getJSONArray("sceneInfo");
                JSONArray apiCaseInfos = obtainedResultsObj.getJSONObject("data").getJSONArray("apiCaseInfo");
                logger.info("sceneInfos:"+sceneInfos.toJSONString());
                logger.info("apiCaseInfos:"+apiCaseInfos.toJSONString());
                long startTime = obtainedResultsObj.getJSONObject("data").getLong("startDate");
                long endTime = obtainedResultsObj.getJSONObject("data").getLong("endDate");
                getPass(sceneInfos);
                getPass(apiCaseInfos);
                responseResultPrintWriter.println(reportSummary(startTime,endTime,this.passCount,this.failCount,sysBranch,isNeed,domainName,commitInfo));
                responseResultPrintWriter.println(header);
                responseResultPrintWriter = cyclicStat(suiteId,suiteName,"场景测试集",sceneInfos,responseResultPrintWriter,checkResult);
                responseResultPrintWriter = cyclicStat(suiteId,suiteName,"接口测试集",apiCaseInfos,responseResultPrintWriter,checkResult);
            }
            else
            {
                responseResultPrintWriter.println(reportSummary(0,0,this.passCount,this.failCount,sysBranch,0,domainName,commitInfo));
                String errorMsg = String.format(" <p class=\"margin\"><strong>执行错误原因:</strong> %s</p> \n</div> ",obtainedResultsObj.getString("msg"));
                responseResultPrintWriter.println(errorMsg);
            }
            responseResultPrintWriter.flush();
            responseResultFileWriter.flush();
            responseResultPrintWriter.close();
            responseResultFileWriter.close();
            logger.info("测试集执行结果写入文件完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
        operatResult[0] = this.passCount;
        operatResult[1] = this.failCount;
        logger.info("执行结果为通过:"+operatResult[0]+"个,失败:"+operatResult[1]);
        return operatResult;
    }
    public static void main(String[] args)
    {
        HandleResponse handleResponse = new HandleResponse();
        int[ ]  operatResult=new int[2];
        handleResponse.reportSummary(0, 12, 12, 0,"dev",2,"smartipc.you.163.com","");
//        handleResponse.encapsuReportDetail("{\"code\":200,\"msg\":\"OK\",\"data\":{\"id\":704,\"suiteName\":\"tob选仓临时测试集\",\"sceneInfo\":[{\"id\":2557,\"name\":\"tob订单调度成功\",\"createUser\":\"hzhuyuanyuan@corp.netease.com\",\"createTime\":1524809285000,\"subProjectId\":2196,\"check\":1},{\"id\":2541,\"name\":\"toB选仓\",\"createUser\":\"hzzhengyunpeng@corp.netease.com\",\"createTime\":1524731475000,\"subProjectId\":2196,\"check\":0}],\"apiCaseInfo\":[{\"id\":41641,\"name\":\"tob选仓失败\",\"createUser\":\"hzhuyuanyuan@corp.netease.com\",\"createTime\":1524722301000,\"subProjectId\":2196,\"url\":\"/smart-ipc/api/v1/omsapi/mps/receiveMsg\",\"check\":0}],\"cid\":\"1i1284\",\"executorName\":\"smartipc测试环境\",\"startDate\":1524906981999,\"endDate\":1524906987474,\"userName\":\"hzhuyuanyuan@corp.netease.com\",\"taskId\":\"d147d0eb-4ac4-11e8-a4d4-6d0a0c2e1ab9\",\"elapse\":5475}}");
//        operatResult = handleResponse.encapsuReportDetail("test","{\"code\":200,\"msg\":\"OK\",\"data\":{\"id\":704,\"suiteName\":\"tob选仓临时测试集\",\"sceneInfo\":[],\"apiCaseInfo\":[{\"id\":41641,\"name\":\"tob选仓失败\",\"createUser\":\"hzhuyuanyuan@corp.netease.com\",\"createTime\":1524722301000,\"subProjectId\":2196,\"url\":\"/smart-ipc/api/v1/omsapi/mps/receiveMsg\",\"check\":1}],\"cid\":\"1i1284\",\"executorName\":\"smartipc测试环境\",\"startDate\":1524906981999,\"endDate\":1524906987474,\"userName\":\"hzhuyuanyuan@corp.netease.com\",\"taskId\":\"d147d0eb-4ac4-11e8-a4d4-6d0a0c2e1ab9\",\"elapse\":5475}}",1);
//        System.out.println("执行通过:"+operatResult[0]+"失败为:"+operatResult[1]);

    }
}
