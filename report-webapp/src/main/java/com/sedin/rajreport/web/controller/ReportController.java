package com.sedin.rajreport.web.controller;

import com.sedin.rajreport.web.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/")
    public String report(
            @RequestParam(name = "department", required = false) String department,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model) {

        var departments = reportService.listDepartments();
        String selectedDepartment = department != null ? department : departments.get(0);
        LocalDate selectedFrom = fromDate != null ? fromDate : LocalDate.of(2025, 4, 1);
        LocalDate selectedTo = toDate != null ? toDate : LocalDate.of(2026, 6, 30);

        model.addAttribute("departments", departments);
        model.addAttribute("report",
                reportService.getOutwardLetterReport(selectedDepartment, selectedFrom, selectedTo));
        return "report";
    }
}
