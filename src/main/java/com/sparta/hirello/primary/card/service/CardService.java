package com.sparta.hirello.primary.card.service;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.card.dto.request.CreateCardRequest;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.repository.CardRepository;
import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.repository.UserRepository;
import com.sparta.hirello.secondary.exception.UserNotFoundException;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CardRepository cardRepository;
    private final BoardMemberRepository boardMemberRepository;

    public Card createCard(UserDetailsImpl userDetails, CreateCardRequest request) {

        //board 존재 확인 및 추출
        Board checkedBoard = checkBoard(request.getBoardId());

        //loginUser 의 Id & 객체 추출
        Long loginUserId = getUserId(userDetails.getUsername());
        User loginUser = getUser(loginUserId);

        //loginUser 가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        checkMember(checkedBoard.getBoardId(), loginUserId);

        //column 존재 확인
        Columns existColumn = isExistColumn(checkedBoard, request);

        //할당된 작업자가 해당 board 의 Manager 또는 초대받은 유저인지 확인
        User worker = boardMemberRepository.findMemberByBoardIdandUserId(checkedBoard.getBoardId(), request.getWorkerId())
                .orElseThrow(() -> new EntityNotFoundException("보드멤버가 아닙니다."));

        Card newCard = Card.of(request, loginUser, worker, existColumn);

        return cardRepository.save(newCard);
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

    private Columns isExistColumn(Board board, CreateCardRequest request) {

        return board.getColumnsList().stream()
                .filter(column -> column.getColumnId().equals(request.getColumnId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("컬럼이 존재하지 않습니다"));
    }

    private void checkMember(Long boardId, Long userId) {
        boolean isMember = boardMemberRepository.findMemberByBoardIdandUserId(boardId, userId);
        if (!isMember) {
            throw new NoAuthorityExcpetion("Card 생성 권한이 없습니다");
        }
    }
}
