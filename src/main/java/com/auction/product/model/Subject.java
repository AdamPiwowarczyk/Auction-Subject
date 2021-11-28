package com.auction.product.model;

import com.auction.product.exception.SubjectError;
import com.auction.product.exception.SubjectException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Subject {
    @Id
    protected String code;
    @NotBlank
    protected String caption;
    @Column(length = 500)
    protected String description;
    protected Date publishDate;
    protected Date endDate;
    protected Double basicPrice;
    protected Double soldPrice;
    protected Boolean archive = false;
    @Column(length = 16777215)
    protected byte[] picByte;
    @ManyToMany
    protected Set<Category> categories = new HashSet<>();

    public void put(Subject subject, MultipartFile file, boolean deleteFile) throws IOException {
        caption = subject.caption;
        description = subject.description;
        basicPrice = subject.basicPrice;
        soldPrice = subject.soldPrice;
        endDate = subject.endDate;
        categories = subject.categories;
        if (file != null) {
            setPicByt(file.getBytes());
        } else if (deleteFile) {
            picByte = null;
        }
        if (subject.getEndDate() != null) {
            publishDate = new Date();
        }
    }

    public void setPicByt(byte[] picByte) {
        if (picByte.length > 16777215) {
            throw new SubjectException(SubjectError.MAX_SIZE_OF_FILE_EXCEEDED);
        }
        this.picByte = picByte;
    }

    public void setArchive() {
        archive = true;
    }

    public void setBasicPrice(Double basicPrice) {
        if (basicPrice < 0) {
            throw new SubjectException(SubjectError.PRICE_TOO_LOW);
        }
        this.basicPrice = basicPrice;
    }

    public void setDescription(String description) {
        if (description == null) {
            return;
        }

        if (description.length() > 500) {
            throw new SubjectException(SubjectError.DESCRIPTION_TOO_LONG);
        }
        this.description = description;
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }
}
