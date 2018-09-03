package com.proxy.service;

import com.proxy.dao.EmailDaoMapper;
import com.proxy.dao.ProxyDaoMapper;
import com.proxy.dao.ProxyUserDaoMapper;
import com.proxy.entity.EmailEntity;
import com.proxy.entity.UserEntity;
import com.proxy.utils.MailUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class EmailService {

    @Resource private EmailDaoMapper emailDaoMapper;
    @Resource private ProxyDaoMapper proxyDaoMapper;
    @Resource private ProxyUserDaoMapper proxyUserDaoMapper;

    /**
     * 发送一个邮箱验证码
     */
    public void sendEmailCode(String email) throws IOException, MessagingException {
        int randomCode = new Random().nextInt(9999);
        MailUtils.senMail(email, String.format("%04d", randomCode));
        emailDaoMapper.saveEmailCode(email, randomCode, new Date().getTime());
    }
    
    /**
     * 通过邮箱和验证码修改密码
     */
    public EmailEntity emailAlterPasswordForUser(UserEntity userEntity, String emailCode, String newPassword) throws ParseException {
        List<EmailEntity> todayEmail = emailDaoMapper.getInTwoHoursEmail(userEntity.email,new Date().getTime() - 1000L * 60 * 60 * 2);
        if (todayEmail == null || todayEmail.size() == 0) {
            return null;
        }
        
        for (EmailEntity emailItem : todayEmail) {
            if (emailItem.random_code == Integer.parseInt(emailCode)) {//验证码不一样算验证失败
                //走到这里说明验证通过,修改密码
                proxyUserDaoMapper.alterUserPassword(userEntity.user_name, newPassword);
                return emailItem;
            }
        }
        return null;
    }
}
