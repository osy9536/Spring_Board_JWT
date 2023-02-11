package com.board.spring_board_jwt.service;

import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.dto.UserRequestDto;
import com.board.spring_board_jwt.entity.User;
import com.board.spring_board_jwt.jwt.JwtUtil;
import com.board.spring_board_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity signup(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 회원이 존재합니다.");
        }
        Pattern namePattern = Pattern.compile("^([a-z[0-9]]){4,10}$"); //8자 영문+숫자
        Matcher nameMatcher = namePattern.matcher(username);

        Pattern pwPattern = Pattern.compile("^([a-zA-Z[0-9]]){8,15}$"); //8자 영문+숫자
        Matcher pwMatcher = pwPattern.matcher(password);
        ResponseMsgDto noName = new ResponseMsgDto("아이디는 4자 이상의 영소문자, 숫자만 가능합니다.", HttpStatus.BAD_REQUEST.value());
        ResponseMsgDto noPw = new ResponseMsgDto("비밀번호는 8자 이상의 영대소문자, 숫자만 가능합니다.", HttpStatus.BAD_REQUEST.value());

        if (!pwMatcher.find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noPw);
        } else if (!nameMatcher.find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noName);
        } else {
            User user = new User(username,password);
            userRepository.save(user);
            ResponseMsgDto ok = new ResponseMsgDto("회원가입 완료!", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(ok);
        }
    }
    @Transactional
    public ResponseEntity<Object> login(UserRequestDto userRequestDto, HttpServletResponse response) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
            ()-> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        if(!user.getPassword().equals(password)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        ResponseMsgDto responseMsgDto = new ResponseMsgDto("로그인 완료!", HttpStatus.OK.value());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
        return ResponseEntity.status(HttpStatus.OK).body(responseMsgDto);
    }
}
