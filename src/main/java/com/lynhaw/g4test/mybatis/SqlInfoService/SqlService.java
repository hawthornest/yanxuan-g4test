package com.lynhaw.g4test.mybatis.SqlInfoService;

import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-05-30 20:57
 */
public interface  SqlService {
    public List<SqlInfoBean> getSqlInfoBeanInfo(int id);
    public List<SqlInfoBean> findsqlInfobyInfo(String sqlconninfo);
    public int getSqlInfocount();
    public int insert(String sqlmode,String sqlconninfo,String sqlusername,String sqlpassword,String sqlname);
    public List<SqlInfoBean> getLimitSqlInfoBeanInfo(int limitStart,int limitEnd);
    public int deleteSqlInfo(int id);
    public int updateSqlInfo(String sqlmode,String sqlconninfo,String sqlusername,String sqlpassword,String sqlname,int id);
}
