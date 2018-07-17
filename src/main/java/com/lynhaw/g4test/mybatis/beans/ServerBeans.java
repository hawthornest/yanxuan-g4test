package com.lynhaw.g4test.mybatis.beans;

/**
 * @Author yyhu3
 * @Date 2018-06-26 13:51
 */
public class ServerBeans {
    private String serverName;
    private String taskId;
    private String addressees;
    private Long id;
    private Long updateTime;
    private String sysBranch;

    public String getSysBranch() {
        return sysBranch;
    }

    public void setSysBranch(String sysBranch) {
        this.sysBranch = sysBranch;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public ServerBeans(Long id, String serverName, String taskId, String addressees)
    {
        this.addressees = addressees;
        this.id = id;
        this.serverName = serverName;
        this.taskId = taskId;
    }

    public ServerBeans(Long id, String serverName, String taskId, String addressees,Long updateTime,String sysBranch)
    {
        this.sysBranch = sysBranch;
        this.addressees = addressees;
        this.id = id;
        this.serverName = serverName;
        this.taskId = taskId;
        this.updateTime = updateTime;
    }

    public ServerBeans(String serverName, String addressees)
    {
        this.addressees = addressees;
        this.id = id;
        this.serverName = serverName;
        this.taskId = taskId;
    }

    public ServerBeans(Long id, String serverName, String addressees)
    {
        this.addressees = addressees;
        this.id = id;
        this.serverName = serverName;
    }

    public ServerBeans(String serverName, String taskId, String addressees)
    {
        this.addressees = addressees;
        this.serverName = serverName;
        this.taskId = taskId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAddressees() {
        return addressees;
    }

    public void setAddressees(String addressees) {
        this.addressees = addressees;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
