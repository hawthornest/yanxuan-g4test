package com.lynhaw.g4test.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Author yyhu3
 * @Date 2018-06-07 20:43
 */

@Service
public class PublicMethod {
    Logger logger = Logger.getLogger(PublicMethod.class);
    public void OperaException(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        logger.error(sw.toString());
    }
}
