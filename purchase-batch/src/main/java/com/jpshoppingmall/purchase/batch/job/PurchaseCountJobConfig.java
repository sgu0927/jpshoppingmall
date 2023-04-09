package com.jpshoppingmall.purchase.batch.job;

import com.jpshoppingmall.domain.product.entity.PurchaseCount;
import com.jpshoppingmall.domain.product.entity.PurchaseCountHistory;
import com.jpshoppingmall.domain.product.repository.PurchaseCountHistoryRepository;
import com.jpshoppingmall.domain.product.repository.PurchaseCountRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class PurchaseCountJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final PurchaseCountRepository purchaseCountRepository;
    private final PurchaseCountHistoryRepository purchaseCountHistoryRepository;


    private static final int chunkSize = 100;

    @Bean
    public Job purchaseCountJob() {
        return jobBuilderFactory.get("purchaseCountJob")
            .incrementer(new RunIdIncrementer())
            .start(applyPurchaseCountHistoryStep())
            .next(deletePurchaseCountHistoryStep())
            .build();
    }

    @Bean
    @JobScope
    public Step applyPurchaseCountHistoryStep() {
        return stepBuilderFactory.get("applyPurchaseCountHistoryStep")
            .<PurchaseCountHistory, PurchaseCountHistory>chunk(chunkSize)
            .reader(sumPurchaseCountItemReader(null))
            .writer(sumProductPurchaseCountWriter())
            .build();
    }

    @Bean
    @JobScope
    public Step deletePurchaseCountHistoryStep() {
        return stepBuilderFactory.get("applyPurchaseCountHistoryStep")
            .tasklet((contribution, chunkContext) -> {
                String jobStartDateTime = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("jobStartDateTime");
                LocalDateTime standardTime = LocalDateTime.parse(jobStartDateTime);

                purchaseCountHistoryRepository.deleteAllByBeforeStandardTime(standardTime);

                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<PurchaseCountHistory> sumPurchaseCountItemReader(
        @Value("#{jobParameters['jobStartDateTime']}") String jobStartDateTime
    ) {
        LocalDateTime standardTime = LocalDateTime.parse(jobStartDateTime);
        HashMap<String, Object> params = new HashMap<>();
        params.put("standardTime", standardTime);

        return new JpaPagingItemReaderBuilder<PurchaseCountHistory>()
            .name("sumPurchaseCountItemReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(chunkSize)
            .queryString(
                "SELECT p FROM PurchaseCountHistory p WHERE p.createdDateTime < :standardTime")
            .parameterValues(params)
            .build();
    }

    @StepScope
    @Bean
    public JpaItemWriter<PurchaseCountHistory> sumProductPurchaseCountWriter() {
        JpaItemWriter<PurchaseCountHistory> jpaItemWriter = new JpaItemWriter<PurchaseCountHistory>() {
            @Override
            public void write(List<? extends PurchaseCountHistory> historyList) {
                List<Long> productIdList = new ArrayList<>();
                Map<Long, Integer> productCountMap = new HashMap<>();

                historyList.forEach((productCount) -> {
                    productIdList.add(productCount.getProductId());
                    productCountMap.put(productCount.getProductId(), productCount.getQuantity());
                });

                List<PurchaseCount> purchaseCountList = purchaseCountRepository.findAllByProductIdIn(
                    productIdList);

                purchaseCountList.forEach((purchaseCount) -> {
                    Integer updateCount = productCountMap.get(purchaseCount.getId());
                    purchaseCount.setQuantity(purchaseCount.getQuantity() + updateCount);
                });
            }
        };
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return jpaItemWriter;
    }
}
