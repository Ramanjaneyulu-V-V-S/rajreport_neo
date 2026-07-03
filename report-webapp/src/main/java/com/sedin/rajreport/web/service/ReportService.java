package com.sedin.rajreport.web.service;

import com.sedin.rajreport.web.model.DepartmentReport;

import java.time.LocalDate;
import java.util.List;

/**
 * Source of the outward-letter language report. {@link MockReportService} is the only
 * implementation today; a Documentum-backed implementation (reusing the DQL in
 * rajreport.java's Aregion/Bregion/Cregion) can be added later behind this same interface.
 */
public interface ReportService {

    List<String> listDepartments();

    DepartmentReport getOutwardLetterReport(String department, LocalDate fromDate, LocalDate toDate);
}
