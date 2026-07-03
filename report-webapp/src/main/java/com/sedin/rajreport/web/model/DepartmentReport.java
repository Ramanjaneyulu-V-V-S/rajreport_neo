package com.sedin.rajreport.web.model;

import java.time.LocalDate;
import java.util.List;

public record DepartmentReport(
        String department,
        LocalDate fromDate,
        LocalDate toDate,
        HindiReplyStatus hindiReplyStatus,
        List<EnglishReplyStatus> englishReplyStatuses,
        List<RegionTierResult> outwardLetters) {
}
