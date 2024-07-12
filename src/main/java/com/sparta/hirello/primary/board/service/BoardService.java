package com.sparta.hirello.primary.board.service;

import com.sparta.hirello.primary.board.dto.request.BoardRequest;
import com.sparta.hirello.primary.board.dto.request.BoardUserRequest;
import com.sparta.hirello.primary.board.dto.request.BoardUserRoleRequest;
import com.sparta.hirello.primary.board.dto.response.BoardResponse;
import com.sparta.hirello.primary.board.dto.response.BoardUserResponse;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.BoardAuthority;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.board.repository.BoardMemberRepository;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.entity.UserRole;
import com.sparta.hirello.primary.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
     * 보드를 생성 합니다.
     *
     * @param user       인가된 유저 정보
     * @param requestDto 클라이언트에서 요청한 보드 생성 정보
     */
    @Transactional
    public BoardResponse createBoard(User user, BoardRequest requestDto) {

        Board board = Board.of(requestDto, user);

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
        List<BoardMember> boardMemberList = boardMemberRepository.findByUserId(user.getId());

        if (user.getRole().equals(UserRole.ADMIN)) {
            boards = boardRepository.findAll();
            return boards.stream()
                    .map(BoardResponse::of)
                    .collect(Collectors.toList());
        } else {
            return boardMemberList.stream()
                    .map(boardMember -> getBoardEntity(boardMember.getBoard().getBoardId()))
                    .map(BoardResponse::of)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 보드 수정
     *
     * @param user       인가된 유저 정보
     * @param requestDto 클라이언트에서 요청한 보드 수정 정보
     * @param boardId    수정 해야할 보드 정보
     */
    @Transactional
    public BoardResponse updateBoard(User user, BoardRequest requestDto, Long boardId) {

        Board board = getBoardEntity(boardId);

        board.compareUserId(board, user);

        String boardName = requestDto.getBoardName();
        String headline = requestDto.getHeadline();

        board.update(boardName, headline);
        return BoardResponse.of(board);
    }

    /**
     * 보드 삭제
     *
     * @param user    인가된 유저 정보
     * @param boardId 삭제 해야할 보드 정보
     */
    @Transactional
    public void deleteBoard(User user, Long boardId) {
        Board board = getBoardEntity(boardId);

        board.compareUserId(board, user);

        boardRepository.delete(board);
    }

    /**
     * 보드 맴버 초대
     *
     * @param user    인가된 유저 정보
     * @param boardId 삭제 해야할 보드 정보
     * @param request 초대 받은 유저 정보
     */
    public BoardUserResponse boardUserInvite(User user, Long boardId,
            BoardUserRequest request) {
        Board board = getBoardEntity(boardId);
        BoardMember mangerMember = getMemberEntity(user.getId(), board.getBoardId());
        compareManger(mangerMember);
        User visitUser = getUser(request.getUserId());

        boardMemberRepository.save(BoardMember.of(visitUser, board));
        return BoardUserResponse.of(visitUser);
    }

    @Transactional
    public BoardUserResponse updateUserBoardRole(User user, Long boardId,
            BoardUserRoleRequest request) {
        User updateuser = getUser(request.getUserId());
        Board board = getBoardEntity(boardId);

        //변경 진행 해줄 유저 정보
        BoardMember changesMember = getMemberEntity(user.getId(), board.getBoardId());

        //변경 당하는 유저의 정보
        BoardMember updateMember = getMemberEntity(updateuser.getId(), board.getBoardId());

        //변경 시킬려고 하는 유저가 매니저 권한이 있는지 확인
        compareManger(changesMember);

        // 같은 권한으로 변경하는지 확인
        if (updateMember.getBoardAuthority().equals(request.getBoardAuthority())) {
            throw new IllegalArgumentException(
                    "해당 회원의 권한이 이미 " + request.getBoardAuthority() + " 상태입니다.");
        }
        updateMember.updateRole(request.getBoardAuthority());
        return BoardUserResponse.of(updateuser);
    }

    /**
     * 유저 정보를 가지고 옵니다 없으면 Exception
     *
     * @param userId 유저의 고유 ID
     */
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다"));
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
     * 맴버 정보를 가지고 옵니다, 없으면 Exception
     *
     * @param boardId 인가된 유저 정보
     */
    public BoardMember getMemberEntity(Long userId, Long boardId) {
        return boardMemberRepository.findByUserIdAndBoardId(userId
                , boardId).orElseThrow(
                () -> new IllegalArgumentException("맴버에 존재 하지 않는 유저 입니다.")
        );
    }

    /**
     * 맴버 정보를 가지고 옵니다, 없으면 Exception
     *
     * @param boardMember 보드 맴버 정보
     */
    public void compareManger(BoardMember boardMember) {
        if (!boardMember.getBoardAuthority().equals(BoardAuthority.MANAGER)) {
            throw new IllegalArgumentException("매니저만 작업할수 있습니다.");
        }
    }
}

