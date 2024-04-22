/*
package com.wgx.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {


    //GET请求
    @GetMapping("/greetGet")
    public String greetGet(@RequestParam(name = "name", defaultValue = "name") String name,@RequestParam(name = "password", defaultValue = "password") String password) {
        return name+password;
    }


    //POST请求
    @PostMapping("/greetPost")
    @ResponseBody
    public String greetPost(@RequestParam("name") String name,
                        @RequestParam("password") String password) {
        // 这里可以添加业务逻辑处理，比如验证密码等
        // ...

        // 假设业务逻辑处理完成后，我们返回一个问候语和密码
        // 实际上，出于安全考虑，不应该在响应中返回密码
        return "Hello, " + name + ", your password is: " + password;
    }

    //异常请求
    @GetMapping("/greetError")
    public String greetError(@RequestParam(name = "name", defaultValue = "World") String name) {
        int a = 1 / 0 ;
        return "Hello, " + name + "!";
    }

}
*/
