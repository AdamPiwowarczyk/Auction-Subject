package com.auction.product.service;


import com.auction.product.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SubjectService {
//    List<Subject> getSubjects(Boolean archive);

    List<Subject> getSubjectsByCategory(Category category);

    Subject getSubject(String code);

//    Subject addSubject(Subject Subject);
    Subject addSubject(MultipartFile file, String subject) throws IOException;

    Subject putSubject(MultipartFile file, String subject, String deleteFile) throws IOException;

    Subject patchSubject(MultipartFile file, String subject) throws IOException;//kurwa put miało być, do usunięcia

    void deleteSubject(String code);

//    Subject putSubject(String code, Subject subject);

//    Subject patchSubject(String code, Subject subject); //stare - do usunięcia

    ResponseEntity<?> buySubject(PurchaseDto purchaseDto);

    List<Subject> getSubjectsByCodes(List<String> codes);

    Double bidSubject(String code, double price, String username);

    Double getIncrementalAmount(Double price);

    SubjectDto getSubjectForBid(String code);//do usunięcia

    List<Subject> getActiveSubjects();

    List<Subject> getNewSubjects();

    List<Subject> getBiddedSubjects();

    List<Subject> getBoughtSubjects();

    List<Subject> getArchiveSubjects();
}
