package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.mybatis.SqlInfoService.SqlService;
import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import com.lynhaw.g4test.service.MySqlOper;
import com.lynhaw.g4test.service.PublicMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    MySqlOper mySqlOper;

    Logger logger = Logger.getLogger(GetSqlInfo.class);

    @RequestMapping("/selectSqlIfo")
    public String GetSelectSqlIfo(@RequestParam(defaultValue = "999999999")int id, @RequestParam(defaultValue = "999999999")String inputSql)
    {
        if (id==999999999||inputSql.equals("999999999"))
        {
            return "{\"code\":400,\"errorMsg\":\"输入参数不全,请完整输入id和inputSql\"}";
        }
        String result = mySqlOper.getSelectSqlIfo(id,inputSql);
        return result;
    }

    @RequestMapping("/writeSqlIfo")
    public String updateSqlIfo(@RequestParam(defaultValue = "999999999")int id,@RequestParam(defaultValue = "999999999")String writeSql)
    {
        if (id==999999999||writeSql.equals("999999999"))
        {
            return "{\"code\":400,\"errorMsg\":\"输入参数不全,请完整输入id和inputSql\"}";
        }
        String result = mySqlOper.writeSqlIfo(id, writeSql);
        return result;
    }


}
