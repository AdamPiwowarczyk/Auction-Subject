package com.auction.product.controller;


import com.auction.product.model.Subject;
import com.auction.product.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/active")
    public List<Subject> getActiveSubjects() {
        return subjectService.getActiveSubjects();
    }

    @GetMapping("/new")
    public List<Subject> getNewSubjects() {
        return subjectService.getNewSubjects();
    }

    @GetMapping("/bidded")
    public List<Subject> getBiddedSubjects() {
        return subjectService.getBiddedSubjects();
    }

    @GetMapping("/bought")
    public List<Subject> getBoughtSubjects() {
        return subjectService.getBoughtSubjects();
    }

    @GetMapping("/archive")
    public List<Subject> getArchiveSubjects() {
        return subjectService.getArchiveSubjects();
    }

    @PostMapping("/codes")
    List<Subject> getSubjectsByCodes(@RequestBody List<String> codes) {
        return subjectService.getSubjectsByCodes(codes);
    }

    @PostMapping
    public Subject addSubject(@RequestParam(name = "imageFile", required = false) MultipartFile file,
                              @RequestParam("subject") String subject) throws IOException {
        return subjectService.addSubject(file, subject);
    }

    @GetMapping("/{code}")
    public Subject getSubject(@PathVariable String code) {
        return subjectService.getSubject(code);
    }

    @PutMapping
    public Subject putSubject(@RequestParam(name = "imageFile", required = false) MultipartFile file,
                              @RequestParam("subject") String subject,
                              @RequestParam String deleteFile) throws IOException {
        return subjectService.putSubject(file, subject, deleteFile);
    }

    @DeleteMapping("/{code}")
    void deleteSubject(@PathVariable String code) {
        subjectService.deleteSubject(code);
    }

    @PatchMapping("/{code}/{price}/{username}")
    Double bidSubject(@PathVariable String code, @PathVariable double price, @PathVariable String username) {
        return subjectService.bidSubject(code, price, username);
    }
}
