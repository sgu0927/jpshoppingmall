package com.jpshoppingmall.purchase.batch.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/batch/purchase")
public class JobLauncherController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @PostMapping("/job-launcher")
    public String launcher(@RequestBody JobLaunchRequest request)
        throws Exception {

        log.info("jobName : {}", request.getName());
        final Job job = jobRegistry.getJob(request.getName());
        final JobExecution jobExecution = jobLauncher
            .run(job, request.getJobParameters());

        return "SUCCESS";
    }

    @Getter
    public static class JobLaunchRequest {

        private String name;
        private Map<String, String> jobParameters;

        public void setName(String name) {
            this.name = name;
        }

        public void setJobParameters(Map<String, String> jobParameters) {
            this.jobParameters = jobParameters;
        }

        public JobParameters getJobParameters() {
            final JobParametersBuilder builder = new JobParametersBuilder();

            if(jobParameters != null) {
                for (Entry<String, String> entry : jobParameters.entrySet()) {
                    builder.addString(entry.getKey(), entry.getValue());
                }
            }

            // force run
            builder.addString("jobStartDateTime", LocalDateTime.now().toString());
            return builder.toJobParameters();
        }
    }
}
