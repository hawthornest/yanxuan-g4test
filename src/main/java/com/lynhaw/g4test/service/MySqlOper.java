package com.lynhaw.g4test.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.controller.GetSqlInfo;
import com.lynhaw.g4test.mybatis.SqlInfoService.SqlService;
import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-06-11 15:43
 */
@Service
public class MySqlOper {
    @Autowired
    private PublicMethod publicMethod;
    @Autowired
    private SqlService sqlService;

    Logger logger = Logger.getLogger(MySqlOper.class);
    public String getSelectSqlIfo(int id,String inputSql)
    {
        Connection con;
        JSONObject jsonResult = new JSONObject();
        logger.info(String.format("输入参数为id=%s,查询sql=%s",id,inputSql));
        List<SqlInfoBean> sqlInfoBeans = sqlService.getSqlInfoBeanInfo(id);
        String sqlIsSqlEmpty = publicMethod.isSqlEmpty(sqlInfoBeans,jsonResult);
        if (sqlIsSqlEmpty.equals(""))
        {
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
        }
        else
        {
            return sqlIsSqlEmpty;
        }
        return null;
    }


    public String writeSqlIfo(int id,String writeSql)
    {
        Connection con;
        logger.info(String.format("输入参数为id=%s,更新sql=%s",id,writeSql));
        List<SqlInfoBean> sqlInfoBeans = sqlService.getSqlInfoBeanInfo(id);
        JSONObject jsonResult = new JSONObject();
        String sqlIsSqlEmpty = publicMethod.isSqlEmpty(sqlInfoBeans,jsonResult);
        if (sqlIsSqlEmpty.equals(""))
        {
            logger.info("查询结果为:"+sqlInfoBeans.get(0).toString());
            if (sqlInfoBeans.get(0).getSqlmode().equals("mysql"))
            {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        con = DriverManager.getConnection(sqlInfoBeans.get(0).getSqlconninfo(),sqlInfoBeans.get(0).getSqlusername(),sqlInfoBeans.get(0).getSqlpassword());
                        Statement stmt=con.createStatement();
                        int resultData = stmt.executeUpdate(writeSql);//执行sql语句
                        if (resultData==1)
                        {
                            jsonResult.put("code",200);
                            jsonResult.put("data",resultData);
                        }
                        else
                        {
                            jsonResult.put("code",400);
                            jsonResult.put("data","写数据库失败");
                        }
                        con.close();
                    } catch (SQLException e) {
                        jsonResult.put("code",500);
                        jsonResult.put("errordata","数据库连接失败");
                        publicMethod.OperaException(e);
                        e.printStackTrace();
                    }

                } catch (ClassNotFoundException e) {
                    jsonResult.put("code",500);
                    jsonResult.put("errordata","数据库连接失败");
                    publicMethod.OperaException(e);
                    e.printStackTrace();
                }finally {
                    return jsonResult.toJSONString();
                }
            }
            else
            {
//            TO DO
                return null;
            }

        }
        else
        {
            return sqlIsSqlEmpty;
        }
        }
}
