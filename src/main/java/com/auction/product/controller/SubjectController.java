package com.auction.product.controller;


import com.auction.product.model.PurchaseDto;
import com.auction.product.model.Subject;
import com.auction.product.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subjects")
public class SubjectController {
    private final SubjectService subjectService;

//    @GetMapping//http://localhost:8080/Subjects// do wyjebania raczej
//    public List<Subject> getSubjects(@RequestParam Boolean archive) {
//        return subjectService.getSubjects(archive);
//    }
//-----------------------nowe zapytania---------------------------------------------------------------
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
//------------------------------------------koniec---------------------------------------------
    @PostMapping
    public Subject addSubject(@RequestParam(name = "imageFile", required = false) MultipartFile file, @RequestParam("subject") String subject) throws IOException {
        return subjectService.addSubject(file, subject);//ok
    }

    @PutMapping
    public Subject putSubject(@RequestParam(name = "imageFile", required = false) MultipartFile file, @RequestParam("subject") String subject, @RequestParam String deleteFile) throws IOException {
        return subjectService.putSubject(file, subject, deleteFile);//raczej do usunięcia, z patch korzystam
    }

//    @PostMapping
//    public Subject addSubject(@RequestBody @Valid SubjectDtoImage subject) throws IOException {
//        return subjectService.addSubject(subject);
//    }

    @GetMapping("/{code}")
    public Subject getSubject(@PathVariable String code) {
        return subjectService.getSubject(code);
    }//ok

    @GetMapping("/bid/{code}")
    public Subject getSubjectForBid(@PathVariable String code) {//raczej do usunięcia
        return subjectService.getSubjectForBid(code);
    }

    @DeleteMapping("/{code}")//ok
    void deleteSubject(@PathVariable String code) {
        subjectService.deleteSubject(code);
    }

//    @PutMapping
//    Subject putSubject(@RequestParam(name = "imageFile", required = false) MultipartFile file, @RequestParam("subject") String subject) throws IOException {
//        return subjectService.putSubject(file, subject);
//    }

//    @PutMapping("/{code}")//stary - do usunięcia
//    Subject putSubject(@PathVariable String code, @RequestBody Subject Subject) {
//        return subjectService.putSubject(code, Subject);
//    }

//    @PatchMapping("/{code}")//stary - do usunięcia
//    Subject patchSubject(@PathVariable String code, @RequestBody Subject Subject) throws Exception {
//        return subjectService.patchSubject(code, Subject);
//    }

    @PatchMapping("/{code}/{price}/{username}")
    @RolesAllowed({"ROLE_USER"})
    Double bidSubject(@PathVariable String code, @PathVariable double price, @PathVariable String username) throws Exception {
        return subjectService.bidSubject(code, price, username);//ok
    }

//    @PostMapping("/buy")
//    public PurchaseDto buySubject(String code, Integer price, String username) {
//        return subjectService.buySubject(code, price, username);
//    }

    @PostMapping("/buy")
    public ResponseEntity<?> buySubject(@RequestBody PurchaseDto purchaseDto) {
        return subjectService.buySubject(purchaseDto);//raczej nie
    }

    @PatchMapping("/testUser")
    @RolesAllowed({"ROLE_USER"})
    Double testUser() {
        return 1234.1;
    }

    @GetMapping("/testAdmin")
    @RolesAllowed({"ROLE_ADMIN"})
    Double testAdmin() {
        return 1234.0;
    }
}
