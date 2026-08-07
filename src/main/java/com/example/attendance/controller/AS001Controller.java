package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AS001Controller {
    /**
     * ログイン画面表示
     *
     * @return ログイン画面
     */
    @GetMapping("/AS001")
    public String login() {

        return "AS001";

    }

}