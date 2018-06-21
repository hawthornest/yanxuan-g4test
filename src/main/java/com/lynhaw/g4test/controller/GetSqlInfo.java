package com.lynhaw.g4test.controller;

import com.lynhaw.g4test.service.MySqlOper;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.util.List;
import java.util.UUID;

/**
 * @Author yyhu3
 * @Date 2018-05-30 21:16
 */

@RestController
public class GetSqlInfo {

    @Autowired
    MySqlOper mySqlOper;

    Logger logger = Logger.getLogger(GetSqlInfo.class);


    @ApiOperation(value="根据数据库维护的数据库表信息查询", notes="")
    @RequestMapping("/selectSqlIfo")
    public String GetSelectSqlIfo(@RequestParam(defaultValue = "999999999")int id, @RequestParam(defaultValue = "999999999")String inputSql)
    {
//        logger.info("传入的uuid:"+UUID.randomUUID().toString());
        MDC.put("traceId", UUID.randomUUID().toString());
        if (id==999999999||inputSql.equals("999999999"))
        {
            return "{\"code\":400,\"errorMsg\":\"输入参数不全,请完整输入id和inputSql\"}";
        }
        String result = mySqlOper.getSelectSqlIfo(id,inputSql);
        MDC.remove("traceId");
        return result;
    }


    @ApiOperation(value="根据数据库维护的数据库表信息update、insert", notes="")
    @RequestMapping("/writeSqlIfo")
    public String updateSqlIfo(@RequestParam(defaultValue = "999999999")int id,@RequestParam(defaultValue = "999999999")String writeSql)
    {
        MDC.put("traceId", UUID.randomUUID().toString());
        if (id==999999999||writeSql.equals("999999999"))
        {
            return "{\"code\":400,\"errorMsg\":\"输入参数不全,请完整输入id和inputSql\"}";
        }
        String result = null;
            logger.info("输入请求参数为:"+writeSql);
            result = mySqlOper.writeSqlIfo(id, writeSql);
        MDC.remove("traceId");
        return result;
    }


}
