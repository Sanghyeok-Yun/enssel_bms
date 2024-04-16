package com.enssel.bms.user.controller;

import com.enssel.bms.core.controller.AbstractController;
import com.enssel.bms.user.dto.ChangePasswordRequest;
import com.enssel.bms.user.dto.UserRequest;
import com.enssel.bms.user.entity.UserInfo;
import com.enssel.bms.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController extends AbstractController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserInfo getUser(@PathVariable(name="userId") String userId){
        return userService.getUser(userId);
    }
    
    @GetMapping("/table")
    public List<UserInfo> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/code/{userId}")
    public void sendSignUpCode(HttpServletRequest request, @PathVariable(name="userId") String userId) throws MessagingException {
        userService.sendSignUpCodeToEmail(request, userId);
    }

    @GetMapping("/code/check")
    public String checkSignUpCode(HttpServletRequest request, @RequestParam(name="code") String code) {
        return userService.checkSignUpCode(request, code);
    }
        
    @PostMapping
    public UserInfo signUp(HttpServletRequest request, @Valid @RequestBody UserRequest userRequest){
        return userService.signUp(request, userRequest);
    }

    @PutMapping("/password")
    public void signUp(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        userService.changePassword(changePasswordRequest);
    }

}
