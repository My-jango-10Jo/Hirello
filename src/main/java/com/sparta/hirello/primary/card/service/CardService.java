package com.sparta.hirello.primary.card.service;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.board.entity.BoardRole;
import com.sparta.hirello.primary.board.repository.BoardMemberRepository;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.card.dto.request.CardCreateRequest;
import com.sparta.hirello.primary.card.dto.request.CardMoveOrderRequest;
import com.sparta.hirello.primary.card.dto.request.CardUpdateRequest;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.repository.CardRepository;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.progress.repository.ProgressRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.repository.UserRepository;
import com.sparta.hirello.secondary.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;
    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;

    /**
     * 카드 생성
     */
    @Transactional
    public Card createCard(CardCreateRequest request, User user) {
        Progress progress = getProgress(request.getProgressId());
        validateInvitedUser(progress.getBoard(), user);
        // 이미 존재하는 카드인지 확인
        String title = request.getTitle();
        if (cardRepository.existsByTitle(title)) {
            throw new DuplicateTitleException(title);
        }
        User worker = getUser(request.getWorkerId());
        return cardRepository.save(Card.of(request, progress, worker, user));
    }

    /**
     * 해당 보드의 카드 목록 조회 - 작업자별, 프로그레스별
     */
    public Page<Card> getCards(Long boardId, Long workerId, Long progressId, User user, Pageable pageable) {
        Board board = getBoard(boardId);
        validateInvitedUser(board, user);
        // 해당 보드의 전체 카드 조회
        if (workerId == null && progressId == null) {
            return cardRepository.findByBoard(board, pageable);
        }
        // 작업자별 조회
        if (progressId == null) {
            User worker = getUser(workerId);
            return cardRepository.findByBoardAndWorker(board, worker, pageable);
        }
        // 프로그레스별 조회
        if (workerId == null) {
            Progress progress = getProgress(progressId);
            return cardRepository.findByBoardAndProgress(board, progress, pageable);
        }
        // 작업자, 프로그레스별 조회
        User worker = getUser(workerId);
        Progress progress = getProgress(progressId);
        return cardRepository.findByBoardAndWorkerAndProgress(board, worker, progress, pageable);
    }

    /**
     * 카드 이동
     */
    @Transactional
    public Card moveCard(Long cardId, CardMoveOrderRequest request, User user) {
        Card card = getCard(cardId);
        Progress progress = card.getProgress();
        Board board = progress.getBoard();
        verifyCardAuthority(card, user);

        int currentOrder = card.getOrder();
        int newOrder = request.getNewOrder();

        if (request.getNewProgressId() == null) {
            moveCardWithinProgress(currentOrder, newOrder, progress, card); // 프로그레스 내에서 이동
        } else {
            Progress newProgress = getProgress(request.getNewProgressId());
            moveCardToAnotherProgress(currentOrder, newOrder, progress, newProgress, card); // 다른 프로그레스로 이동
        }
        return card;
    }

    /**
     * 카드 수정
     */
    @Transactional
    public Card updateCard(Long cardId, CardUpdateRequest request, User user) {
        Card card = getCard(cardId);
        verifyCardAuthority(card, user);
        // 작업자 수정
        User worker = null;
        if (request.getWorkerId() != null) {
            worker = getUser(request.getWorkerId());
        }
        card.update(request, worker);
        return card;
    }

    /**
     * 카드 삭제
     */
    @Transactional
    public void deleteCard(Long cardId, User user) {
        Card card = getCard(cardId);
        verifyCardAuthority(card, user);

        int currentOrder = card.getOrder();
        cardRepository.delete(card);

        // 해당 카드보다 뒤에 있는 카드들의 순서 당기기
        Progress progress = card.getProgress();
        cardRepository.decreaseOrderBetween(progress, currentOrder + 1, progress.getCards().size());
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
    }

    private Progress getProgress(Long progressId) {
        return progressRepository.findById(progressId)
                .orElseThrow(() -> new ProgressNotFoundException(progressId));
    }

    private Card getCard(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    /**
     * 해당 보드에 초대된 사용자인지 검증
     */
    private void validateInvitedUser(Board board, User user) {
        if (!boardMemberRepository.existsByBoardAndUser(board, user)) {
            throw new UninvitedBoardMemberException();
        }
    }

    /**
     * 해당 카드에 대한 권한이 있는 사용자인지 검증
     */
    private void verifyCardAuthority(Card card, User user) {
        Board board = card.getProgress().getBoard();
        BoardMember boardMember = boardMemberRepository.findByBoardAndUser(board, user)
                .orElseThrow(UninvitedBoardMemberException::new);

        if (boardMember.getBoardRole().equals(BoardRole.USER)) {
            if (!card.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("해당 카드에 대한 권한이 없습니다.");
            }
        }
    }

    /**
     * 프로그레스 내에서 카드 이동
     */
    private void moveCardWithinProgress(int currentOrder, int newOrder, Progress progress, Card card) {
        // 순서가 변하지 않은 경우
        if (currentOrder == newOrder) {
            throw new IllegalArgumentException("현재 위치와 동일한 위치입니다.");
        }
        // 순서가 부적절한 경우
        if (newOrder < 0 || newOrder > progress.getCards().size()) {
            throw new InvalidOrderException();
        }
        // 해당 카드로 인해 순서가 바뀐 카드들의 order 필드 증감
        if (currentOrder < newOrder) {
            cardRepository.decreaseOrderBetween(progress, currentOrder + 1, newOrder);
        } else {
            cardRepository.increaseOrderBetween(progress, newOrder, currentOrder - 1);
        }
        card.updateOrder(newOrder);
    }

    /**
     * 다른 프로그레스로 카드 이동
     */
    private void moveCardToAnotherProgress(int currentOrder, int newOrder, Progress progress, Progress newProgress, Card card) {
        // 순서가 부적절한 경우
        if (newOrder < 0 || newOrder > newProgress.getCards().size()) {
            throw new InvalidOrderException();
        }
        // 기존 프로그레스의 카드 순서 조정
        cardRepository.decreaseOrderBetween(progress, currentOrder + 1, progress.getCards().size());
        // 새 프로그레스의 카드 순서 조정
        cardRepository.increaseOrderBetween(newProgress, newOrder, newProgress.getCards().size());
        // 카드의 프로그레스와 순서 업데이트
        card.updateProgress(newProgress);
        card.updateOrder(newOrder);
    }

}