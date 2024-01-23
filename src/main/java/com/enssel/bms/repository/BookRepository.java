package com.enssel.bms.repository;

import com.enssel.bms.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {

    List<Book> findByBookNm(String bookNm);

    @Query("select i from Book i where i.bookDetail like %:bookDetail% order by i.id asc")
    List<Book> findByBookDetail(@Param("bookDetail") String bookDetail);
}
