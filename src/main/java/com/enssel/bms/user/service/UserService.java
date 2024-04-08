package com.enssel.bms.user.service;

import com.enssel.bms.mail.dto.MailRequest;
import com.enssel.bms.mail.service.MailService;
import com.enssel.bms.user.dto.UserRequest;
import com.enssel.bms.user.entity.User;
import com.enssel.bms.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private Map<String, String> signUpCodeMap = new HashMap<>();
    private Map<String, String> passedCodeMap = new HashMap<>();
    private char[] characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    public User getUser(String userId){
        return userRepository.findById(userId).orElseThrow();
    }
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public void sendSignUpCodeToEmail(HttpServletRequest request, String userId) throws MessagingException {
        String signUpCode = createSignUpCode();
        signUpCodeMap.put(request.getRemoteAddr()+";"+request.getLocalAddr(), signUpCode);

        MailRequest mailRequest = new MailRequest();
        mailRequest.setAddress(userId+"@enssel.com");
        mailRequest.setTitle("Enssel BMS 회원가입 인증 코드입니다.");

        String content = "안녕하세요 Enssel Book Management System입니다.<br><br>" +
                "인증코드는 ["+signUpCode+"]입니다.<br><br>" +
                "감사합니다.";
        mailRequest.setContent(content);
        mailService.sendMail(mailRequest);
    }

    public String checkSignUpCode(HttpServletRequest request, String code){
        String addr = request.getRemoteAddr()+";"+request.getLocalAddr();

        if(!signUpCodeMap.containsKey(addr)){
            throw new IllegalStateException("비정상적인 인증이 감지되었습니다.");
        }
        else if(!signUpCodeMap.get(addr).equals(code)){
            throw new IllegalStateException("잘못된 인증코드입니다.");
        }
        else{
            String newCode = createSignUpCode();
            passedCodeMap.put(addr, newCode);

            return newCode;
        }
    }

    public String createSignUpCode(){
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);

        for(int i=0; i<8; i++){
            sb.append(characters[random.nextInt(characters.length)]);
        }

        return sb.toString();
    }

    public User signUp(HttpServletRequest request, UserRequest userRequest){
        String addr = request.getRemoteAddr()+";"+request.getLocalAddr();

        if(isExistUser(userRequest)){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
        else if(!passedCodeMap.containsKey(addr)){
            throw new IllegalStateException("email 인증을 진행해주세요.");
        }
        else if(!passedCodeMap.get(addr).equals(userRequest.getPassedCode())){
            throw new IllegalStateException("email 인증이 정상적으로 완료되지 않았습니다.");
        }
        else{
            signUpCodeMap.remove(addr);
            passedCodeMap.remove(addr);
        }

        User user = new User();
        user.updateByRequest(userRequest);

        return userRepository.save(user);
    }

    private boolean isExistUser(UserRequest userRequest){
        return userRepository.findById(userRequest.getUserId()).isPresent();
    }
}
