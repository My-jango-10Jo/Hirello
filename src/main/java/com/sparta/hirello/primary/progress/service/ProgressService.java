package com.sparta.hirello.primary.progress.service;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.repository.BoardRepository;
import com.sparta.hirello.primary.progress.dto.request.ProgressRequest;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.progress.repository.ProgressRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.repository.UserRepository;
import com.sparta.hirello.secondary.exception.BoardNotFoundException;
import com.sparta.hirello.secondary.exception.ProgressDuplicatedException;
import com.sparta.hirello.secondary.exception.PermissionDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public Progress createProgress(ProgressRequest request, User user){
        Board board=boardRepository.findById(request.getBoardId()).orElseThrow(
                ()-> new BoardNotFoundException("찾는 보드 없음"));
        //Columns columns=new Columns(request.getColumnName(),user, board);
        Progress progress = Progress.of(request.getProgressName(), user, board);

      progressRepository.findByProgressName(request.getProgressName()).ifPresentOrElse(col->{
          throw new ProgressDuplicatedException("이미 존재하는 컬럼");
      },
      ()->{
          progressRepository.save(progress);
      });
        return progress;
    }

    public void deleteProgress(Long id, User user) {
        Progress progress = progressRepository.findById(id).orElseThrow(()-> new NullPointerException("찾는 컬럼 없음"));
        if(!progress.getUser().getId().equals(user.getId())){
            throw new PermissionDeniedException("실행 권한 없음");
        }
        progressRepository.delete(progress);
    }
}
