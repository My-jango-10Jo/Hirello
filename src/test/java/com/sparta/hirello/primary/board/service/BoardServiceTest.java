//package com.sparta.hirello.primary.board.service;
//
//import com.sparta.hirello.primary.board.dto.request.BoardRequest;
//import com.sparta.hirello.primary.board.entity.Board;
//import com.sparta.hirello.primary.board.repository.BoardRepository;
//import com.sparta.hirello.primary.user.entity.User;
//import com.sparta.hirello.primary.user.entity.UserRole;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class BoardServiceTest {
//
//    @Mock
//    private BoardRepository boardRepository;
//
//    @InjectMocks
//    private BoardService boardService;
//
//    private User user;
//    private Board board;
//    private BoardRequest boardRequest;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        MockitoAnnotations.openMocks(this);
//
//        // Initialize user
//        user = User.class.getDeclaredConstructor().newInstance();
//        setField(user, "id", 1L);
//        setField(user, "username", "testuser");
//        setField(user, "password", "password123");
//        setField(user, "name", "Test User");
//        setField(user, "role", UserRole.USER);
//
//        // Initialize board
//        board = Board.class.getDeclaredConstructor().newInstance();
//        setField(board, "id", 1L);
//        setField(board, "title", "Original Title");
//        setField(board, "description", "Original description");
//
//        // Initialize boardRequest
//        boardRequest = new BoardRequest();
//        boardRequest.setTitle("Updated Title");
//        boardRequest.setDescription("Updated Content");
//
//        boardService = spy(new BoardService(boardRepository, null, null));
//
//        // Mock repository methods
////        when(boardService.getBoardAndVerifyManager(any(Long.class), any(User.class))).thenReturn(null);
//    }
//
//
//
//    @Test
//    void testUpdateBoard() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        // Given
//        Long boardId = 2L;
//
//        // Using reflection to invoke private method
//        Method getBoardAndVerifyManagerMethod = BoardService.class.getDeclaredMethod("getBoardAndVerifyManager", Long.class, User.class);
//        getBoardAndVerifyManagerMethod.setAccessible(true);
//        Board verifiedBoard = (Board) getBoardAndVerifyManagerMethod.invoke(boardService, boardId, user);
//
//
//        // When
//        Board updatedBoard = boardService.updateBoard(boardId, boardRequest, user);
//
//        // Then
//        assertThat(updatedBoard).isNull();
////        assertThat(updatedBoard.getTitle()).isEqualTo("Updated Title");
////        assertThat(updatedBoard.getDescription()).isEqualTo("Updated Content");
////        verify(boardRepository, times(1)).save(updatedBoard);
//    }
//
//    private void setField(Object target, String fieldName, Object value) throws Exception {
//        Field field = target.getClass().getDeclaredField(fieldName);
//        field.setAccessible(true);
//        field.set(target, value);
//    }
//
//
//}