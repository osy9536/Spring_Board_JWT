package com.board.spring_board_jwt.controller;

import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.dto.UserRequestDto;
import com.board.spring_board_jwt.security.UserDetailsImpl;
import com.board.spring_board_jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseMsgDto signup(@Valid @RequestBody UserRequestDto userRequestDto) {
        return userService.signup(userRequestDto);
    }

    @PostMapping("/login")
    public ResponseMsgDto login(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response) {
        return userService.login(userRequestDto, response);
    }
    @PostMapping("/withdrawal")
    public ResponseMsgDto withdrawal(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.withdrawal (userDetails.getUser());
    }
}
