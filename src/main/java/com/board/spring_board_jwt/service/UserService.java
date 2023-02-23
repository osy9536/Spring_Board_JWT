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


    @Transactional
    public ResponseEntity<Object> login(UserRequestDto userRequestDto, HttpServletResponse response) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();
        ResponseMsgDto noUser = ResponseMsgDto.builder()
                .msg("회원을 찾을 수 없습니다.")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        ResponseMsgDto withdrawUser = ResponseMsgDto.builder()
                .msg("탈퇴한 회원입니다.")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        if (userRepository.findByUsername(username).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noUser);
        }
        User user = userRepository.findByUsername(username).get();
        if (user.isWithdrawal()) { //탈퇴 회원 검증
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(withdrawUser);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                .msg("로그인 완료!")
                .statusCode(HttpStatus.OK.value())
                .build();
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return ResponseEntity.status(HttpStatus.OK).body(responseMsgDto);
    }
    @Transactional
    public ResponseEntity<Object> withdrawal(UserRequestDto userRequestDto, HttpServletResponse response) {
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
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                .msg("계정 탈퇴 성공")
                .statusCode(HttpStatus.OK.value())
                .build();
        ResponseMsgDto responseMsgDto2 = ResponseMsgDto.builder()
                .msg("계정 복구 성공")
                .statusCode(HttpStatus.OK.value())
                .build();
        user.changeWithdrawal();
        if (user.isWithdrawal()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseMsgDto);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(responseMsgDto2);
        }
    }
}
