package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.mybatis.SqlInfoService.SqlService;
import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
        List<SqlInfoBean> sqlInfoBeans = sqlService.getSqlInfoBeanInfo(id);
        System.out.println(sqlInfoBeans.toString());
        return sqlInfoBeans;
    }


    @ApiOperation(value="根据连接信息查询数据库信息", notes="")
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


    @ApiOperation(value="按照页数查询数据库信息", notes="")
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


    @ApiOperation(value="新增维护的数据库信息", notes="")
    @RequestMapping("/insertSql")
    public int insertSqlInfo(String sqlmode,String sqlconninfo,String sqlusername,String sqlpassword)
    {
        String content = "I am noob " ;
        String pattern="";
        int insertResult = 0;
        if (sqlmode.equals("mysql"))
        {
            pattern = "jdbc:mysql://.*:.*/.*";
        }
        else
        {
            pattern = ".*?key=.*secret.key&logdir=.*";

        }
        boolean isMatch = Pattern.matches(pattern, sqlconninfo);
        if (isMatch)
        {
            insertResult = sqlService.insert(sqlmode,sqlconninfo,sqlusername,sqlpassword);
        }
        else{
            logger.info("输入的数据库信息未满足校验条件");
        }
        return insertResult;
    }


}
