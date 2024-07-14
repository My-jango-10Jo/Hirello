package com.sparta.hirello.primary.card.service;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.board.repository.BoardMemberRepository;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.card.dto.request.CardDeleteRequest;
import com.sparta.hirello.primary.card.dto.request.CardUpdateOnlyProgressRequest;
import com.sparta.hirello.primary.card.dto.request.CardUpdateRequest;
import com.sparta.hirello.primary.card.dto.request.CreateCardRequest;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.repository.CardRepository;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.repository.UserRepository;
import com.sparta.hirello.secondary.exception.NoAuthorityException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CardRepository cardRepository;
    private final BoardMemberRepository boardMemberRepository;

    /**
     * Card 생성
     */
    @Transactional
    public Card createCard(User loginUser, CreateCardRequest request) {

        Board basicCheckedBoard = getBoard(request.getBoardId(),loginUser.getUsername());
        Progress existProgress = existProgress(basicCheckedBoard, request.getProgressId());

        //할당된 작업자가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        User worker = getInvitedUser(basicCheckedBoard.getId(), request.getWorkerId());

        Card newCard = Card.of(request, loginUser, worker, existProgress);
        return cardRepository.save(newCard);
    }

    /**
     * Board 의 모든 Card 조회
     */

    public Board getAllCardOfBoard(User loginUser, Long boardId) {

        return getBoard(boardId,loginUser.getUsername());
    }

    /**
     * 특정 Worker 의 모든 Card 조회
     */
    public List<Card> getCardOfSpecificWorker(User loginUser, Long boardId, Long workerId) {

        Board basicCheckedBoard = getBoard(boardId,loginUser.getUsername());

        //작업자가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        User worker = getInvitedUser(basicCheckedBoard.getId(), workerId);

        List<Card> CardListOfSpecificWorker = cardRepository.findByWorkerId(worker.getId());
        if (CardListOfSpecificWorker.isEmpty()) {
            throw new EntityNotFoundException("작업자에게 할당된 카드가 없습니다.");
        }
        return CardListOfSpecificWorker;
    }

    /**
     * 특정 Progress 의 모든 Card 조회
     */
    public List<Card> getCardOfProgress(User loginUser, Long boardId, Long progressId) {

        //board 존재 확인 및 추출
        Board basicCheckedBoard = getBoard(boardId,loginUser.getUsername());
        Progress existProgress = existProgress(basicCheckedBoard, progressId);

        List<Card> cardListOfProgress = cardRepository.findByProgressId(existProgress.getId());
        if (cardListOfProgress.isEmpty()) {
            throw new EntityNotFoundException("컬럼이 비어있습니다.");
        }
        return cardListOfProgress;
    }

    /**
     * Card 내용 업데이트(Progress 제외)
     */
    @Transactional
    public Card updateCard(User loginUser, Long cardId, CardUpdateRequest request) {

        Board basicCheckedBoard = getBoard(request.getBoardId(),loginUser.getUsername());

        //수정할 카드 확인
        Card targetCard = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("삭제된 카드입니다.")
        );

        //수정할 카드의 컬럼 존재
        basicCheckedBoard.checkProgress(request.getProgressId());

        //작업자가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        User worker = request.getWorkerName() != null ?
                getInvitedUser(basicCheckedBoard.getId(), getUserId(request.getWorkerName()))
                : null;

        Card updatedCard = targetCard.updateCard(request, worker);
        cardRepository.save(updatedCard);
        return updatedCard;
    }

    /**
     * Card 의 Progress 변경(이동)
     */
    @Transactional
    public Card updateCardProgress(User loginUser, Long cardId, CardUpdateOnlyProgressRequest request) {

        Board basicCheckedBoard = getBoard(request.getBoardId(),loginUser.getUsername());
        Progress existProgress = existProgress(basicCheckedBoard, request.getProgressId());

        //수정할 카드 확인
        Card targetCard = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("삭제된 카드입니다.")
        );

        //카드끼리의 위치 정하기 로직


        Card updatedCard = targetCard.updateCardProgress(existProgress);

        cardRepository.save(updatedCard);
        return updatedCard;
    }

    /**
     * Card 삭제
     */
    @Transactional
    public void deleteCard(User loginUser, Long cardId, CardDeleteRequest request) {

        Board basicCheckedBoard = getBoard(request.getBoardId(),loginUser.getUsername());
        basicCheckedBoard.checkProgress(request.getProgressId());

        Card targetCard = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("이미 삭제된 카드입니다.")
        );
        cardRepository.delete(targetCard);
    }


    private Board getBoard(Long boardId, String username) { //메서드명 정정 //check validation
        //board 존재 확인 및 추출
        Board checkedBoard = getBoard(boardId);

        //loginUser 가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        Long loginUserId = getUserId(username);
        checkMember(checkedBoard.getId(), loginUserId);

        return checkedBoard;
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("보드가 존재하지 않습니다")); //임시 예외 처리
    }

    private Long getUserId(String username) {
        return userRepository.findUserIdByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("찾을 수 없는 사용자입니다.")); //임시 예외 처리
    }

    private void checkMember(Long boardId, Long userId) {
        boardMemberRepository.findByUserIdAndBoardId(userId, boardId).orElseThrow(
                () -> new NoAuthorityException("Card 생성 권한이 없습니다")
        );
    }

    private Progress existProgress(Board board, Long progressId) { //getProgress 으로

        return board.getProgressList().stream()
                .filter(progress -> progress.getId().equals(progressId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("컬럼이 존재하지 않습니다"));
    }

    private User getInvitedUser(Long boardId, Long workerId) {
        BoardMember boardMember = boardMemberRepository.findByUserIdAndBoardId(workerId, boardId)
                .orElseThrow(() -> new EntityNotFoundException("보드멤버가 아닙니다."));

        return boardMember.getUser();
    }
}
