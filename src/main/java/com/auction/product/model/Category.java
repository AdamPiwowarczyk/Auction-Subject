package com.auction.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class Category {
    @Id
//    @Column(unique = true) nie potrzebuję już tej walidacji, metoda existsByName wykonuje się szybciej
    String name;

//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//                    CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinTable(name = "subject_category",
//            joinColumns = @JoinColumn(name = "course_id"),
//            inverseJoinColumns = @JoinColumn(name = "subject_id"))
//    private Set<Subject> subjects = new HashSet<>();
//
//    public void addSubject(Subject subject) {
//        subjects.add(subject);
//    }
}