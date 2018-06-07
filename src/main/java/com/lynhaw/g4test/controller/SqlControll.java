package com.lynhaw.g4test.controller;

import com.lynhaw.g4test.mybatis.SqlInfoService.SqlService;
import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
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
    @RequestMapping("/getSqlInfo")
    public List<SqlInfoBean> getSqlInfo(int id)
    {
        List<SqlInfoBean> sqlInfoBeans = sqlService.getSqlInfoBeanInfo(id);
        System.out.println(sqlInfoBeans.toString());
        return sqlInfoBeans;
    }

    @RequestMapping("getLimitSqlInfo")
    public  List<SqlInfoBean> getLimitSqlInfo(int limitStart,int limitEnd)
    {
        List<SqlInfoBean> sqlInfoBeans = sqlService.getLimitSqlInfoBeanInfo(limitStart,limitEnd);
        return sqlInfoBeans;
    }

    @RequestMapping("/insertSql")
    public int insertSqlInfo(String sqlmode,String sqlconninfo,String sqlusername,String sqlpassword)
    {
        int insertResult = sqlService.insert(sqlmode,sqlconninfo,sqlusername,sqlpassword);
        return insertResult;
    }


}
