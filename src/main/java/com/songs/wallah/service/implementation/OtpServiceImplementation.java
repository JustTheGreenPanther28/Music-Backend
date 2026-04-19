package com.songs.wallah.service.implementation;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.songs.wallah.entity.OtpEntity;
import com.songs.wallah.entity.UserEntity;
import com.songs.wallah.enums.otp.OperationStatus;
import com.songs.wallah.enums.otp.OtpVerification;
import com.songs.wallah.repository.OtpRepository;
import com.songs.wallah.repository.UserRepository;
import com.songs.wallah.service.EmailService;
import com.songs.wallah.service.OtpService;

@Service
public class OtpServiceImplementation implements OtpService {

	private EmailService emailService;
	private OtpRepository otpRepository;
	private UserRepository userRepository;

	public OtpServiceImplementation(EmailService emailService, OtpRepository otpRepository,
			UserRepository userRepository) {
		this.emailService = emailService;
		this.otpRepository = otpRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void sendOtp(String email) {
		otpRepository.deleteOldOtps();
		SecureRandom secureRandom = new SecureRandom();
		int otp = secureRandom.nextInt(900000);
		OtpEntity otpEntity = new OtpEntity(email, LocalDateTime.now().plusMinutes(10), otp);
		emailService.sendOTP(email, String.valueOf(otp));
		otpRepository.save(otpEntity);

	}
	
	public OperationStatus resendOtp(String email) {
		UserEntity user = userRepository.findByEmail(email);
		
		if(user==null) {
			throw new UsernameNotFoundException("Email doesn't exist");
		}
		if(user.isEmailVerification()) {
			return OperationStatus.FAIL;
		}
		sendOtp(email);
		return OperationStatus.SUCCESS;
	}

	@Override
	public OtpVerification verifyOtp(String email, String givenOtp) {
		if (email==null||givenOtp==null||email.isEmpty()||givenOtp.isEmpty()) {
			throw new RuntimeException("Incorrect Credentials");
		}
		UserEntity user = userRepository.findByEmail(email);
		if(user==null) {
			return OtpVerification.INVALID_EMAIL;
		}
		List<OtpEntity> otps=otpRepository.findOtpByEmail(email);
		if(otps==null) {
			return OtpVerification.OTP_EXPIRED;
		}
		if(String.valueOf(otps.get(otps.size()-1).getOtp()).equals(givenOtp)) {
			otpRepository.delete(otps.get(otps.size()-1));
			user.setEmailVerification(true);
			userRepository.save(user);
			return OtpVerification.SUCCESS;
		}
		return OtpVerification.INVALID_OTP;
	}

}
