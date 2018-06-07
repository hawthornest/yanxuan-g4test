package com.lynhaw.g4test.mybatis.beans;

import javax.xml.stream.events.EndDocument;
import java.io.Serializable;

/**
 * @Author yyhu3
 * @Date 2018-05-30 17:30
 */
public class SqlInfoBean implements Serializable {
    private int id;
    private String sqlmode;
    private String sqlconninfo;
    private String sqlusername;
    private String sqlpassword;

    public SqlInfoBean()
    {
        super();
    }

    public SqlInfoBean(String sqlmode,String sqlconninfo,String sqlusername,String sqlpassword)
    {
        this.sqlmode = sqlmode;
        this.sqlconninfo = sqlconninfo;
        this.sqlusername = sqlusername;
        this.sqlpassword = sqlpassword;
    }

    @Override
    public String toString()
    {
        return "SqlInfoBean = [id = "+id+",sqlmode = "+sqlmode+",sqlconninfo = "+sqlconninfo+",sqlusername = "+sqlusername+",sqlpassword = "+sqlpassword+"]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSqlmode() {
        return sqlmode;
    }

    public void setSqlmode(String sqlmode) {
        this.sqlmode = sqlmode;
    }

    public String getSqlconninfo() {
        return sqlconninfo;
    }

    public void setSqlconninfo(String sqlconninfo) {
        this.sqlconninfo = sqlconninfo;
    }

    public String getSqlusername() {
        return sqlusername;
    }

    public void setSqlusername(String sqlusername) {
        this.sqlusername = sqlusername;
    }

    public String getSqlpassword() {
        return sqlpassword;
    }

    public void setSqlpassword(String sqlpassword) {
        this.sqlpassword = sqlpassword;
    }
}
