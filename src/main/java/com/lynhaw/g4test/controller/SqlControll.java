package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.mybatis.SqlInfoService.SqlService;
import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @Author yyhu3
 * @Date 2018-05-30 21:16
 */

@RestController
public class SqlControll {
    @Autowired
    private SqlService sqlService;
    Logger logger = Logger.getLogger(SqlControll.class);

    @ApiOperation(value="根据id查询数据库信息", notes="")
    @RequestMapping("/getSqlInfo")
    public List<SqlInfoBean> getSqlInfo(int id)
    {
        MDC.put("traceId", UUID.randomUUID().toString());
        List<SqlInfoBean> sqlInfoBeans = sqlService.getSqlInfoBeanInfo(id);
        System.out.println(sqlInfoBeans.toString());
        MDC.remove("traceId");
        return sqlInfoBeans;
    }

    @ApiOperation(value="根据连接信息查询数据库信息", notes="")
    @RequestMapping("/getSqlInfobyinfo")
    public String getSqlInfobyinfo(String sqlconninfo)
    {
        MDC.put("traceId", UUID.randomUUID().toString());
        List<SqlInfoBean> sqlInfoBeans = sqlService.findsqlInfobyInfo(sqlconninfo);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("code",200);
        jsonResult.put("count",1);
        jsonResult.put("data",sqlInfoBeans);
        logger.info("模糊查询结果为:"+jsonResult.toJSONString());
        MDC.remove("traceId");
        return jsonResult.toJSONString();
    }


    @ApiOperation(value="按照页数查询数据库信息", notes="")
    @RequestMapping("/getLimitSqlInfo")
    public  String getLimitSqlInfo(int limitStart,int limitEnd)
    {
        MDC.put("traceId", UUID.randomUUID().toString());
//        logger.info("传入的traceId为:"+UUID.randomUUID().toString());
        int count = sqlService.getSqlInfocount();
        logger.info("查询数据库总数为:"+count);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("code",200);
        jsonResult.put("count",count);
        List<SqlInfoBean> sqlInfoBeans = sqlService.getLimitSqlInfoBeanInfo(limitStart,limitEnd);
        jsonResult.put("data",sqlInfoBeans);
        MDC.remove("traceId");
        return jsonResult.toJSONString();
    }


    @ApiOperation(value="新增维护的数据库信息", notes="")
    @RequestMapping("/insertSql")
    public int insertSqlInfo(String sqlmode,String sqlconninfo,String sqlusername,String sqlpassword,String sqlname)
    {
        MDC.put("traceId", UUID.randomUUID().toString());
        String pattern="";
        int insertResult = 0;
        if (sqlmode.equals("mysql"))
        {
            pattern = "jdbc:mysql://.*:.*/.*";
        }
        else
        {
            pattern = ".*key=.*secret.key.*";

        }
        boolean isMatch = Pattern.matches(pattern, sqlconninfo);
        if (isMatch)
        {
            try {
                logger.info("原始输入的数据库连接信息为:"+sqlconninfo);
                insertResult = sqlService.insert(sqlmode,URLDecoder.decode(sqlconninfo, "utf-8"),sqlusername,sqlpassword,sqlname);
                logger.info("处理后的数据库连接信息为:"+URLDecoder.decode(sqlconninfo, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.info(e);
            }
        }
        else{
            logger.info("输入的数据库信息未满足校验条件");
        }
        MDC.remove("traceId");
        return insertResult;
    }

    @ApiOperation(value="删除维护的数据库信息", notes="")
    @RequestMapping("/deleteSql")
    public String delSql(int id)
    {
        MDC.put("traceId", UUID.randomUUID().toString());
        logger.info("需要删除的数据库id为:"+id);
        int result = sqlService.deleteSqlInfo(id);
        JSONObject jsonResult = new JSONObject();
        if (result==1)
        {
            logger.info("删除数据库表记录的结果为成功");
            jsonResult.put("code",200);
        }
        else{
            logger.info("删除数据库表记录的结果为失败");
            jsonResult.put("code",400);
        }
        MDC.remove("traceId");
        return jsonResult.toJSONString();
    }


    @ApiOperation(value="修改数据库信息", notes="")
    @RequestMapping("/updateSql")
    public String updateSql(String sqlmode, String sqlconninfo, String sqlusername, String sqlpassword, String sqlname, int id)
    {
        MDC.put("traceId", UUID.randomUUID().toString());
        logger.info("需要修改的数据库id为:"+id);
        int result = 0;
        try {
            logger.info("原始输入的数据库连接信息为:"+sqlconninfo);
            result = sqlService.updateSqlInfo(sqlmode, URLDecoder.decode(sqlconninfo, "utf-8"),sqlusername,sqlpassword,sqlname,id);
            logger.info("处理后的数据库连接信息为:"+URLDecoder.decode(sqlconninfo, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e);
        }
        JSONObject jsonResult = new JSONObject();
        if (result==1)
        {
            logger.info("修改数据库表记录的结果为成功");
            jsonResult.put("code",200);
        }
        else{
            logger.info("修改数据库表记录的结果为失败");
            jsonResult.put("code",400);
        }
        MDC.remove("traceId");
        return jsonResult.toJSONString();
    }

}
