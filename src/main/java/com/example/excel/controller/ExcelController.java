package com.example.excel.controller;

import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.excel.router.ExcelImportRouter;
import com.example.user.model.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {
	
	private final ExcelImportRouter router;
	private final JobLauncher jobLauncher;
    private final Job questionAExcelImportJob;
	
	@GetMapping("/import")
    public String excelUploadForm() {
        return "excel_import";
    }

    @PostMapping("/import")
    public String importExcel(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        String customerId = userDetail.getCustomerId();
        try {
            router.resolve(customerId).importExcel(file);
            model.addAttribute("message", "Excel import 성공 (tenant=" + customerId + ")");
        } catch (Exception e) {
            model.addAttribute("message", "실패: " + e.getMessage());
        }

        return "excel_import"; 
        
    }
    
    @PostMapping("/batch/import/questionA")
    public String importQuestionA(@RequestParam("file") MultipartFile file, Model model) {
        try {
            
            String tempFilePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
            file.transferTo(new File(tempFilePath));

            JobParameters params = new JobParametersBuilder()
                    .addString("filePath", tempFilePath)   // Reader에 전달
                    .addLong("timestamp", System.currentTimeMillis()) // 중복 실행 방지
                    .toJobParameters();

            jobLauncher.run(questionAExcelImportJob, params);
            model.addAttribute("message", "Excel Batch Import 시작됨!");
        } catch (Exception e) {
            model.addAttribute("message", "실패: " + e.getMessage());
        }
        return "excel_import";
    }


	
}
