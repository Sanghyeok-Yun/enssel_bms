package com.enssel.bms.entity;

import com.enssel.bms.constant.BookStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="book")
@Getter
@Setter
@ToString
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String bookNm;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    private String bookDetail;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
