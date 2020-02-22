package com.eva.blog.backend.model.services;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SmtpMailSender {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public void send(String to, String subject, String body) throws MessagingException {
		
		MimeMessage message=javaMailSender.createMimeMessage();
		MimeMessageHelper helper;

			helper= new MimeMessageHelper(message,true);
			helper.setFrom(new InternetAddress("ecointex@ecointex.org"));               

		helper.setSubject(subject);
		helper.setTo("ecointex@ecointex.org");
		helper.setText(body,true);

		javaMailSender.send(message);
	}
	
	
	
	
}
