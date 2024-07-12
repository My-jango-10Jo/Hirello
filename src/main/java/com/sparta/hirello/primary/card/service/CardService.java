package com.sparta.hirello.primary.card.service;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.card.dto.request.*;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.repository.CardRepository;
import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.repository.UserRepository;
import com.sparta.hirello.secondary.exception.UserNotFoundException;
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

    @Transactional
    public Card createCard(User loginUser, CreateCardRequest request) {

        Board basicCheckedBoard = basicCheck(request.getBoardId(),loginUser.getUsername());
        Columns existColumn = existColumn(basicCheckedBoard, request.getColumnId());

        //할당된 작업자가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        User worker = invitedUser(basicCheckedBoard.getBoardId(), request.getWorkerId());

        Card newCard = Card.of(request, loginUser, worker, existColumn);

        return cardRepository.save(newCard);
    }


    public Board getAllCardOfBoard(User loginUser, Long boardId) {

        //board 존재 확인 및 추출
        Board checkedBoard = checkBoard(boardId);

        //loginUser 가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        Long loginUserId = getUserId(loginUser.getUsername());
        checkMember(checkedBoard.getBoardId(), loginUserId);

        return checkedBoard;
    }

    public List<Card> getCardOfSpecificWorker(User loginUser, CardOfSpecificWorkerRequest request) {

        //board 존재 확인 및 추출
        Board checkedBoard = checkBoard(request.getBoardId());

        //loginUser 가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        Long loginUserId = getUserId(loginUser.getUsername());
        checkMember(checkedBoard.getBoardId(), loginUserId);

        //작업자가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        User worker = invitedUser(checkedBoard.getBoardId(), request.getWorkerId());

        List<Card> CardListOfSpecificWorker = cardRepository.findCardByWorkerId(worker.getId());
        if (CardListOfSpecificWorker.isEmpty()) {
            throw new EntityNotFoundException("작업자에게 할당된 카드가 없습니다.");
        }

        return CardListOfSpecificWorker;
    }

    public List<Card> getCardOfColumn(User loginUser, CardOfColumnRequest request) {

        //board 존재 확인 및 추출
        Board checkedBoard = checkBoard(request.getBoardId());

        //loginUser 가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        Long loginUserId = getUserId(loginUser.getUsername());
        checkMember(checkedBoard.getBoardId(), loginUserId);

        Columns existColumn = existColumn(checkedBoard, request.getColumnId());

        List<Card> cardListOfColumn = cardRepository.findCardByColumnsColumnId(existColumn.getColumnId());
        if (cardListOfColumn.isEmpty()) {
            throw new EntityNotFoundException("컬럼이 비어있습니다.");
        }

        return cardListOfColumn;
    }

    @Transactional
    public Card updateCard(User loginUser, Long cardId, CardUpdateRequest request) {

        //board 존재 확인 및 추출
        Board checkedBoard = checkBoard(request.getBoardId());

        //loginUser 가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        Long loginUserId = getUserId(loginUser.getUsername());
        checkMember(checkedBoard.getBoardId(), loginUserId);

        //수정할 카드 확인
        Card targetCard = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("삭제된 카드입니다.")
        );
        //수정할 카드의 컬럼 존재
        checkColumn(checkedBoard, request.getColumnId());

        //작업자가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        User worker = request.getWorkerName() != null ?
                invitedUser(checkedBoard.getBoardId(), getUserId(request.getWorkerName()))
                : null;

        Card updatedCard = targetCard.updateCard(request, worker);
        cardRepository.save(updatedCard);
        return updatedCard;
    }

    @Transactional
    public Card updateCardColumn(User loginUser, Long cardId, CardUpdateOnlyColumnRequest request) {
        //board 존재 확인 및 추출
        Board checkedBoard = checkBoard(request.getBoardId());

        //loginUser 가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        Long loginUserId = getUserId(loginUser.getUsername());
        checkMember(checkedBoard.getBoardId(), loginUserId);

        //수정할 카드 확인
        Card targetCard = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("삭제된 카드입니다.")
        );
        //수정할 카드의 컬럼 존재
        Columns existColumn = existColumn(checkedBoard, request.getColumnId());

        Card updatedCard = targetCard.updateCardColumn(existColumn);
        cardRepository.save(updatedCard);

        return updatedCard;
    }

    @Transactional
    public void deleteCard(User loginUser, Long cardId, CardDeleteRequest request) {

        Board basicCheckedBoard = basicCheck(request.getBoardId(),loginUser.getUsername());
        Columns existColumn = existColumn(basicCheckedBoard, request.getColumnId());

        Card targetCard = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("이미 삭제된 카드입니다.")
        );
        cardRepository.delete(targetCard);
    }


    private Board basicCheck(Long boardId,String username) {
        //board 존재 확인 및 추출
        Board checkedBoard = checkBoard(boardId);

        //loginUser 가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        Long loginUserId = getUserId(username);
        checkMember(checkedBoard.getBoardId(), loginUserId);

        return checkedBoard;
    }

    private Board checkBoard(Long boardId) {
        return boardRepository.findBoardByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("보드가 존재하지 않습니다")); //임시 예외 처리
    }

    private Long getUserId(String username) {
        return userRepository.findUserIdByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("찾을 수 없는 사용자입니다.")); //임시 예외 처리
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void checkMember(Long boardId, Long userId) {
        boolean isMember = boardMemberRepository.findMemberByBoardIdAndUserId(boardId, userId);
        if (!isMember) {
            throw new NoAuthorityExcpetion("Card 생성 권한이 없습니다");
        }
    }

    private Columns existColumn(Board board, Long columnId) {

        return board.getColumnsList().stream()
                .filter(column -> column.getColumnId().equals(columnId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("컬럼이 존재하지 않습니다"));
    }

    private void checkColumn(Board board, Long columnId) {

        boolean columnExist = board.getColumnsList().stream()
                .anyMatch(column -> column.getColumnId().equals(columnId));

        if (!columnExist) {
            throw new EntityNotFoundException("컬럼이 존재하지 않습니다.");
        }

    }

    private User invitedUser(Long boardId, Long workerId) {
        return boardMemberRepository.findMemberByBoardIdAndUserId(boardId, workerId)
                .orElseThrow(() -> new EntityNotFoundException("보드멤버가 아닙니다."));
    }
}
