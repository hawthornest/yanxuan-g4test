package com.lynhaw.g4test.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import com.lynhaw.g4test.request.HttpRquest;
import com.lynhaw.g4test.request.HttpsRquest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @Author yyhu3
 * @Date 2018-06-07 20:43
 */

@Service
public class PublicMethod {
    HttpRquest httpRquest = new HttpRquest();
    HttpsRquest httpsRquest = new HttpsRquest();
    Logger logger = Logger.getLogger(PublicMethod.class);
    public void OperaException(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        logger.error(sw.toString());
    }

    public ArrayList<String> interfaceContrast(String requestUrl,String testId)
    {
        ArrayList<String> interfaceServers = new ArrayList<String>();
        ArrayList<String> interfaceGotest = new ArrayList<String>();
        ArrayList<String> interfaceDiff = new ArrayList<String>();
        String[] monitorInter = {"/","/health","/version"};
        String interfaceServerInfosAct = httpRquest.httpGet(requestUrl);
        String interfaceGotestInfosExist = httpsRquest.httpsTaskSuiteInfo(testId);
        JSONObject interfaceServerInfosActJson = JSON.parseObject(interfaceServerInfosAct);
        JSONObject interfaceGotestInfosExistJson = JSON.parseObject(interfaceGotestInfosExist);
        JSONObject interfaceServerInfosArray = interfaceServerInfosActJson.getJSONObject("paths");
        JSONArray interfaceGotestInfosExistArray = interfaceGotestInfosExistJson.getJSONObject("data").getJSONArray("coverApiList");
        for (Map.Entry<String,Object>interfaceServerInfos:interfaceServerInfosArray.entrySet())
        {
            interfaceServers.add(interfaceServerInfos.getKey());
        }
        for (int gotestTemp = 0;gotestTemp < interfaceGotestInfosExistArray.size();gotestTemp++)
        {
            interfaceGotest.add(interfaceGotestInfosExistArray.getJSONObject(gotestTemp).getString("path"));
        }
        for (int interfaceServersTemp = 0; interfaceServersTemp < interfaceServers.size(); interfaceServersTemp++)
        {
            if (!interfaceGotest.contains(interfaceServers.get(interfaceServersTemp)))
            {
                logger.info("gotest中未覆盖到的接口为:"+interfaceServers.get(interfaceServersTemp));
                if (!Arrays.asList(monitorInter).contains(interfaceServers.get(interfaceServersTemp)))
                {
                    interfaceDiff.add(interfaceServers.get(interfaceServersTemp));
                }
            }
        }
        return interfaceDiff;
    }

    public String getRandomString(int length){
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
            //产生0-61的数字
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    public String isSqlEmpty(List<SqlInfoBean> sqlInfoBeans,JSONObject jsonResult)
    {
        if (sqlInfoBeans.size()==0)
        {
            logger.info("传入数据库id错误,在数据库表中,未维护:");
            jsonResult.put("code",400);
            jsonResult.put("errorMsg","操作的数据库未在管理平台维护,请先维护管理关系");
            return jsonResult.toJSONString();
        }
        else
        {
            return "";
        }

    }

    public int ReportNoCoverHeader(String requestUrl,String testId)
    {
        ArrayList<String> interfaceServers = interfaceContrast(requestUrl,testId);
//        File interfaceContrastFile = new File("E:\\resultfile\\result.html");
        File interfaceContrastFile = new File("/home/webapps/yanxuan-g4test/result.html");
        try {
            BufferedWriter interfaceContrastFileWriter = new BufferedWriter(new OutputStreamWriter (new FileOutputStream (interfaceContrastFile,false),"UTF-8"));
            PrintWriter responseResultPrintWriter = new PrintWriter(interfaceContrastFileWriter);
            String noCoverInter = "";
            for(int temp=0;temp<interfaceServers.size();temp++)
            {
                noCoverInter = noCoverInter + interfaceServers.get(temp) + "<br>";
            }
            if (noCoverInter=="")
            {
                logger.info("gotest测试集覆盖的所有接口");
                responseResultPrintWriter.println("<p style=\"margin-left:200px;\"><strong>测试集覆盖了全部接口！！！</strong></p>");
            }
            else
            {
                logger.info("gotest测试集未覆盖的接口为:"+noCoverInter);
                String noCoverList =
                        "<p style=\"margin-left:200px;\"><strong>>未覆盖的接口如下:<strong></p>\n" +
                                "<hr/>\n" +
                                "<p style=\"margin-left:200px;\">"+noCoverInter+"</p>\n" +
                                "<hr/>\n";
                responseResultPrintWriter.println(noCoverList);
            }
            responseResultPrintWriter.flush();
            interfaceContrastFileWriter.flush();
            responseResultPrintWriter.close();
            interfaceContrastFileWriter.close();
            return 200;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 400;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 400;
        } catch (IOException e) {
            e.printStackTrace();
            return 400;
        }
    }
    public static void main(String[] args)
    {
        PublicMethod PublicMethod = new PublicMethod();
        PublicMethod.ReportNoCoverHeader("http://smartipc.you.163.com/v2/api-docs","1024");
    }
}
