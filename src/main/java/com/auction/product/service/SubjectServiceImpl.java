package com.auction.product.service;


import com.auction.product.exception.SubjectError;
import com.auction.product.exception.SubjectException;
import com.auction.product.model.Category;
import com.auction.product.model.PurchaseDto;
import com.auction.product.model.Subject;
import com.auction.product.repository.SubjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final PurchaseServiceClient purchaseServiceClient;

    @Override
    public List<Subject> getSubjectsByCategory(Category category) {
        return subjectRepository.findAllByCategories(category);
    }

    @Override
    public Subject getSubject(String code) {
        return subjectRepository.findById(code)
                .orElseThrow(() -> new SubjectException(SubjectError.SUBJECT_NOT_FOUND));
    }

    @Override
    public Subject addSubject(MultipartFile file, String subjectStr) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Subject subject = mapper.readValue(subjectStr, Subject.class);

        if (subjectRepository.existsByCode(subject.getCode())) {
            throw new SubjectException(SubjectError.SUBJECT_ALREADY_EXISTS);
        }

        if (file != null) {
            subject.setPicByte(file.getBytes());
        }
        if (subject.getEndDate() != null) {
            subject.setPublishDate(new Date());
        }

        return subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(String code) {
        Subject subject = subjectRepository.findById(code)
                .orElseThrow(() -> new SubjectException(SubjectError.SUBJECT_NOT_FOUND));
        subjectRepository.delete(subject);
    }

    @Override
    public Subject putSubject(MultipartFile file, String subjectStr, String deleteFile) throws IOException {
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

        PurchaseDto purchaseDto = new PurchaseDto(code, price, username);
        purchaseServiceClient.addPurchase(purchaseDto);
        return price;
    }

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
