package com.fc.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

/**
 * @ClassName MailController
 * @Description
 * @Author fc
 * @Date 2021/12/15 2:17 下午
 * @Version 1.0
 **/
@RestController
@RequestMapping("mail")
public class MailController {

   /* @Autowired
    private JavaMailSenderImpl mailSender;

    public void sendMail() throws MessagingException {
        //简单邮件
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("admin@163.com");
        simpleMailMessage.setTo("socks@qq.com");
        simpleMailMessage.setSubject("Happy New Year");
        simpleMailMessage.setText("新年快乐！");
        mailSender.send(simpleMailMessage);


    }*/
}
