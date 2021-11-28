package com.auction.product.tasks;

import com.auction.product.model.Subject;
import com.auction.product.repository.SubjectRepository;
import com.auction.product.service.PurchaseServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class TaskScheduler {
    private final SubjectRepository subjectRepository;
    private final PurchaseServiceClient purchaseServiceClient;

    @Scheduled(cron = "1 * * * * *")
    public ResponseEntity<?> archiveAndBuySubjects() {
        List<Subject> subjects = subjectRepository.findAllWithEndDateExceeded();
        if (subjects.isEmpty()) {
            return ResponseEntity.ok(ResponseEntity.status(HttpStatus.OK));
        }
        subjects.forEach(Subject::setArchive);
        subjectRepository.saveAll(subjects);

        List<String> boughtSubjects = subjects.stream()
                .filter(subject -> subject.getSoldPrice() != null)
                .map(Subject::getCode)
                .collect(Collectors.toList());

        return purchaseServiceClient.buySubjects(boughtSubjects);
    }
}
