package com.enssel.bms.repository;

import com.enssel.bms.constant.BookStatus;
import com.enssel.bms.entity.Book;
import com.enssel.bms.entity.QBook;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class BookRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    public void createBookList(){
        for(int i=1; i<=10; i++){
            Book book = new Book();
            book.setBookNm("테스트 책" + i);
            book.setBookDetail("테스트 상세 설명" + i);
            book.setBookStatus(BookStatus.RENTABLE);
            book.setStockNumber(1);
            book.setRegTime(LocalDateTime.now());
            book.setUpdateTime(LocalDateTime.now());
            Book savedBook = bookRepository.save(book);
        }
    }

    @Test
    @DisplayName("도서 저장 테스트")
    public void createBookTest(){
        Book book = new Book();
        book.setBookNm("테스트 책");
        book.setBookDetail("테스트 상세 설명");
        book.setBookStatus(BookStatus.RENTABLE);
        book.setStockNumber(1);
        book.setRegTime(LocalDateTime.now());
        book.setUpdateTime(LocalDateTime.now());
        Book savedBook = bookRepository.save(book);
        System.out.println(savedBook.toString());
    }

    @Test
    @DisplayName("도서명 조회 테스트")
    public void findByBookNmTest(){
        this.createBookList();
        List<Book> bookList = bookRepository.findByBookNm("테스트 책1");
        bookList.stream().forEach(book -> System.out.println(book.toString()));
    }

    @Test
    @DisplayName("JPQL 도서 조회 테스트")
    public void findByBookDetailTest(){
        this.createBookList();
        List<Book> bookList = bookRepository.findByBookDetail("테스트 상세 설명");
        bookList.stream().forEach(book -> System.out.println(book.toString()));
    }

    @Test
    @DisplayName("QueryDSL 조회 테스트")
    public void queryDSLTest(){
        this.createBookList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBook qBook = QBook.book;
        JPAQuery<Book> query = queryFactory.selectFrom(qBook)
                .where(qBook.bookStatus.eq(BookStatus.RENTABLE))
                .where(qBook.bookDetail.eq("테스트 상품 상세 설명" + "%"))
                .orderBy(qBook.id.asc());
        List<Book> bookList = query.fetch();
        bookList.stream().forEach(book -> System.out.println(book.toString()));
    }
}