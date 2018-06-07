package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.mybatis.SqlInfoService.SqlService;
import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import com.lynhaw.g4test.service.PublicMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-05-30 21:16
 */

@RestController
public class GetSqlInfo {
    @Autowired
    private SqlService sqlService;

    @Autowired
    private PublicMethod publicMethod;

    Logger logger = Logger.getLogger(GetSqlInfo.class);

    @RequestMapping("/selectSqlIfo")
    public String GetSelectSqlIfo(int id,String inputSql)
    {
        Connection con;
        logger.info(String.format("输入参数为id=%s,查询sql=%s",id,inputSql));
        List<SqlInfoBean> sqlInfoBeans = sqlService.getSqlInfoBeanInfo(id);
        logger.info("查询结果为:"+sqlInfoBeans.get(0).toString());
        if (sqlInfoBeans.get(0).getSqlmode().equals("mysql"))
        {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                try {
                    con = DriverManager.getConnection(sqlInfoBeans.get(0).getSqlconninfo(),sqlInfoBeans.get(0).getSqlusername(),sqlInfoBeans.get(0).getSqlpassword());
                    Statement statement = con.createStatement();
                    ResultSet rs = statement.executeQuery(inputSql);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    JSONObject jsonResult = new JSONObject();
                    JSONArray sqlJsonArray = new JSONArray();
                    jsonResult.put("code",200);
                    jsonResult.put("data",sqlJsonArray);
                    while(rs.next())
                    {
                        JSONObject sqlColValues = new JSONObject();
                        for (int sqlTemp=1;sqlTemp<=rsmd.getColumnCount();sqlTemp++)
                        {
                            sqlColValues.put(rsmd.getColumnName(sqlTemp),rs.getObject(sqlTemp));
                            logger.info(String.format("获取到的列名为:%s,对应的值为:%s",rsmd.getColumnName(sqlTemp),rs.getObject(sqlTemp)));
                        }
                        sqlJsonArray.add(sqlColValues);
                    }
                    logger.info("返回值为:"+sqlJsonArray.toJSONString());
                    return jsonResult.toJSONString();
                } catch (SQLException e) {
                    publicMethod.OperaException(e);
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                publicMethod.OperaException(e);
                e.printStackTrace();
            }
        }
        return null;
    }


}
