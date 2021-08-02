package com.auction.product.tasks;

import com.auction.product.model.Subject;
import com.auction.product.repository.SubjectRepository;
import com.auction.product.service.HistoryServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class TaskScheduler {

    private final SubjectRepository subjectRepository;
    private final HistoryServiceClient historyServiceClient;

//        @Scheduled(fixedRate = 5000)
    @Scheduled(cron = "5 * * * * *")
    public ResponseEntity<?> archiveAndBuySubjects() {
//    public void archiveAndBuySubjects() {
        System.out.println("cron test " + LocalDateTime.now());
        List<Subject> subjects = subjectRepository.findAllWithEndDateExceeded();
        if (subjects.isEmpty()) {
//            return ResponseEntity.ok(ResponseEntity.status(HttpStatus.OK));
//            return;
        }
        subjects.forEach(Subject::setArchive);
        subjectRepository.saveAll(subjects);

        List<String> boughtSubjects = subjects.stream()
                .filter(subject -> subject.getSoldPrice() != null)
                .map(subject -> subject.getCode())
                .collect(Collectors.toList());
        System.out.println(boughtSubjects);

        //--------------------------------autoryzacja----------------------------------------------
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
//        String token = String.format("Bearer %s", details.getTokenValue());
        //------------------------------------------------------------------------------

        return historyServiceClient.buySubjects(boughtSubjects);
    }
}
