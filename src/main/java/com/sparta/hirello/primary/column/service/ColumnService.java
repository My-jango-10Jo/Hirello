package com.sparta.hirello.primary.column.service;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.jpa.BoardRepository;
import com.sparta.hirello.primary.column.dto.request.ColumnRequest;
import com.sparta.hirello.primary.column.dto.response.ColumnResponse;
import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.column.repository.ColumnRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.repository.UserRepository;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.exception.BoardNotFoundException;
import com.sparta.hirello.secondary.exception.ColumnDuplicatedException;
import com.sparta.hirello.secondary.exception.PermissionDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

@Service
@RequiredArgsConstructor
public class ColumnService {
    private final ColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public ResponseEntity<CommonResponse<?>> createColumn(ColumnRequest request, User user){
        Board board=boardRepository.findByBoardId(request.getBoardId()).orElseThrow(
                ()-> new BoardNotFoundException("찾는 보드 없음"));
        //Columns columns=new Columns(request.getColumnName(),user, board);
        Columns columns= Columns.of(request.getColumnName(), user, board);

      columnRepository.findByColumnName(request.getColumnName()).ifPresentOrElse(col->{
          throw new ColumnDuplicatedException("이미 존재하는 컬럼");
      },
      ()->{
          columnRepository.save(columns);
      });
        return getResponseEntity(ColumnResponse.of(columns), "컬럼 생성 성공");
    }

    public ResponseEntity<CommonResponse<?>> deleteColumn(Long id, User user) {
        Columns columns=columnRepository.findById(id).orElseThrow(()-> new NullPointerException("찾는 컬럼 없음"));
        if(!columns.getUser().getId().equals(user.getId())){
            throw new PermissionDeniedException("실행 권한 없음");
        }
        columnRepository.delete(columns);
        return getResponseEntity(1, "컬럼 삭제 완료");
    }
}
