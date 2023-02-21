package com.board.spring_board_jwt.service;

import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.dto.UserRequestDto;
import com.board.spring_board_jwt.entity.User;
import com.board.spring_board_jwt.entity.UserRoleEnum;
import com.board.spring_board_jwt.jwt.JwtUtil;
import com.board.spring_board_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<Object> signup(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        //중복 회원 에러
        ResponseMsgDto sameUser = ResponseMsgDto.builder()
                .msg("중복된 username입니다.")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sameUser);
        }
        UserRoleEnum role = UserRoleEnum.USER;
        if (userRequestDto.isAdmin()) {
            if (!userRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다");
            }
            role = UserRoleEnum.ADMIN;
        }
        Pattern namePattern = Pattern.compile("^([a-z[0-9]]){4,10}$"); //4자 영문+숫자
        Matcher nameMatcher = namePattern.matcher(username);

        Pattern pwPattern = Pattern.compile("^([a-zA-Z[0-9]]){8,15}$"); //8자 영문+숫자
        Matcher pwMatcher = pwPattern.matcher(userRequestDto.getPassword());
        ResponseMsgDto noName = ResponseMsgDto.builder()
                .msg("아이디는 4자 이상의 영소문자, 숫자만 가능합니다.")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        ResponseMsgDto noPw = ResponseMsgDto.builder()
                .msg("비밀번호는 8자 이상의 영대소문자, 숫자만 가능합니다.")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        if (!pwMatcher.find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noPw);
        } else if (!nameMatcher.find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noName);
        } else {
            User user = User.builder()
                    .username(username)
                    .password(password)
                    .role(role)
                    .build();
            userRepository.save(user);
            ResponseMsgDto ok = ResponseMsgDto.builder()
                    .msg("회원가입 완료!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(ok);
        }
    }
    @Transactional
    public ResponseEntity<Object> login(UserRequestDto userRequestDto, HttpServletResponse response) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();
        ResponseMsgDto noUser = ResponseMsgDto.builder()
                .msg("회원을 찾을 수 없습니다.")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        if (userRepository.findByUsername(username).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noUser);
        }
        User user = userRepository.findByUsername(username).get();
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                .msg("로그인 완료!")
                .statusCode(HttpStatus.OK.value())
                .build();
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(),user.getRole()));
        return ResponseEntity.status(HttpStatus.OK).body(responseMsgDto);
    }
}
