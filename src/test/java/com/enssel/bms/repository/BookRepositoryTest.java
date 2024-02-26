package com.enssel.bms.repository;

import com.enssel.bms.book.constant.BookStatus;
import com.enssel.bms.book.entity.Book;
import com.enssel.bms.book.repository.BookRepository;
import com.enssel.bms.entity.QBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    public void createBookList2(){
        for(int i=1; i<=5; i++){
            Book book = new Book();
            book.setBookNm("테스트 책" + i);
            book.setBookDetail("테스트 상세 설명" + i);
            book.setBookStatus(BookStatus.RENTABLE);
            book.setStockNumber(1);
            book.setRegTime(LocalDateTime.now());
            book.setUpdateTime(LocalDateTime.now());
            Book savedBook = bookRepository.save(book);
        }

        for(int i=6; i<=10; i++){
            Book book = new Book();
            book.setBookNm("테스트 책" + i);
            book.setBookDetail("테스트 상세 설명" + i);
            book.setBookStatus(BookStatus.NON_RENTABLE);
            book.setStockNumber(0);
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

    @Test
    @DisplayName("QueryDSL 조회 테스트2")
    public void queryDSLTest2(){
        this.createBookList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QBook book = QBook.book;

        String bookDeatil = "테스트 상세 설명";
        String bookStatus = "RENTABLE";

        booleanBuilder.and(book.bookDetail.like("%" + bookDeatil + "%"));
        if(bookStatus.equals(BookStatus.RENTABLE))
            booleanBuilder.and(book.bookStatus.eq(BookStatus.RENTABLE));

        Pageable pageable = PageRequest.of(0, 3);
        Page<Book> bookPagingResult = bookRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + bookPagingResult.getTotalElements());

        List<Book> resultBookList = bookPagingResult.getContent();
        resultBookList.stream().forEach(resultBook -> System.out.println(resultBook.toString()));
    }
}