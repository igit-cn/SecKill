package com.proxy.action;

import com.proxy.utils.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api")
public class TestAction  {
    
    @RequestMapping("com/test")
    public void test(){
        System.out.println(RandomStringUtils.randomString("kx123456",10));
    }
}
