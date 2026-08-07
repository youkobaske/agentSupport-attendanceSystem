package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AS002Controller {

    /**
     * ホーム画面表示
     *
     * @param year  年
     * @param month 月
     * @param model Model
     * @return ホーム画面
     */
    @GetMapping("/AS002")
    public String home(@RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,Model model) {

        // 現在日付取得
        LocalDate now = LocalDate.now();

        YearMonth targetMonth;

        // 初回表示は現在年月
        if (year == null || month == null) {
            targetMonth = YearMonth.now();
        } else {
            targetMonth = YearMonth.of(year, month);
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy年M月");

        model.addAttribute("currentMonth",
                targetMonth.format(formatter));

        model.addAttribute("year",
                targetMonth.getYear());

        model.addAttribute("month",
                targetMonth.getMonthValue());

        // 前月
        YearMonth prev = targetMonth.minusMonths(1);

        model.addAttribute("prevYear",
                prev.getYear());

        model.addAttribute("prevMonth",
                prev.getMonthValue());

        // 翌月
        YearMonth next = targetMonth.plusMonths(1);

        model.addAttribute("nextYear",
                next.getYear());

        model.addAttribute("nextMonth",
                next.getMonthValue());

        return "AS002";

    }

}