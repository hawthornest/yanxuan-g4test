package com.lynhaw.g4test.mybatis.mapper;

import com.lynhaw.g4test.mybatis.beans.ServerBeans;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-06-26 14:24
 */

@Mapper
@Component(value = "ServerMapper")
public interface ServerMapper {
    public List<ServerBeans> selectInfolimit(@Param("limitStart") int limitStart, @Param("limitEnd") int limitEnd);
    public List<ServerBeans> selectInfobyname(@Param("serverNameInfo") String serverNameInfo);
    public ServerBeans selectInfobytaskId(@Param("taskId") String taskId);
    public int updateInfoBytaskId(@Param("taskId") String taskId,@Param("sysBranch") String sysBranch,@Param("Need") int Need, @Param("id") int id);
    public int updateCallBackTime(@Param("updateTime") Long updateTime,@Param("taskId") String taskId);
    public int updateCallBackStatus(@Param("callback") int callback,@Param("taskId") String taskId);
    public int updateCommitInfo(@Param("commitInfo") String commitInfo,@Param("taskId") String taskId);
    public Long findUpdateTime(@Param("id") int id);
    public int insertSysInfo(ServerBeans serverBeans);
    public int findsyscount();
    public int updateInfo(ServerBeans serverBeans);
    public int deleteInfo(@Param("id") Long id);
}
