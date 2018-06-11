package com.lynhaw.g4test.mybatis.SqlInfoService;

import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;

import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-05-30 20:57
 */
public interface  SqlService {
    public List<SqlInfoBean> getSqlInfoBeanInfo(int id);
    public List<SqlInfoBean> findsqlInfobyInfo(String sqlconninfo);
    public int getSqlInfocount();
    public int insert(String sqlmode,String sqlconninfo,String sqlusername,String sqlpassword);
    public List<SqlInfoBean> getLimitSqlInfoBeanInfo(int limitStart,int limitEnd);
}
