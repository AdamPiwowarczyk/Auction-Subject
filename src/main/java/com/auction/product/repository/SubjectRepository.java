package com.auction.product.repository;


import com.auction.product.model.Category;
import com.auction.product.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
//    List<Subject> findAllByEndDateIsNotNull();

//    List<Subject> findAllByEndDateIsNull();
//    List<Subject> findAllByArchive(boolean archive);

    List<Subject> findAllByCategories(Category category);

    List<Subject> findAllByCodeIn(List<String> codes);

    boolean existsByCode(String code);

    @Query("select s from Subject s where s.endDate < current_time() and s.archive = false")
    List<Subject> findAllWithEndDateExceeded();

    //--------------------------nowe---------------------------------

    @Query("select s from Subject s where s.endDate is not null and s.archive = false")//aktywne
    List<Subject> findNotArchiveWithEndDate();

    @Query("select s from Subject s where s.soldPrice > s.basicPrice and s.archive = false")//licytowane
    List<Subject> findNotArchiveWithSoldPriceBiggerThanBasicPrice();

    @Query("select s from Subject s where s.endDate is null")//nowe
    List<Subject> findWhereEndDateIsNull();

    @Query("select s from Subject s where s.soldPrice > s.basicPrice and s.archive = true")//kupione
    List<Subject> findArchiveWithSoldPriceBiggerThanBasicPrice();

    @Query("select s from Subject s where s.archive = true")//archiwalne
    List<Subject> findArchive();
}
