package com.zq.backend.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 
 * @ClassName: EmailHelper 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:45:46
 */
public class EmailHelper {
    public static synchronized void sendMail(
            String to_email,
            String content,
            String from_email,
            String from_name,
            String from_email_pwd,
            String server,
            int port,
            String title)
    {
        try {
            Properties props = new Properties();
            //普通客户端
            props.setProperty("mail.smtp.auth", "true");
            //选择协议
            props.setProperty("mail.transport.protocol", "smtp");
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
            //
            Session session = Session.getDefaultInstance(props);
            // 设置debug模式 在控制台看到交互信息
            //session.setDebug(true);
            // 建立一个要发送的信息(邮件的标题，内容等)
            Message msg = new MimeMessage(session);
            // 设置简单的发送内容
            msg.setContent(content, "text/html; charset=utf-8");
            // msg.setText(content);

            //设置自定义发件人昵称
            String nick="";
            try {
                nick=javax.mail.internet.MimeUtility.encodeText(from_name);
            } catch (UnsupportedEncodingException e) {
                // e.printStackTrace();
                nick = "Birdex";
            }
            msg.setFrom(new InternetAddress(nick+" <"+ from_email +">"));

            //邮件标题
            msg.setSubject(title);

            // 发送信息的工具
            Transport transport = session.getTransport();
            // 发件人邮箱号// 和密码
            transport.connect(server, port, from_email, from_email_pwd);
            // 对方的地址
            transport.sendMessage(msg, new Address[]{new InternetAddress(to_email)});
            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
