package com.lynhaw.g4test.response;

/**
 * @Author yyhu3
 * @Date 2018-05-08 19:19
 */
public class LogInResponse {
    private String YX_CSRF_TOKEN;
    private String YX_OPENID_SESS;
    private String csrftoken;
    private String yx_name;
    private String yx_username;

    public String getYX_CSRF_TOKEN() {
        return YX_CSRF_TOKEN;
    }

    public void setYX_CSRF_TOKEN(String YX_CSRF_TOKEN) {
        this.YX_CSRF_TOKEN = YX_CSRF_TOKEN;
    }

    public String getYX_OPENID_SESS() {
        return YX_OPENID_SESS;
    }

    public void setYX_OPENID_SESS(String YX_OPENID_SESS) {
        this.YX_OPENID_SESS = YX_OPENID_SESS;
    }

    public String getCsrftoken() {
        return csrftoken;
    }

    public void setCsrftoken(String csrftoken) {
        this.csrftoken = csrftoken;
    }

    public String getYx_name() {
        return yx_name;
    }

    public void setYx_name(String yx_name) {
        this.yx_name = yx_name;
    }

    public String getYx_username() {
        return yx_username;
    }

    public void setYx_username(String yx_username) {
        this.yx_username = yx_username;
    }
}
