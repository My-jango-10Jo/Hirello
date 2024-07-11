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

    public BoardResponseDto createBoard(
            UserDetailsImpl userDetails,
            BoardRequestDto requestDto
    ) {
        User user = userDetails.getUser();
        Board board = new Board(requestDto, user);

        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("보드 생성 권한이 없습니다.");
        }

        boardRepository.save(board);
        return new BoardResponseDto(board, user.getUsername());
    }

    public List<BoardResponseDto> getBoardList() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponseDto> responseDtos = new ArrayList<>();

        for (Board board : boards) {
            responseDtos.add(new BoardResponseDto(board, board.getUser().getUsername()));
        }

        return responseDtos;
    }

    @Transactional
    public BoardResponseDto updateBoard(
            UserDetailsImpl userDetails,
            BoardRequestDto requestDto, Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("보드가 존재 하지 않습니다.")
        );

        User user = userDetails.getUser();

        if (user.getId().equals(board.getUser().getId())) {
            throw new IllegalArgumentException("보드 생성자가 아닙니다.");
        }

        String boardName = requestDto.getBoardName();
        String headline = requestDto.getHeadline();

        board.update(boardName, headline);
        return new BoardResponseDto(board.getBoardName(), board.getHeadline(),
                user.getUsername());
    }
}
