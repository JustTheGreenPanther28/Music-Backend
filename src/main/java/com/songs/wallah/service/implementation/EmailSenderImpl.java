package com.songs.wallah.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.songs.wallah.service.EmailService;

@Service
public class EmailSenderImpl implements EmailService {
	private final SendGrid sendGrid;
	private final String apiKey;

	public EmailSenderImpl(@Value("${SENDGRID_API_KEY:}") String apiKey) {
		this.apiKey=apiKey;
		this.sendGrid = new SendGrid(apiKey);
	}

	public void sendOTP(String toEmail, String otp) {
		Email from = new Email("zippyjese@gmail.com");
		Email to = new Email(toEmail);
		String subject = "Verification Email";
		Content content = new Content("text/plain", "Verification code: " + otp);

		Mail mail = new Mail(from, subject, to, content);

		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			Response response = sendGrid.api(request);
			
			if (response.getStatusCode() != 202) {
				throw new RuntimeException("Email not accepted by SendGrid");
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to send email", e);
		}
	}

	@Override
	public void sendEmail(String to, String body, String Subject) {
		// TODO Auto-generated method stub

	}
}
