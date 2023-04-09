package com.jpshoppingmall.mail.service;

import com.jpshoppingmall.domain.payment.dto.PaymentSuccessDto;
import com.jpshoppingmall.mail.util.CertCharacterGenerator;
import com.jpshoppingmall.mail.util.RedisUtil;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@AllArgsConstructor
public class MailService {

    private JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final RedisUtil redisUtil;
    private static final String FROM_ADDRESS = "tsi04076@naver.com";

    public void sendVerificationMail(String toEmail) {
        String verificationCode = CertCharacterGenerator.generate();
        Context context = getVerificationMailContext(verificationCode);
        String message = templateEngine.process("mail/email-verification", context);

        try {
            redisUtil.setDataExpire(verificationCode, toEmail, 1800000); // 3분

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false,
                "UTF-8");
            mimeMessageHelper.setFrom(MailService.FROM_ADDRESS);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject("[JP Shoppingmall] 회원가입 인증번호 입니다.");
            mimeMessageHelper.setText(message,true);
            mailSender.send(mimeMessage);

            log.info("[MailService] 메일 전송에 성공했습니다. - email :: {}", toEmail);
        } catch (Exception e) {
            log.error("[MailService] 메일 전송에 실패했습니다. - email  :: {}", toEmail, e);
        }
    }

    public void sendPaymentSuccessMail(PaymentSuccessDto paymentSuccessDto) {
        Context context = getPaymentMailContext(paymentSuccessDto);
        String message = templateEngine.process("mail/payment", context);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.addRecipients(RecipientType.TO, paymentSuccessDto.memberEmail());
            mimeMessage.setFrom(MailService.FROM_ADDRESS);
            mimeMessage.setSubject("[JP Shoppingmall] 결재 내역을 보내드립니다.");
            mimeMessage.setText(message, "utf-8","html");

            mailSender.send(mimeMessage);
            log.info("[MailService] 메일 전송에 성공했습니다. - email :: {}", paymentSuccessDto.memberEmail());
        } catch (Exception e) {
            log.error("[MailService] 메일 전송에 실패했습니다. - email  :: {}",
                paymentSuccessDto.memberEmail(), e);
        }
    }

    private Context getVerificationMailContext(String verificationCode) {
        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);

        return context;
    }

    private Context getPaymentMailContext(PaymentSuccessDto paymentSuccessDto) {
        Context context = new Context();
        context.setVariable("memberName", paymentSuccessDto.memberName());
        context.setVariable("displayName", paymentSuccessDto.displayName());
        context.setVariable("totalPrice", paymentSuccessDto.totalPrice());
        context.setVariable("totalDiscountPrice", paymentSuccessDto.totalDiscountPrice());
        context.setVariable("roadNameAddress", paymentSuccessDto.roadNameAddress());
        context.setVariable("detailAddress", paymentSuccessDto.detailAddress());
        context.setVariable("receiverPhone", paymentSuccessDto.receiverPhone());
        context.setVariable("deliveryMessage", paymentSuccessDto.deliveryMessage());

        return context;
    }
}
