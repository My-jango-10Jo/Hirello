package com.sparta.hirello.primary.progress.service;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.board.entity.BoardRole;
import com.sparta.hirello.primary.board.repository.BoardMemberRepository;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.progress.dto.request.ProgressCreateRequest;
import com.sparta.hirello.primary.progress.dto.request.ProgressMoveOrderRequest;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.progress.repository.ProgressRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.secondary.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    /**
     * 프로그레스 생성
     */
    @Transactional
    public Progress createProgress(ProgressCreateRequest request, User user) {
        Board board = getBoard(request.getBoardId());
        validateBoardManager(board, user);
        // 이미 존재하는 프로그레스인지 확인
        String title = request.getTitle();
        if (progressRepository.existsByTitle(title)) {
            throw new DuplicateTitleException(title);
        }
        return progressRepository.save(Progress.of(title, board));
    }

    /**
     * 프로그레스 순서 이동
     */
    @Transactional
    public Progress moveProgress(Long progressId, ProgressMoveOrderRequest request, User user) {
        Progress progress = getProgress(progressId);
        Board board = progress.getBoard();
        validateBoardManager(board, user);

        int currentOrder = progress.getOrder();
        int newOrder = request.getNewOrder();

        // 순서가 변하지 않은 경우
        if (currentOrder == newOrder) {
            throw new IllegalArgumentException("현재 위치와 동일한 위치입니다.");
        }
        // 순서가 부적절한 경우
        if (newOrder < 0 || newOrder > board.getProgresses().size()) {
            throw new InvalidOrderException();
        }
        // 해당 프로그레스로 인해 순서가 바뀐 프로그레스들의 order 필드 증감
        if (currentOrder < newOrder) {
            progressRepository.decreaseOrderBetween(board, currentOrder + 1, newOrder);
        } else {
            progressRepository.increaseOrderBetween(board, newOrder, currentOrder - 1);
        }
        // 프로그레스 순서 변경
        progress.updateOrder(newOrder);
        return progress;
    }

    /**
     * 프로그레스 삭제
     */
    @Transactional
    public void deleteProgress(Long progressId, User user) {
        Progress progress = getProgress(progressId);
        Board board = progress.getBoard();
        validateBoardManager(board, user);

        int currentOrder = progress.getOrder();
        progressRepository.delete(progress);

        // 해당 프로그레스보다 뒤에 있는 프로그레스의 순서 당기기
        progressRepository.decreaseOrderBetween(board, currentOrder + 1, board.getProgresses().size());
    }

    private Progress getProgress(Long progressId) {
        return progressRepository.findByIdWithPessimisticLock(progressId)
                .orElseThrow(() -> new ProgressNotFoundException(progressId));
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
    }

    private void validateBoardManager(Board board, User user) {
        BoardMember boardMember = boardMemberRepository.findByBoardAndUserWithPessimisticLock(board, user)
                .orElseThrow(UninvitedBoardMemberException::new);
        if (!boardMember.getBoardRole().equals(BoardRole.MANAGER)) {
            throw new NotBoardManagerException();
        }
    }

}
