package com.sparta.hirello.primary.board.service;

import com.sparta.hirello.primary.board.dto.request.BoardMemberRequest;
import com.sparta.hirello.primary.board.dto.request.BoardRequest;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.board.entity.BoardRole;
import com.sparta.hirello.primary.board.repository.BoardMemberRepository;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.repository.UserRepository;
<<<<<<< Updated upstream
import com.sparta.hirello.secondary.exception.NotBoardManagerException;
import com.sparta.hirello.secondary.exception.UninvitedBoardMemberException;
=======
>>>>>>> Stashed changes
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;
    private final UserRepository userRepository;

    /**
     * 보드 생성
     */
    @Transactional
    public Board createBoard(BoardRequest request, User user) {
        return boardRepository.save(Board.of(request, user));
    }

    /**
     * 전체 보드 조회 (admin only)
     */
    public Page<Board> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    /**
     * 사용자 보드(자신이 생성한 보드이거나 초대된 보드) 목록 조회
     */
    public Page<Board> getUserBoards(User user, Pageable pageable) {
        return boardRepository.findByUser(user, pageable);
    }

    /**
     * 보드 조회
     */
    public Board getBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 보드입니다."));
    }

    /**
     * 보드 수정
     */
    @Transactional
    public Board updateBoard(Long boardId, BoardRequest request, User user) {
        Board board = getBoardAndVerifyManager(boardId, user);
        board.update(request);
        return board;
    }

    /**
     * 보드 삭제
     */
    @Transactional
    public Long deleteBoard(Long boardId, User user) {
        Board board = getBoardAndVerifyManager(boardId, user);
        boardRepository.delete(board);
    }

    /**
     * 보드 맴버 초대
     */
    @Transactional
    public BoardMember inviteUser(Long boardId, BoardMemberRequest request, User user) {
        Board board = getBoardAndVerifyManager(boardId, user);
        User invitedUser = getUser(request.getUserId());
        // 이미 해당 보드에 초대된 사용자인 경우
        if (boardMemberRepository.existsByBoardAndUser(board, invitedUser)) {
            throw new IllegalArgumentException("이미 해당 보드에 초대된 사용자입니다.");
        }
        BoardMember boardMember = BoardMember.of(invitedUser, board, request.getRole());
        return boardMemberRepository.save(boardMember);
    }

    /**
     * 보드 멤버 권한 변경
     */
    @Transactional
    public BoardMember updateBoardMemberRole(Long boardId, BoardMemberRequest request, User user) {
        Board board = getBoardAndVerifyManager(boardId, user);
        BoardMember boardMember = getBoardMember(board, getUser(request.getUserId()));
        // 같은 권한으로 변경하는지 확인
        if (boardMember.getBoardRole().equals(request.getRole())) {
            throw new IllegalArgumentException("해당 회원의 권한이 이미 " + request.getRole() + " 상태입니다.");
        }
        boardMember.updateRole(request.getRole());
        return boardMember;
    }

    private Board getBoardAndVerifyManager(Long boardId, User user) {
        Board board = getBoard(boardId);
        BoardMember member = getBoardMember(board, user);
        if (!member.getBoardRole().equals(BoardRole.MANAGER)) {
            throw new NotBoardManagerException();
        }
        return board;
    }

    private BoardMember getBoardMember(Board board, User user) {
        return boardMemberRepository.findByBoardAndUser(board, user)
                .orElseThrow(UninvitedBoardMemberException::new);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

}

