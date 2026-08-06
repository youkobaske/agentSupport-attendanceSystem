package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    /**
     * ログイン画面表示
     *
     * @return ログイン画面
     */
    @GetMapping("/login")
    public String login() {

        return "login";

    }

}