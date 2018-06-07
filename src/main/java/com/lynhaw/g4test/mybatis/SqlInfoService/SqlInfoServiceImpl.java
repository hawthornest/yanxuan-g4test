package com.lynhaw.g4test.mybatis.SqlInfoService;

import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import com.lynhaw.g4test.mybatis.mapper.SqlInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-05-30 20:58
 */

@Service
public class SqlInfoServiceImpl implements SqlService{
    @Autowired
    private SqlInfoMapper sqlInfoMapper;

    @Override
    public List<SqlInfoBean> getSqlInfoBeanInfo( int id) {
        SqlInfoBean sqlInfoBean = new SqlInfoBean();
        sqlInfoBean.setId(id);
        return sqlInfoMapper.findsqlInfo(sqlInfoBean);
    }

    @Override
    public int insert(String sqlmode,String sqlconninfo,String sqlusername,String sqlpassword) {
        SqlInfoBean sqlInfoBean = new SqlInfoBean(sqlmode,sqlconninfo,sqlusername,sqlpassword);
        return sqlInfoMapper.addsqlInfo(sqlInfoBean);
    }

    @Override
    public List<SqlInfoBean> getLimitSqlInfoBeanInfo(int limitStart, int limitEnd) {
        SqlInfoBean sqlInfoBean = new SqlInfoBean();
        return sqlInfoMapper.findlimitsqlInfo(limitStart,limitEnd );
    }
}
