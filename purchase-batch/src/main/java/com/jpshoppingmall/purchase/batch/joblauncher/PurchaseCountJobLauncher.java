package com.jpshoppingmall.purchase.batch.joblauncher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class PurchaseCountJobLauncher {

    private final JobLauncher jobLauncher;
    private final Job job;

    @Scheduled(cron = "0 0/10 * * * *")
    public void productPurchaseCountJobLauncher() throws Exception {
//        log.info("Batch : 상품 구매 수량 카운트 Job 실행");
//
//        LocalDateTime now = LocalDateTime.now();
//        String requestDateTimeStr
//            = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
//                now.getHour(), now.getMinute(), 0)
//            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//
//        JobParameters jobParameters = new JobParametersBuilder().addString("jobStartDateTime",
//            requestDateTimeStr).toJobParameters();
//
//        jobLauncher.run(job, jobParameters);
    }
}
