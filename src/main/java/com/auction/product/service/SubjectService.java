package com.auction.product.service;


import com.auction.product.model.Category;
import com.auction.product.model.Subject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SubjectService {

    List<Subject> getSubjectsByCategory(Category category);

    Subject getSubject(String code);

    Subject addSubject(MultipartFile file, String subject) throws IOException;

    Subject putSubject(MultipartFile file, String subject, String deleteFile) throws IOException;

    void deleteSubject(String code);

    List<Subject> getSubjectsByCodes(List<String> codes);

    Double bidSubject(String code, double price, String username);

    Double getIncrementalAmount(Double price);

    List<Subject> getActiveSubjects();

    List<Subject> getNewSubjects();

    List<Subject> getBiddedSubjects();

    List<Subject> getBoughtSubjects();

    List<Subject> getArchiveSubjects();
}
