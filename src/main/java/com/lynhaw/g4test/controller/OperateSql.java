package com.lynhaw.g4test.controller;

import com.lynhaw.g4test.mybatis.SqlInfoService.SysInfoServiceImpl;
import com.lynhaw.g4test.mybatis.beans.ServerBeans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/yanxuan/getInfolimit")
    public List<ServerBeans> getInfolimit(int limitStart, int limitEnd){
        List<ServerBeans> ServerBeans = sysInfoServiceImpl.getInfolimit(limitStart,limitEnd);
        return ServerBeans;
    }

    @GetMapping("/yanxuan/insertInfo")
    public int insertInfo(String serverName, String taskId, String addressees)
    {
        return sysInfoServiceImpl.insertInfo(serverName,taskId,addressees);
    }
}
