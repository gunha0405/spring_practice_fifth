package com.example.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.user.model.SiteUser;
import com.example.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailSendJob implements Job{
	
	private final JavaMailSender mailSender;
	private final UserRepository userRepository;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException{
		List<SiteUser> users = userRepository.findAll();
		
		for (SiteUser user : users) {
			if (user.getEmail() == null) continue;
			
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(user.getEmail());
			message.setSubject("[공지] 아침 알림 메일");
			message.setText("좋은 하루 되세요. 지금은 (" + LocalDateTime.now() + ") 입니다.");
			
			mailSender.send(message);
		}
		
	}
	
}
