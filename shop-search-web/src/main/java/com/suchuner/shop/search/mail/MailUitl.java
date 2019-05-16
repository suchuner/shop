package com.suchuner.shop.search.mail;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailUitl {
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendMail(String subject, String text, String from, String to){
		SimpleMailMessage ssm = new SimpleMailMessage();
		ssm.setSubject(subject);
		ssm.setText(text);
		ssm.setSentDate(new Date());
		ssm.setFrom(from);
		ssm.setTo(to);
		mailSender.send(ssm);
	}
}
