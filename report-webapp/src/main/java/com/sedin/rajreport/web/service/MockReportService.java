package com.sedin.rajreport.web.service;

import com.sedin.rajreport.web.model.DepartmentReport;
import com.sedin.rajreport.web.model.EnglishReplyStatus;
import com.sedin.rajreport.web.model.HindiReplyStatus;
import com.sedin.rajreport.web.model.RegionTierResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Stand-in for a Documentum-backed ReportService. HRMD is seeded with the figures from a
 * real department report (HRMD.xlsx); other departments get deterministic placeholder
 * figures so the UI can be exercised without a live docbase connection.
 */
@Service
public class MockReportService implements ReportService {

    private static final List<String> DEPARTMENTS = List.of("HRMD", "DMFI", "LAW", "RAJ", "GSD");

    private static final Map<String, HindiReplyStatus> HINDI_REPLY_FIXTURES = Map.of(
            "HRMD", new HindiReplyStatus(439, 0, 0)
    );

    // department -> tier -> {receivedEnglish, repliedHindi, repliedEnglish} for regions A, B
    private static final Map<String, int[][]> ENGLISH_REPLY_FIXTURES = Map.of(
            "HRMD", new int[][]{{15, 0, 0}, {77, 0, 0}}
    );

    // department -> tier -> {hindiOrBilingual, english} for regions A, B, C
    private static final Map<String, int[][]> OUTWARD_LETTER_FIXTURES = Map.of(
            "HRMD", new int[][]{{132, 31}, {560, 70}, {169, 44}}
    );

    @Override
    public List<String> listDepartments() {
        return DEPARTMENTS;
    }

    @Override
    public DepartmentReport getOutwardLetterReport(String department, LocalDate fromDate, LocalDate toDate) {
        Random random = new Random(department.hashCode());

        HindiReplyStatus hindiReplyStatus = HINDI_REPLY_FIXTURES.getOrDefault(department,
                new HindiReplyStatus(random.nextInt(500), random.nextInt(50), random.nextInt(10)));

        int[][] englishCounts = ENGLISH_REPLY_FIXTURES.getOrDefault(department,
                new int[][]{{random.nextInt(100), random.nextInt(10), random.nextInt(10)},
                        {random.nextInt(100), random.nextInt(10), random.nextInt(10)}});
        List<EnglishReplyStatus> englishReplyStatuses = List.of(
                new EnglishReplyStatus("A", englishCounts[0][0], englishCounts[0][1], englishCounts[0][2]),
                new EnglishReplyStatus("B", englishCounts[1][0], englishCounts[1][1], englishCounts[1][2]));

        int[][] outwardCounts = OUTWARD_LETTER_FIXTURES.getOrDefault(department,
                new int[][]{{random.nextInt(300), random.nextInt(30)},
                        {random.nextInt(300), random.nextInt(30)},
                        {random.nextInt(300), random.nextInt(30)}});
        List<RegionTierResult> outwardLetters = List.of(
                new RegionTierResult("A", outwardCounts[0][0], outwardCounts[0][1]),
                new RegionTierResult("B", outwardCounts[1][0], outwardCounts[1][1]),
                new RegionTierResult("C", outwardCounts[2][0], outwardCounts[2][1]));

        return new DepartmentReport(department, fromDate, toDate, hindiReplyStatus, englishReplyStatuses, outwardLetters);
    }
}
