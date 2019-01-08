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
    private int isNeed;

    public String getCommitInfo() {
        return commitInfo;
    }

    public void setCommitInfo(String commitInfo) {
        this.commitInfo = commitInfo;
    }

    private int callback;
    private String domainName;
    private String commitInfo;

    public int getCallback() {
        return callback;
    }

    public void setCallback(int callback) {
        this.callback = callback;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public int getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(int isNeed) {
        this.isNeed = isNeed;
    }

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

    public ServerBeans(Long id, String serverName, String taskId, String addressees,Long updateTime,String sysBranch,int isNeed,int callback,String commitInfo)
    {
        this.sysBranch = sysBranch;
        this.addressees = addressees;
        this.id = id;
        this.serverName = serverName;
        this.taskId = taskId;
        this.updateTime = updateTime;
        this.isNeed = isNeed;
        this.callback = callback;
        this.commitInfo = commitInfo;
    }


    public ServerBeans(Long id, String serverName, String taskId, String addressees,Long updateTime,String sysBranch,int isNeed,String domainName,int callback,String commitInfo)
    {
        this.sysBranch = sysBranch;
        this.addressees = addressees;
        this.id = id;
        this.serverName = serverName;
        this.taskId = taskId;
        this.updateTime = updateTime;
        this.isNeed = isNeed;
        this.domainName = domainName;
        this.commitInfo = commitInfo;
    }

    public ServerBeans(Long id, String serverName, String taskId, String addressees,Long updateTime,String sysBranch,int callback,String commitInfo)
    {
        this.sysBranch = sysBranch;
        this.addressees = addressees;
        this.id = id;
        this.serverName = serverName;
        this.taskId = taskId;
        this.updateTime = updateTime;
        this.commitInfo = commitInfo;
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
