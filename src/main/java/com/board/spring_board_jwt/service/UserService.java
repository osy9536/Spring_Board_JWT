package com.board.spring_board_jwt.service;

import com.board.spring_board_jwt.annotation.NoChecking;
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
    public ResponseMsgDto signup(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 username입니다.");
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
        return ResponseMsgDto.builder()
                .msg("회원가입 완료!")
                .statusCode(HttpStatus.OK.value())
                .build();
    }


    @Transactional
    public ResponseMsgDto login(UserRequestDto userRequestDto, HttpServletResponse response) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }
        User user = userRepository.findByUsername(username).get();
        if (user.isWithdrawal()) { //탈퇴 회원 검증
            throw new IllegalArgumentException("탈퇴한 회원입니다.");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return ResponseMsgDto.builder()
                .msg("로그인 완료!")
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @NoChecking
    @Transactional
    public ResponseMsgDto withdrawal(User user) {
        User user1 = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> {
                    throw new IllegalArgumentException("회원이 존재하지 않습니다.");
                }
        );
        user1.changeWithdrawal();
        if (user1.isWithdrawal()) {
            return ResponseMsgDto.builder()
                    .msg("계정 탈퇴 성공")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        } else {
            return ResponseMsgDto.builder()
                    .msg("계정 복구 성공")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }
    }
}