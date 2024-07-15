//package com.sparta.hirello;
//
//import com.sparta.hirello.primary.board.dto.request.BoardRequest;
//import com.sparta.hirello.primary.board.entity.Board;
//import com.sparta.hirello.primary.board.entity.BoardRole;
//import com.sparta.hirello.primary.board.repository.BoardRepository;
//import com.sparta.hirello.primary.board.service.BoardService;
//import com.sparta.hirello.primary.user.dto.request.SignupRequest;
//import com.sparta.hirello.primary.user.entity.User;
//import com.sparta.hirello.primary.user.entity.UserRole;
//import com.sparta.hirello.primary.user.repository.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class BoardServiceTest {
//
//    private static final Logger logger = LoggerFactory.getLogger(BoardServiceTest.class);
//
//    @Autowired
//    private BoardService boardService;
//
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//    @Test
//    void hello() {
//        logger.debug("aaa");
//    }
//
//    @Test
//    @DisplayName("매니저 2명이 동시에 보드를 수정을 시도했을 때")
//    @Transactional
//    void testOptimisticLockingWithConcurrentUpdates() throws InterruptedException {
//        logger.debug("aaa");
//        // Given
//        SignupRequest signupRequest1 = new SignupRequest();
//        signupRequest1.setUsername("manager1");
//        signupRequest1.setPassword("Aa12345678!");
//        signupRequest1.setName("Manager One");
//        logger.debug("aaa");
//        User user1 = User.of(signupRequest1, "encodedPassword1", UserRole.USER);
//        userRepository.save(user1);
//
//        logger.debug("bbb");
//        SignupRequest signupRequest2 = new SignupRequest();
//        signupRequest2.setUsername("manager2");
//        signupRequest2.setPassword("Bb12345678!");
//        signupRequest2.setName("Manager Two");
//
//        logger.debug("ccc");
//        User user2 = User.of(signupRequest2, "encodedPassword2", UserRole.USER);
//        userRepository.save(user2);
//
//        BoardRequest boardRequest = new BoardRequest();
//        boardRequest.setTitle("Board Title");
//        boardRequest.setDescription("Board Description");
//
//        Board board = Board.of(boardRequest, user1); // 첫 번째 매니저로 보드 생성
//        boardRepository.saveAndFlush(board);
//
//        // 초대하여 두 번째 매니저로 권한 부여
//        board.addMember(user2, BoardRole.MANAGER); // 첫 번째 매니저가 두 번째 매니저 초대
//        boardRepository.save(board);
//
//        // When
//
//        //고정된 두개의 스레드 풀 생성
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        //두개의 작업이 동시에 시작될 수 있도록 조정
//        CountDownLatch latch = new CountDownLatch(2);
//
//        BoardRequest boardRequestChange1 = new BoardRequest();
//        boardRequestChange1.setTitle("Board Title Change1");
//        boardRequestChange1.setDescription("Board Description Change1");
//
//        BoardRequest boardRequestChange2 = new BoardRequest();
//        boardRequestChange2.setTitle("Board Title Change2");
//        boardRequestChange2.setDescription("Board Description Change2");
//
//        //사용자1(최초로 보드를 생성한 매니저)
//        Runnable manager1Task = () -> {
//            try {
//                boardService.updateBoard(board.getId(), boardRequestChange1, user1);
//            } catch (Exception e) {
//                System.out.println("Manager1 업데이트 실패: " + e.getMessage());
//            } finally {
//                latch.countDown();
//            }
//        };
//
//        //사용자2(초대되어 매니저 권한을 얻은 매니저2)
//        Runnable manager2Task = () -> {
//            try {
//                boardService.updateBoard(board.getId(), boardRequestChange2, user2);
//            } catch (Exception e) {
//                System.out.println("Manager2 업데이트 실패 " + e.getMessage());
//            } finally {
//                latch.countDown();
//            }
//        };
//
//        executor.submit(manager1Task);
//        executor.submit(manager2Task);
//
//        latch.await();
//        executor.shutdown();
//
//        // Then
//        Board updatedBoard = boardRepository.findById(board.getId()).orElseThrow();
//        System.out.println("Final Title: " + updatedBoard.getTitle());
//        System.out.println("Final Description: " + updatedBoard.getDescription());
//
//        // Assertions to ensure the final state of the board
//        assertNotNull(updatedBoard);
//        assertEquals(1,updatedBoard.getVersion());
//    }
//}
//
