package com.sparta.hirello.primary.board.service;

import com.sparta.hirello.primary.board.dto.CreateBoardRequestDto;
import com.sparta.hirello.primary.board.dto.CreateBoardResponseDto;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.entity.UserRole;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public CreateBoardResponseDto createBoard(
            UserDetailsImpl userDetails,
            CreateBoardRequestDto requestDto
    ) {
        User user = userDetails.getUser();
        Board board = new Board(requestDto, user);

        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("보드 생성 권한이 없습니다.");
        }

        boardRepository.save(board);
        return new CreateBoardResponseDto(board, user.getUsername());
    }
}
