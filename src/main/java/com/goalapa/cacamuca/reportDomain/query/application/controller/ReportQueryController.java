package com.goalapa.cacamuca.reportDomain.query.application.controller;

import com.goalapa.cacamuca.reportDomain.query.application.dto.ReportQueryDTO;
import com.goalapa.cacamuca.reportDomain.query.application.service.ReportPageServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin-page")
public class ReportQueryController {

    private final ReportPageServiceImpl reportPageServiceImpl;

    public ReportQueryController(ReportPageServiceImpl reportPageServiceImpl) {
        this.reportPageServiceImpl = reportPageServiceImpl;
    }

    @GetMapping("/report")
    public String getReportPage () {
        return "reportlist";
    }

    // 신고 리스트 조회
    @GetMapping("/report-list")
    @ResponseBody
    public ResponseEntity<Page<ReportQueryDTO>> getReportListPage(@PageableDefault(size = 2, sort="report_no", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReportQueryDTO> reportPage = reportPageServiceImpl.getReportPage(pageable);

        return ResponseEntity.ok(reportPage);
    }
}
