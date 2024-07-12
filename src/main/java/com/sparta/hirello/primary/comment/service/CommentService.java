package com.sparta.hirello.primary.comment.service;

import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.comment.dto.CommentRequest;
import com.sparta.hirello.primary.comment.entity.Comment;
import com.sparta.hirello.primary.comment.repository.CommentRepository;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.secondary.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    /**
     * 댓글 작성
     */
    @Transactional
    public Comment createComment(CommentRequest request, User user) {
        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new NullPointerException("존재 하지 않는 카드입니다"));
        Comment comment = Comment.of(request, card, user);
        return commentRepository.save(comment);
    }

    /**
     * 댓글 조회
     */
    public Comment getCardComment(Long cardId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        comment.verifyCard(cardId); // 해당 카드의 댓글이 맞는지 확인
        return comment;
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Comment updateComment(Long commentId, CommentRequest request, User user) {
        Comment comment = getCardComment(request.getCardId(), commentId);
        comment.verifyUser(user.getId()); // 해당 댓글의 작성자가 맞는지 확인
        comment.update(request);
        return comment;
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public Long deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NullPointerException("존재 하지 않는 댓글입니다"));
        comment.verifyUser(user.getId());
        commentRepository.delete(comment);
        return comment.getId();
    }

}
