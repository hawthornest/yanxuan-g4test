package com.lynhaw.g4test.mybatis.mapper;

import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author yyhu3
 * @Date 2018-05-30 17:38
 */

@Mapper
@Component(value = "SqlInfoMapper")
public interface  SqlInfoMapper {
    public List<SqlInfoBean> findsqlInfo(SqlInfoBean sqlInfoBean);
    public int findsqlcount();
    public List<SqlInfoBean> findsqlInfobyInfo(@Param("sqltestinfo")String sqltestinfo);
    public int addsqlInfo(SqlInfoBean sqlInfoBean);
    public List<SqlInfoBean> findlimitsqlInfo(@Param("limitStart")int limitStart, @Param("limitEnd")int limitEnd);
    public int deleteSql(@Param("id")int id);
    public int updateSql(SqlInfoBean sqlInfoBean);


}
