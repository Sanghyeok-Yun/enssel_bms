package com.enssel.bms.user.service;

import com.enssel.bms.user.dto.UserRequest;
import com.enssel.bms.user.entity.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
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
class UserInfoServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserRequest createUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("윤상혁");
        userRequest.setUsername("shyoon");
        userRequest.setPassword("1111");

        return userRequest;
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveUserTest(HttpServletRequest request){
        UserRequest user = createUserRequest();
        UserInfo savedUserInfo = userService.signUp(request, user);
        System.out.println(savedUserInfo.toString());

        Assertions.assertEquals(user.getUsername(), savedUserInfo.getUsername());
    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    public void signUpTest(HttpServletRequest request){
        UserRequest user1 = this.createUserRequest();
        UserRequest user2 = this.createUserRequest();
        userService.signUp(request, user1);

        Exception exception = assertThrows(IllegalStateException.class, () ->
                userService.signUp(request, user2)
        );
        System.out.println(exception.getMessage());

        Assertions.assertEquals("이미 가입된 회원입니다.", exception.getMessage());
    }
}