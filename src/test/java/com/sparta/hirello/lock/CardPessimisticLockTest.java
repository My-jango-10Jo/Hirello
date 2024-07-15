package com.sparta.hirello.lock;

import com.sparta.hirello.primary.card.dto.request.CardCreateRequest;
import com.sparta.hirello.primary.card.dto.request.CardUpdateRequest;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.repository.CardRepository;
import com.sparta.hirello.primary.card.service.CardService;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.user.dto.request.SignupRequest;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.entity.UserRole;
import com.sparta.hirello.primary.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
public class CardPessimisticLockTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardService cardService;
    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() {
        //유저 생성
        String username = "TestUser" + i;
        String password = "12345qwert!@";
        String name = "테스트유저" + i;
        boolean admin = false;
        String adminToken = "";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword(password);
        signupRequest.setName(name);
        signupRequest.setAdmin(admin);
        signupRequest.setAdminToken(adminToken);

        User loginUser = User.of(signupRequest, password, UserRole.USER);
        userRepository.save(loginUser);

        // 카드 생성
        Long progressId = 2L;
        String title = "제목입니다.";
        String description = "내용입니다.";
        LocalDateTime deadLine = LocalDateTime.of(2024, 7, 20, 13, 00);
        Long workId = 3L;

        Progress progress;
        User member;

        CardCreateRequest cardCreateRequest = new CardCreateRequest();
        cardCreateRequest.setProgressId(progressId);
        cardCreateRequest.setTitle(title);
        cardCreateRequest.setDescription(description);
        cardCreateRequest.setDeadline(deadLine);
        cardCreateRequest.setWorkerId(workId);

        Card card = Card.of(cardCreateRequest, loginUser, );
        cardRepository.save(card);

        //CardUpdateRequest 생성
    }

    @AfterEach
    void tearDown() {
        cardRepository.deleteAll();
    }

    @Test
    @DisplayName("비관적 락을 이용한 동시성 제어 - Card update")
    //given

    //when
        for(int i = 0;i< 10;i++) {
        cardService.updateCard(1L, new CardUpdateRequest(), user1);
    }
    //then
}

}
