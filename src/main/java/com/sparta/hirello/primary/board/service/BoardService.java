package com.sparta.hirello.primary.board.service;

import com.sparta.hirello.primary.board.dto.request.BoardRequest;
import com.sparta.hirello.primary.board.dto.request.BoardUserVisitRequest;
import com.sparta.hirello.primary.board.dto.response.BoardResponse;
import com.sparta.hirello.primary.board.dto.response.BoardUserVisitResponse;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.BoardAuthority;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.board.repository.BoardMemberRepository;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.entity.UserRole;
import com.sparta.hirello.primary.user.repository.UserRepository;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;
    private final UserRepository userRepository;


    /**
     * 보드를 생성 합니다.
     *
     * @param userDetails 인가된 유저 정보
     * @param requestDto  클라이언트에서 요청한 보드 생성 정보
     */
    @Transactional
    public BoardResponse createBoard(
            UserDetailsImpl userDetails,
            BoardRequest requestDto
    ) {
        User user = userDetails.getUser();
        Board board = Board.of(requestDto, user);

        //compareUserRole(user);
        boardMemberRepository.save(BoardMember.of(user, board, BoardAuthority.MANAGER));
        boardRepository.save(board);
        return BoardResponse.of(board);
    }

    /**
     * 보드 목록들을 조회 합니다.
     *
     * @param user 인가된 유저 정보 일단 Admin 권한을 가진 인원만 전체 조회로 구현 TeamEntity 관련 구현이 완료 되면 로직 추가
     */
    public List<BoardResponse> getBoardList(User user) {

        List<Board> boards;
        List<BoardResponse> responseDtos = new ArrayList<>();
        List<BoardMember> boardMemberList = boardMemberRepository.findByUserId(user.getId());

        if (user.getRole().equals(UserRole.ADMIN)) {
            boards = boardRepository.findAll();
            for (Board board : boards) {
                responseDtos.add(BoardResponse.of(board));
            }
        } else {
            for (BoardMember boardMember : boardMemberList) {
                Board board = boardRepository
                        .findById(boardMember.getBoard().getBoardId()).orElseThrow(
                                () -> new IllegalArgumentException("보드가 존재 하지 않습니다.")
                        );
                responseDtos.add(BoardResponse.of(board));
            }
        }

        return responseDtos;
    }

    /**
     * 보드 수정
     *
     * @param userDetails 인가된 유저 정보
     * @param requestDto  클라이언트에서 요청한 보드 수정 정보
     * @param boardId     수정 해야할 보드 정보
     */
    @Transactional
    public BoardResponse updateBoard(
            UserDetailsImpl userDetails,
            BoardRequest requestDto, Long boardId) {

        Board board = getBoardEntity(boardId);

        User user = userDetails.getUser();

        compareUserId(board, user);

        String boardName = requestDto.getBoardName();
        String headline = requestDto.getHeadline();

        board.update(boardName, headline);
        return BoardResponse.of(board);
    }

    public void deleteBoard(UserDetailsImpl userDetails, Long boardId) {
        Board board = getBoardEntity(boardId);
        User user = userDetails.getUser();

        compareUserId(board, user);

        boardRepository.delete(board);
    }

    /**
     * 보드 정보가 있는지 확인 후 있으면 보드 정보 저장, 없으면 Exception
     *
     * @param boardId 인가된 유저 정보
     */
    public Board getBoardEntity(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("보드가 존재 하지 않습니다.")
        );
    }

    /**
     * 보드생성자와 작업 요청한 유저 정보가 맞는지 확인 합니다, 틀리면 Exception
     *
     * @param board board에 getUser().getId()의 정보
     * @param user  요청자의 유저 정보
     */
    public void compareUserId(Board board, User user) {
        if (!user.getId().equals(board.getUser().getId())) {
            throw new IllegalArgumentException("보드 생성자가 아닙니다.");
        }
    }

    /**
     * 유저의 권한 정보가 매니저 인지 확인,매니저가 아니면 Exception
     *
     * @param user 요청자의 유저 정보
     */
    public void compareUserRole(User user) {
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    public BoardUserVisitResponse boardUserVisit(User user, Long boardId,
            BoardUserVisitRequest request) {
        Board board = getBoardEntity(boardId);
        BoardMember mangerMember = boardMemberRepository.findByUserIdAndBoardId(user.getId(),
                board.getBoardId()).orElseThrow(() -> new IllegalArgumentException("없는 맴버 입니다."));

        if (!mangerMember.getBoardAuthority().equals(BoardAuthority.MANAGER)) {
            throw new IllegalArgumentException("초대 권한이 없습니다.");
        }

        User visitUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        boardMemberRepository.save(BoardMember.of(visitUser, board));
        return BoardUserVisitResponse.of(visitUser);
    }
}
