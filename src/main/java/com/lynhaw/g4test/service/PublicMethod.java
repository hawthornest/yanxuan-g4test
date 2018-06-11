package com.lynhaw.g4test.service;

import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-06-07 20:43
 */

@Service
public class PublicMethod {
    Logger logger = Logger.getLogger(PublicMethod.class);
    public void OperaException(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        logger.error(sw.toString());
    }

    public String isSqlEmpty(List<SqlInfoBean> sqlInfoBeans,JSONObject jsonResult)
    {
        if (sqlInfoBeans.size()==0)
        {
            logger.info("传入数据库id错误,在数据库表中,未维护:");
            jsonResult.put("code",400);
            jsonResult.put("errorMsg","操作的数据库未在管理平台维护,请先维护管理关系");
            return jsonResult.toJSONString();
        }
        else
        {
            return "";
        }

    }
}
