package com.lynhaw.g4test.mybatis.SqlInfoService;

import com.lynhaw.g4test.mybatis.beans.ServerBeans;
import com.lynhaw.g4test.mybatis.mapper.ServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-06-26 13:54
 */
@Service
public class SysInfoServiceImpl {
    @Autowired
    private ServerMapper serverMapper;


    public List<ServerBeans> getInfolimit(int limitStart, int limitEnd) {
        return serverMapper.selectInfolimit(limitStart,limitEnd);
    }


    public List<ServerBeans> getInfobyname(String serverNameInfo) {
        return serverMapper.selectInfobyname(serverNameInfo);
    }


    public ServerBeans getInfobytaskId(String taskId) {
        return serverMapper.selectInfobytaskId(taskId);
    }

    public int updateInfo(String taskId, int id) {
        return serverMapper.updateInfoBytaskId(taskId,id);
    }

    public int insertInfo(String serverName, String taskId, String addressees) {
        return serverMapper.insertSysInfo(new ServerBeans(serverName,taskId,addressees));
    }

    public int getsyscount() {
        return serverMapper.findsyscount();
    }
}
