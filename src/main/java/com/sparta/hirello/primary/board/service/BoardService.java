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
import com.sparta.hirello.secondary.exception.NotBoardManagerException;
import com.sparta.hirello.secondary.exception.UninvitedBoardMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
/**
 * 아래 주석한 메서드는 순모님이 작성하셨던 메서드 입니다
 * Long으로 반환 타입 선언하셨는데 막상 return문이 없어서 void로 수정 했습니다
 * 솔직히 삭제하는데 반환할게 딱히 없다고 생각해서 void로 수정했지만 나중에 확인하시고 수정 필요하시면 수정 부탁드립니다
* */
//    @Transactional
//    public Long deleteBoard(Long boardId, User user) {
//        Board board = getBoardAndVerifyManager(boardId, user);
//        boardRepository.delete(board);
//
//    }


//이건 제가 수정한거
    @Transactional
    public void deleteBoard(Long boardId, User user) {
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

    //에러가 생겨서 exception 부분만 임의로 생성했습니다
    //116번째줄 확인하시고 수정 필요하시면 수정 부탁드려요
    private Board getBoardAndVerifyManager(Long boardId, User user) {
        Board board = getBoard(boardId);
        BoardMember member = getBoardMember(board, user);
        if (!member.getBoardRole().equals(BoardRole.MANAGER)) {
            throw new NotBoardManagerException("이 보드의 매니저가 아닙니다");
        }
        return board;
    }

    //여기도 에러가 생겨서 임의로 exception 생성했습니다
    private BoardMember getBoardMember(Board board, User user) {
        return boardMemberRepository.findByBoardAndUser(board, user)
                .orElseThrow(UninvitedBoardMemberException::new);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

}

