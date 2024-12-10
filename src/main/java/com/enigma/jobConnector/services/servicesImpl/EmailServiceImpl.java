package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    @Override
    public void sendEmail(String to, String subject, String url) throws MessagingException {
        templateEngine.addTemplateResolver(new ClassLoaderTemplateResolver() {
            {
                setPrefix("templates/");
                setSuffix(".html");
            }
        });
        String htmlContent = templateEngine.process("email-template", new Context());
        htmlContent = htmlContent.replace("[[URL]]", url);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("darulcrypto@gmail.com");

        mailSender.send(message);
    }
}
