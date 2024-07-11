package com.sparta.hirello.primary.board.service;

import com.sparta.hirello.primary.board.dto.request.BoardRequestDto;
import com.sparta.hirello.primary.board.dto.response.BoardResponseDto;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.entity.UserRole;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;


    /**
     * 보드를 생성 합니다.
     *
     * @param userDetails 인가된 유저 정보
     * @param requestDto  클라이언트에서 요청한 보드 생성 정보
     */
    public BoardResponseDto createBoard(
            UserDetailsImpl userDetails,
            BoardRequestDto requestDto
    ) {
        User user = userDetails.getUser();
        Board board = new Board(requestDto, user);

        //compareUserRole(user);

        boardRepository.save(board);
        return new BoardResponseDto(board, user.getUsername());
    }

    /**
     * 보드 목록들을 조회 합니다.
     *
     * @param user 인가된 유저 정보 일단 Admin 권한을 가진 인원만 전체 조회로 구현 TeamEntity 관련 구현이 완료 되면 로직 추가
     */
    public List<BoardResponseDto> getBoardList(User user) {
        List<Board> boards;
        List<BoardResponseDto> responseDtos = new ArrayList<>();

        if (user.getRole().equals(UserRole.ADMIN)) {
            boards = boardRepository.findAll();
            for (Board board : boards) {
                responseDtos.add(new BoardResponseDto(board, board.getUser().getUsername()));
            }
        }

        return responseDtos;
    }

    /**
     * 보드 수정
     *
     * @param userDetails 인가된 유저 정보
     * @param requestDto  클라이언트에서 요청한 보드 수정 정보
     * @param boardId     수정 해야할 보드 정보
     */
    @Transactional
    public BoardResponseDto updateBoard(
            UserDetailsImpl userDetails,
            BoardRequestDto requestDto, Long boardId) {

        Board board = getBoardEntity(boardId);

        User user = userDetails.getUser();

        compareUserId(board, user);

        String boardName = requestDto.getBoardName();
        String headline = requestDto.getHeadline();

        board.update(boardName, headline);
        return new BoardResponseDto(board.getBoardId(), board.getBoardName(), board.getHeadline(),
                user.getUsername());
    }

    public void deleteBoard(UserDetailsImpl userDetails, Long boardId) {
        Board board = getBoardEntity(boardId);
        User user = userDetails.getUser();

        compareUserId(board, user);

        boardRepository.delete(board);
    }

    /**
     * 보드 정보가 있는지 확인 후 있으면 보드 정보 저장, 없으면 Exception
     *
     * @param boardId 인가된 유저 정보
     */
    public Board getBoardEntity(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("보드가 존재 하지 않습니다.")
        );
    }

    /**
     * 보드생성자와 작업 요청한 유저 정보가 맞는지 확인 합니다, 틀리면 Exception
     *
     * @param board board에 getUser().getId()의 정보
     * @param user  요청자의 유저 정보
     */
    public void compareUserId(Board board, User user) {
        if (!user.getId().equals(board.getUser().getId())) {
            throw new IllegalArgumentException("보드 생성자가 아닙니다.");
        }
    }

    /**
     * 유저의 권한 정보가 매니저 인지 확인,매니저가 아니면 Exception
     *
     * @param user 요청자의 유저 정보
     */
    public void compareUserRole(User user) {
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
