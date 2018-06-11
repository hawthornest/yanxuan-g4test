package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.mybatis.SqlInfoService.SqlService;
import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-05-30 21:16
 */

@RestController
public class SqlControll {
    @Autowired
    private SqlService sqlService;
    Logger logger = Logger.getLogger(SqlControll.class);
    @RequestMapping("/getSqlInfo")
    public List<SqlInfoBean> getSqlInfo(int id)
    {
        List<SqlInfoBean> sqlInfoBeans = sqlService.getSqlInfoBeanInfo(id);
        System.out.println(sqlInfoBeans.toString());
        return sqlInfoBeans;
    }

    @RequestMapping("/getSqlInfobyinfo")
    public String getSqlInfobyinfo(String sqlconninfo)
    {
        List<SqlInfoBean> sqlInfoBeans = sqlService.findsqlInfobyInfo(sqlconninfo);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("code",200);
        jsonResult.put("count",1);
        jsonResult.put("data",sqlInfoBeans);
        logger.info("模糊查询结果为:"+jsonResult.toJSONString());
        return jsonResult.toJSONString();
    }

    @RequestMapping("/getLimitSqlInfo")
    public  String getLimitSqlInfo(int limitStart,int limitEnd)
    {
        int count = sqlService.getSqlInfocount();
        logger.info("查询数据库总数为:"+count);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("code",200);
        jsonResult.put("count",count);
        List<SqlInfoBean> sqlInfoBeans = sqlService.getLimitSqlInfoBeanInfo(limitStart,limitEnd);
        jsonResult.put("data",sqlInfoBeans);
        return jsonResult.toJSONString();
    }

    @RequestMapping("/insertSql")
    public int insertSqlInfo(String sqlmode,String sqlconninfo,String sqlusername,String sqlpassword)
    {
        int insertResult = sqlService.insert(sqlmode,sqlconninfo,sqlusername,sqlpassword);
        return insertResult;
    }


}
