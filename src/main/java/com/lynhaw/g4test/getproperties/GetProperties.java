package com.lynhaw.g4test.getproperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author yyhu3
 * @Date 2018-05-08 20:05
 */
@Configuration
@PropertySource(value = {"classpath:/config/dynamic.properties"})
public class GetProperties {
    @Value("${email.sender}")
    public String emailSender;
    @Value("${email.password}")
    public String emailPassword;
    @Value("${email.mailHost}")
    public String emailMailHost;
    @Value("${email.mailPort}")
    public String emailMailPort;
    @Value("${email.Addressee}")
    public String emailAddressee;

}