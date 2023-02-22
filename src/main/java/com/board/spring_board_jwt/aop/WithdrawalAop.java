package com.board.spring_board_jwt.aop;

import com.board.spring_board_jwt.entity.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class WithdrawalAop {
    @Before("execution(* com..service..*(..)) && args(user, ..)")
    public void beforeMethod(User user) {
        if (user.isWithdrawal()) {
            throw new IllegalArgumentException("탈퇴된 회원입니다.");
        }
    }
}
