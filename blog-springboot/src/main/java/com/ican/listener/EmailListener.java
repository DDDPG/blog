package com.ican.listener;

import com.alibaba.fastjson2.JSON;
import com.ican.constant.MqConstant;
import com.ican.model.dto.MailDTO;
import com.ican.utils.EmailUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ican.constant.CommonConstant.*;

/**
 * 邮件监听
 *
 * @author ican
 */
@Component
public class EmailListener {

    @Autowired
    private EmailUtils emailUtils;

    @RabbitListener(queues = MqConstant.EMAIL_QUEUE)
    public void listenSendEmail(byte[] mail) {
        MailDTO mailDTO = JSON.parseObject(new String(mail), MailDTO.class);
        if (CAPTCHA.equals(mailDTO.getSubject()) || CHECK_REMIND.equals(mailDTO.getSubject())) {
            emailUtils.sendSimpleMail(mailDTO);
        }
        if(COMMENT_REMIND.equals(mailDTO.getSubject())){
            emailUtils.sendHtmlMail(mailDTO);
        }
    }
}
