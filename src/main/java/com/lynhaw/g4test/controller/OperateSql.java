package com.lynhaw.g4test.controller;

import com.alibaba.fastjson.JSONObject;
import com.lynhaw.g4test.mybatis.SqlInfoService.SysInfoServiceImpl;
import com.lynhaw.g4test.mybatis.beans.ServerBeans;
import com.lynhaw.g4test.mybatis.beans.SqlInfoBean;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-06-26 14:58
 */
@RestController
public class OperateSql {
    @Autowired
    private SysInfoServiceImpl sysInfoServiceImpl;
    Logger logger = Logger.getLogger(OperateSql.class);
    @GetMapping("/yanxuan/getInfolimit")
    public String getInfolimit(int limitStart, int limitEnd){
        List<ServerBeans> ServerBeans = sysInfoServiceImpl.getInfolimit(limitStart,limitEnd);
        int sysCount = sysInfoServiceImpl.getsyscount();
        JSONObject jsonSysInfo = new JSONObject();
        jsonSysInfo.put("count",sysCount);
        jsonSysInfo.put("data",ServerBeans);
        jsonSysInfo.put("code",200);
        return jsonSysInfo.toJSONString();
    }

    @RequestMapping("/getUpdateTimeById")
    public  String getUpdateTimeById(int sysId)
    {
        Long updateTime = sysInfoServiceImpl.findUpdateTimeById(sysId);
        logger.info("查询到的更新时间为:"+updateTime);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("code",200);
        jsonResult.put("updateTime",updateTime);
        return jsonResult.toJSONString();
    }

    @GetMapping("/yanxuan/findsyscount")
    public String findsyscount(){
        int sysCount = sysInfoServiceImpl.getsyscount();
        JSONObject jsonSysCount = new JSONObject();
        jsonSysCount.put("data",200);
        jsonSysCount.put("count",sysCount);
        return jsonSysCount.toJSONString();
    }

    @GetMapping("/yanxuan/findInfobyName")
    public String selectSysInfobyname(String sysName){
        List<ServerBeans> ServerBeans = sysInfoServiceImpl.getInfobyname(sysName);
        int sysCount = sysInfoServiceImpl.getsyscount();
        JSONObject jsonSysCount = new JSONObject();
        jsonSysCount.put("data",200);
        jsonSysCount.put("count",sysCount);
        jsonSysCount.put("data",ServerBeans);
        return jsonSysCount.toJSONString();
    }

    @GetMapping("/yanxuan/insertInfo")
    public int insertInfo(String serverName,String addressees)
    {
        return sysInfoServiceImpl.insertInfo(serverName,addressees);
    }

    @GetMapping("/yanxuan/updateInfo")
    public String updateSyInfo(String serverName, String addressees,Long id)
    {
        int resultData = sysInfoServiceImpl.updateSysInfo(serverName,addressees,id);
        JSONObject jsonUpdateCount = new JSONObject();
        if (resultData>0)
        {
            jsonUpdateCount.put("data",200);
            jsonUpdateCount.put("count",resultData);
        }
        else
        {
            jsonUpdateCount.put("data",400);
            jsonUpdateCount.put("errMsg","修改失败,请联系管理员");
        }
        return  jsonUpdateCount.toJSONString();
    }

    @GetMapping("/yanxuan/deleteInfo")
    public String delSyInfo(Long id)
    {
        int delData = sysInfoServiceImpl.deleteSysInfo(id);
        JSONObject jsonDelCount = new JSONObject();
        if (delData>0)
        {
            jsonDelCount.put("data",200);
            jsonDelCount.put("count",delData);
        }
        else
        {
            jsonDelCount.put("data",400);
            jsonDelCount.put("errMsg","删除失败,请联系管理员");
        }
        return jsonDelCount.toJSONString();
    }
}
