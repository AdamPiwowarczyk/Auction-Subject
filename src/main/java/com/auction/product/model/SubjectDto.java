package com.auction.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto extends Subject {//do wyjebania
    private double bid;

    public SubjectDto(Subject subject) {
        this.code = subject.getCode();
        this.caption = subject.getCaption();
        this.description = subject.getDescription();
        this.publishDate = subject.getPublishDate();
        this.endDate = subject.getEndDate();
        this.basicPrice = subject.getBasicPrice();
        this.soldPrice = subject.getSoldPrice();
        this.categories = subject.getCategories();
        this.archive = subject.getArchive();
        this.picByte = subject.getPicByte();
    }
}