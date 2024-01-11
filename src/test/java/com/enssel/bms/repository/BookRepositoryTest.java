package com.enssel.bms.repository;

import com.enssel.bms.constant.BookStatus;
import com.enssel.bms.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    public void createBookTest(){
        Book book = new Book();
        book.setBookNm("테스트 책");
        book.setBookDetail("테스트 상세설명");
        book.setBookStatus(BookStatus.RENTABLE);
        book.setStockNumber(1);
        book.setRegTime(LocalDateTime.now());
        book.setUpdateTime(LocalDateTime.now());
        Book savedBook = bookRepository.save(book);
        System.out.println(savedBook.toString());
    }
}