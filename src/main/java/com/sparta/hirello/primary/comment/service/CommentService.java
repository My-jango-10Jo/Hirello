package com.sparta.hirello.primary.comment.service;

import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.repository.CardRepository;
import com.sparta.hirello.primary.comment.dto.CommentRequest;
import com.sparta.hirello.primary.comment.dto.CommentResponse;
import com.sparta.hirello.primary.comment.entity.Comment;
import com.sparta.hirello.primary.comment.repository.CommentRepository;
import com.sparta.hirello.primary.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    CommentRepository commentRepository;
    CardRepository cardRepository;

    // 댓글 생성
    public void createComment(Long cardId, Long boardId, CommentRequest request, User user) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NullPointerException("존재 하지 않는 카드입니다"));

        Comment comment = new Comment(request, card, user);
        commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, Long cardId, CommentRequest request, User user) {
        Comment comment = findComment(commentId);

        if (comment.getUser().getId().equals(user.getId())) {
            comment.update(request);
        } else {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다. 댓글 수정 권한이 없습니다.");
        }
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, Long boardId, User user) {
        Comment comment = findComment(commentId);

        if (comment.getUser().getId().equals(user.getId())) {
            commentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다. 댓글 삭제 권한이 없습니다.");
        }
    }

    // 댓글 조회
    public List<CommentResponse> getComments(Long cardId) {
        findCard(cardId);
        List<Comment> commentList = commentRepository.findByCardCardId(cardId);
        return convertToDtoList(commentList);
    }

    private List<CommentResponse> convertToDtoList(List<Comment> commentList) {
        List<CommentResponse> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponse(comment));
        }
        return commentResponseDtoList;
    }

    public Card findCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NullPointerException("없는 카드입니다."));
        return card;
    }

    // 분리
    public Comment findComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NullPointerException("존재 하지 않는 댓글입니다"));

        return comment;
    }
}
