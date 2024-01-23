package com.enssel.bms.service;

import com.enssel.bms.dto.MemberFormDto;
import com.enssel.bms.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("윤상혁");
        memberFormDto.setEmail("shyoon@enssel.com");
        memberFormDto.setPassword("1111");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){
        Member member = this.createMember();
        Member savedMember = memberService.saveMember(member);
        System.out.println(savedMember.toString());

        Assertions.assertEquals(member.getEmail(), savedMember.getEmail());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = this.createMember();
        Member member2 = this.createMember();
        memberService.saveMember(member1);

        Exception exception = assertThrows(IllegalStateException.class, () ->
                memberService.saveMember(member2)
        );
        System.out.println(exception.getMessage());

        Assertions.assertEquals("이미 가입된 회원입니다.", exception.getMessage());
    }
}