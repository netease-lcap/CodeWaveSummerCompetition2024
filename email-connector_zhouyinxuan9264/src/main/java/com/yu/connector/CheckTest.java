package com.yu.connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/23 15:21
 **/
@SpringBootApplication
@RestController
public class CheckTest {
    public static void main(String[] args) {
        SpringApplication.run(CheckTest.class, args);
    }
    @Resource
    private EmailConnectorUtil emailUtil;
    @GetMapping("/test")
    public List<MailBody> getEmail(){
        return emailUtil.getEmail(5);
    }
}
