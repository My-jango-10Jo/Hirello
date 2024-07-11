package com.sparta.hirello.primary.board.service;

import com.sparta.hirello.primary.board.dto.BoardResponseDto;
import com.sparta.hirello.primary.board.dto.CreateBoardRequestDto;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.entity.UserRole;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponseDto createBoard(
            UserDetailsImpl userDetails,
            CreateBoardRequestDto requestDto
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
}
