package com.lynhaw.g4test.mybatis.SqlInfoService;

import com.lynhaw.g4test.controller.TaskRun;
import com.lynhaw.g4test.mybatis.beans.ServerBeans;
import com.lynhaw.g4test.mybatis.mapper.ServerMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-06-26 13:54
 */
@Service
public class SysInfoServiceImpl {
    Logger logger = Logger.getLogger(SysInfoServiceImpl.class);
    @Autowired
    private ServerMapper serverMapper;


    public List<ServerBeans> getInfolimit(int limitStart, int limitEnd) {
        return serverMapper.selectInfolimit(limitStart,limitEnd);
    }

    public int updateSysInfo(String serverName, String addressees,Long id)
    {
        return serverMapper.updateInfo(new ServerBeans(id,serverName,addressees));
    }

    public int deleteSysInfo(Long id)
    {
        return serverMapper.deleteInfo(id);
    }

    public List<ServerBeans> getInfobyname(String serverNameInfo) {
        return serverMapper.selectInfobyname(serverNameInfo);
    }


    public ServerBeans getInfobytaskId(String taskId) {
        return serverMapper.selectInfobytaskId(taskId);
    }

    public int updateInfo(String taskId,String sysBranch,int isNeed ,int id) {
        logger.info("传入的isNeed="+isNeed);
        return serverMapper.updateInfoBytaskId(taskId,sysBranch,isNeed,id);
    }


    public int updateCallBackTimeByTaskId(String taskId) {
        Calendar cal = Calendar.getInstance();
        Long currTime = cal.getTime().getTime();
        logger.info("当前时间为:"+currTime);
        return serverMapper.updateCallBackTime(currTime,taskId);
    }

    public int updateCallBackStatusByTaskId(int callback,String taskId) {
        return serverMapper.updateCallBackStatus(callback,taskId);
    }

    public int updateCommitInfoByTaskId(String commitInfo,String taskId) {
        return serverMapper.updateCommitInfo(commitInfo,taskId);
    }

    public Long findUpdateTimeById(int id) {
        return serverMapper.findUpdateTime(id);
    }

    public int insertInfo(String serverName, String addressees) {
        return serverMapper.insertSysInfo(new ServerBeans(serverName,addressees));
    }

    public int getsyscount() {
        return serverMapper.findsyscount();
    }
}
