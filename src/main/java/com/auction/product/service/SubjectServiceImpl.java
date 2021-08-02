package com.auction.product.service;


import com.auction.product.exception.SubjectError;
import com.auction.product.exception.SubjectException;
import com.auction.product.model.Category;
import com.auction.product.model.PurchaseDto;
import com.auction.product.model.Subject;
import com.auction.product.model.SubjectDto;
import com.auction.product.repository.SubjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final HistoryServiceClient historyServiceClient;

//    @Override
//    public List<Subject> getSubjects(Boolean archive) {
//        if (archive == null) {
//            return subjectRepository.findAll();
//        } else if (archive) {
//            return subjectRepository.findAllBySoldDateIsNotNull();
//        } else {
//            return subjectRepository.findAllBySoldDateIsNull();
//        }
//    }

//    @Override //do wyjebania raczej
//    public List<Subject> getSubjects(Boolean archive) {
//        List<Subject> subjects;
//        if (archive == null) {
//            subjects = subjectRepository.findAll();
//        } else if (archive) {
//            subjects = subjectRepository.findAllByEndDateIsNotNull();
//        } else {
//            subjects = subjectRepository.findAllByEndDateIsNull();
//        }
//
//        return subjects;
//        return subjects.stream()
//                .map(subject -> {
//                    byte[] picByte = subject.getPicByte();
//                    subject.setPicByte(decompressBytes(picByte));
//                    return subject;
//                })
//                .collect(Collectors.toList());
//        return subjectRepository.findAllByArchive(archive);
//    }

    @Override
    public List<Subject> getSubjectsByCategory(Category category) {
        return subjectRepository.findAllByCategories(category);
    }

    @Override
    public Subject getSubject(String code) {
        return subjectRepository.findById(code)
                .orElseThrow(() -> new SubjectException(SubjectError.SUBJECT_NOT_FOUND));
    }

//    @Override
//    public Subject addSubject(SubjectDtoImage subject) throws IOException {
//        if (subjectRepository.existsByCode(subject.getCode())) {
//            throw new SubjectException(SubjectError.SUBJECT_ALREADY_EXISTS);
//        }
//
//        return subjectRepository.save(subject);
//    }

    @Override
    public Subject addSubject(MultipartFile file, String subjectStr) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Subject subject = mapper.readValue(subjectStr, Subject.class);

        if (subjectRepository.existsByCode(subject.getCode())) {
            throw new SubjectException(SubjectError.SUBJECT_ALREADY_EXISTS);
        }

//        byte[] imageBytes = compressBytes(file.getBytes());
//        subject.setPicByte(imageBytes);
        if (file != null) {
            subject.setPicByte(file.getBytes());
        }

//        if (Boolean.parseBoolean(publish)) {
//            subject.setPublishedDate(new Date());
//        }
        if (subject.getEndDate() != null) {
            subject.setPublishDate(new Date());
        }

        return subjectRepository.save(subject);
    }

    @Override
    public Subject patchSubject(MultipartFile file, String subjectStr) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Subject subject = mapper.readValue(subjectStr, Subject.class);

        return subjectRepository.findById(subject.getCode())
                .map(SubjectFromDb -> {
                    try {
                        SubjectFromDb.patch(subject, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return subjectRepository.save(SubjectFromDb);
                }).orElseThrow(() -> new SubjectException(SubjectError.SUBJECT_NOT_FOUND));
    }

    @Override
    public void deleteSubject(String code) {
        Subject subject = subjectRepository.findById(code)
                .orElseThrow(() -> new SubjectException(SubjectError.SUBJECT_NOT_FOUND));
        subjectRepository.delete(subject);
    }

    @Override
    public Subject putSubject(MultipartFile file, String subjectStr, String deleteFile) throws IOException {
//        return subjectRepository.findById(code)
//                .map(SubjectFromDb -> {
//                    SubjectFromDb.put(subject);
//                    return subjectRepository.save(SubjectFromDb);
//                }).orElseThrow(() -> new SubjectException(SubjectError.SUBJECT_NOT_FOUND));
        ObjectMapper mapper = new ObjectMapper();
        Subject subject = mapper.readValue(subjectStr, Subject.class);

        return subjectRepository.findById(subject.getCode())
                .map(SubjectFromDb -> {
                    try {
                        SubjectFromDb.put(subject, file, Boolean.parseBoolean(deleteFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return subjectRepository.save(SubjectFromDb);
                }).orElseThrow(() -> new SubjectException(SubjectError.SUBJECT_NOT_FOUND));
    }

    //stare - do usunięcia
//    @Override
//    public Subject patchSubject(String code, Subject subject) {
//        return subjectRepository.findById(code)
//                .map(SubjectFromDb -> {
//                    SubjectFromDb.patch(subject);
//                    return subjectRepository.save(SubjectFromDb);
//                }).orElseThrow(() -> new SubjectException(SubjectError.SUBJECT_NOT_FOUND));
//    }

    @Override
    public ResponseEntity<?> buySubject(PurchaseDto purchaseDto) {
//        Subject subject = new Subject();
//        subject.setSoldDate(new Date());
//        subject.setSoldPrice(purchaseDto.getPrice());
//        patchSubject(purchaseDto.getCode(), subject);

        Subject subject = subjectRepository.findById(purchaseDto.getCode())
                .orElseThrow(() -> new SubjectException(SubjectError.SUBJECT_NOT_FOUND));
        subject.setEndDate(new Date());
        subject.setSoldPrice(purchaseDto.getPrice());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String token = String.format("Bearer %s", details.getTokenValue());

        return historyServiceClient.addPurchase(token, purchaseDto);
    }

    @Override
    public List<Subject> getSubjectsByCodes(List<String> codes) {
        return subjectRepository.findAllByCodeIn(codes);
    }

    @Override
    public Double bidSubject(String code, double price, String username) {
        Subject subject = getSubject(code);

        if (subject.getEndDate().before(new Date())) {
            throw new SubjectException(SubjectError.BIDDING_TIME_HAS_EXPIRED);
        }

        Double soldPrice = subject.getSoldPrice();
        double currentPrice = soldPrice != null ? soldPrice : subject.getBasicPrice();
        double minPrice = currentPrice + getIncrementalAmount(currentPrice);

        if (minPrice > price) {
            throw new SubjectException(SubjectError.PRICE_TOO_LOW);
        }

        subject.setSoldPrice(price);
        subjectRepository.save(subject);
        //--------------------------------nowe----------------------------------------------
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String token = String.format("Bearer %s", details.getTokenValue());

        PurchaseDto purchaseDto = new PurchaseDto(code, price, username);
        historyServiceClient.addPurchase(token, purchaseDto);
        //------------------------------------------------------------------------------
        return price;
    }
//    @Override
//    public Double bidSubject(String code, double price, String username) { wersja 1
//        Subject subject = getSubject(code);
//
//        if (subject.getEndDate().before(new Date())) {
//            throw new SubjectException(SubjectError.BIDDING_TIME_HAS_EXPIRED);
//        }
//
//        Double soldPrice = subject.getSoldPrice();
//        double currentPrice = soldPrice != null ? soldPrice : subject.getBasicPrice();
//        double minPrice = getBid(currentPrice);
//
//        if (minPrice > price) {
//            throw new SubjectException(SubjectError.PRICE_TOO_LOW);
//        }
//
//        double updatedPrice = currentPrice + price;
//        subject.setSoldPrice(updatedPrice);
//        subjectRepository.save(subject);
//        //--------------------------------nowe----------------------------------------------
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
//        String token = String.format("Bearer %s", details.getTokenValue());
//
//        PurchaseDto purchaseDto = new PurchaseDto(code, updatedPrice, username);
//        historyServiceClient.addPurchase(token, purchaseDto);
//        //------------------------------------------------------------------------------
//        return updatedPrice;
//    }

    @Override
    public Double getIncrementalAmount(Double price) {
        if (price == null || price < 100) {
            return 1.0;
        } else if (price < 1000) {
            return 5.0;
        } else {
            return ((int) (price / 1000)) * 10.0;
        }
    }

    @Override//do usunięcia
    public SubjectDto getSubjectForBid(String code) {
        Subject subject = getSubject(code);
        SubjectDto subjectDto = new SubjectDto(subject);
        Double currentPrice = subject.getSoldPrice();
        Double bid = getIncrementalAmount(currentPrice);
        subjectDto.setBid(bid);
        return subjectDto;
    }
//-----------------nowe metody-------------------------------------
    @Override
    public List<Subject> getActiveSubjects() {
        return subjectRepository.findNotArchiveWithEndDate();
    }

    @Override
    public List<Subject> getNewSubjects() {
        return subjectRepository.findWhereEndDateIsNull();
    }

    @Override
    public List<Subject> getBiddedSubjects() {
        return subjectRepository.findNotArchiveWithSoldPriceBiggerThanBasicPrice();
    }

    @Override
    public List<Subject> getBoughtSubjects() {
        return subjectRepository.findArchiveWithSoldPriceBiggerThanBasicPrice();
    }

    @Override
    public List<Subject> getArchiveSubjects() {
        return subjectRepository.findArchive();
    }
}
