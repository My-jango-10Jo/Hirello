package com.sparta.hirello.primary.card.service;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.board.repository.BoardMemberRepository;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.card.dto.request.*;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.repository.CardRepository;
import com.sparta.hirello.primary.column.entity.Columns;
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

    @Transactional
    public Card createCard(User loginUser, CreateCardRequest request) {

        Board basicCheckedBoard = getBoard(request.getBoardId(),loginUser.getUsername());
        Columns existColumn = existColumn(basicCheckedBoard, request.getColumnId());

        //할당된 작업자가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        User worker = getInvitedUser(basicCheckedBoard.getBoardId(), request.getWorkerId());

        Card newCard = Card.of(request, loginUser, worker, existColumn);
        return cardRepository.save(newCard);
    }


    public Board getAllCardOfBoard(User loginUser, Long boardId) {

        return getBoard(boardId,loginUser.getUsername());
    }

    public List<Card> getCardOfSpecificWorker(User loginUser, CardOfSpecificWorkerRequest request) {

        Board basicCheckedBoard = getBoard(request.getBoardId(),loginUser.getUsername());

        //작업자가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        User worker = getInvitedUser(basicCheckedBoard.getBoardId(), request.getWorkerId());

        List<Card> CardListOfSpecificWorker = cardRepository.findCardByWorkerId(worker.getId());
        if (CardListOfSpecificWorker.isEmpty()) {
            throw new EntityNotFoundException("작업자에게 할당된 카드가 없습니다.");
        }
        return CardListOfSpecificWorker;
    }

    public List<Card> getCardOfColumn(User loginUser, CardOfColumnRequest request) {

        //board 존재 확인 및 추출
        Board basicCheckedBoard = getBoard(request.getBoardId(),loginUser.getUsername());
        Columns existColumn = existColumn(basicCheckedBoard, request.getColumnId());

        List<Card> cardListOfColumn = cardRepository.findCardByColumnsColumnId(existColumn.getColumnId());
        if (cardListOfColumn.isEmpty()) {
            throw new EntityNotFoundException("컬럼이 비어있습니다.");
        }
        return cardListOfColumn;
    }

    @Transactional
    public Card updateCard(User loginUser, Long cardId, CardUpdateRequest request) {

        Board basicCheckedBoard = getBoard(request.getBoardId(),loginUser.getUsername());

        //수정할 카드 확인
        Card targetCard = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("삭제된 카드입니다.")
        );

        //수정할 카드의 컬럼 존재
        basicCheckedBoard.checkColumn(request.getColumnId());

        //작업자가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        User worker = request.getWorkerName() != null ?
                getInvitedUser(basicCheckedBoard.getBoardId(), getUserId(request.getWorkerName()))
                : null;

        Card updatedCard = targetCard.updateCard(request, worker);
        cardRepository.save(updatedCard);
        return updatedCard;
    }

    @Transactional
    public Card updateCardColumn(User loginUser, Long cardId, CardUpdateOnlyColumnRequest request) {

        Board basicCheckedBoard = getBoard(request.getBoardId(),loginUser.getUsername());
        Columns existColumn = existColumn(basicCheckedBoard, request.getColumnId());

        //수정할 카드 확인
        Card targetCard = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("삭제된 카드입니다.")
        );

        Card updatedCard = targetCard.updateCardColumn(existColumn);
        cardRepository.save(updatedCard);
        return updatedCard;
    }

    @Transactional
    public void deleteCard(User loginUser, Long cardId, CardDeleteRequest request) {

        Board basicCheckedBoard = getBoard(request.getBoardId(),loginUser.getUsername());
        basicCheckedBoard.checkColumn(request.getColumnId());

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
        checkMember(checkedBoard.getBoardId(), loginUserId);

        return checkedBoard;
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findBoardByBoardId(boardId)
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

    private Columns existColumn(Board board, Long columnId) { //getColumn 으로

        return board.getColumnsList().stream()
                .filter(column -> column.getColumnId().equals(columnId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("컬럼이 존재하지 않습니다"));
    }

    private User getInvitedUser(Long boardId, Long workerId) {
        BoardMember boardMember = boardMemberRepository.findByUserIdAndBoardId(workerId, boardId)
                .orElseThrow(() -> new EntityNotFoundException("보드멤버가 아닙니다."));

        return boardMember.getUser();
    }
}
