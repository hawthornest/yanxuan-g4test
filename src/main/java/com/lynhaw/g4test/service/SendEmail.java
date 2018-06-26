package com.lynhaw.g4test.service;


import com.lynhaw.g4test.getproperties.GetProperties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;


/**
 * @Author yyhu3
 * @Date 2018-05-07 20:13
 */
@Service
public class SendEmail {
    @Autowired
    GetProperties getProperties;
    Logger logger = Logger.getLogger(SendEmail.class);

    public Message createSimpleMail(Session session) throws Exception {

         String ownEmailAccount = getProperties.emailSender.trim();
        // 发送邮件对方的邮箱
         String receiveMailAccount = getProperties.emailAddressee.trim();
        MimeMessage message = new MimeMessage(session);
        // 设置发送邮件地址,param1 代表发送地址 param2 代表发送的名称(任意的) param3 代表名称编码方式
        message.setFrom(new InternetAddress(ownEmailAccount, "严选自动化测试", "utf-8"));
        if(receiveMailAccount.split(",").length==1)
        {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMailAccount.split(",")[0], receiveMailAccount, "utf-8"));
            logger.info("收件人邮箱为:"+receiveMailAccount.split(",")[0]);
        }
        else {
            for (int tempReceiveMailAccount = 1; tempReceiveMailAccount < receiveMailAccount.split(",").length; tempReceiveMailAccount++) {
                logger.info("收件人邮箱为:"+receiveMailAccount.split(",")[tempReceiveMailAccount]);
                message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMailAccount.split(",")[tempReceiveMailAccount], receiveMailAccount.split(",")[tempReceiveMailAccount], "UTF-8"));
            }
        }
        // 设置邮件主题
        message.setSubject("自动化测试结果");
        // 设置邮件内容
        message.setContent(readToString("responseResult.html"), "text/html;charset=utf-8");
        // 设置发送时间
        message.setSentDate(new Date());
        // 保存上面的编辑内容
        message.saveChanges();
        return message;

    }

    public void sendMail()
    {
        Properties prop = new Properties();
        // 设置邮件传输采用的协议smtp
        prop.setProperty("mail.transport.protocol", "smtp");
        // 设置发送人邮件服务器的smtp地址
        // 这里以网易的邮箱smtp服务器地址为例
        prop.setProperty("mail.smtp.host", getProperties.emailMailHost);
        // 设置验证机制
        prop.setProperty("mail.smtp.auth", "true");
        // 创建对象回话跟服务器交互
        Session session = Session.getInstance(prop);
        // 会话采用debug模式
        session.setDebug(true);
        // 创建邮件对象
        Message message = null;
        try {
            message = createSimpleMail(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Transport trans = null;
        try {
            trans = session.getTransport();
            trans.connect(getProperties.emailSender.trim(), getProperties.emailPassword.trim());
            logger.info("邮件发件人为:"+getProperties.emailSender.trim());
            logger.info("邮件发件人密码为:"+getProperties.emailPassword.trim());
            trans.sendMessage(message, message.getAllRecipients());
            trans.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String readToString(String fileName) throws IOException {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        FileInputStream in = new FileInputStream(file);
        in.read(filecontent);
        in.close();
        return new String(filecontent, encoding);

    }


    public static void main(String[] args) throws Exception {
        SendEmail SendEmail = new SendEmail();
        SendEmail.sendMail();

    }


}
