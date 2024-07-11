package com.sparta.hirello.primary.comment.service;

import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.repository.CardRepository;
import com.sparta.hirello.primary.comment.dto.CommentRequest;
import com.sparta.hirello.primary.comment.entity.Comment;
import com.sparta.hirello.primary.comment.repository.CommentRepository;
import com.sparta.hirello.primary.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    CommentRepository commentRepository;
    CardRepository cardRepository;

    // 댓글 생성
    public void createComment(Long id, CommentRequest request, User user) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("존재 하지 않는 카드입니다"));

        Comment comment = new Comment(request, card, user);
        commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long cardId, Long commentId, CommentRequest request, User user) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NullPointerException("존재 하지 않는 카드입니다"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("존재 하지 않는 댓글입니다"));
        //todo: user 검증
        if (card.getCardId() == comment.getCard().getCardId()) {
            comment.update(request);
        } else {
            throw new IllegalArgumentException("선택한 카드는 존재하지 않습니다.");
        }
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("존재 하지 않는 댓글입니다"));
        commentRepository.delete(comment);
    }
}
