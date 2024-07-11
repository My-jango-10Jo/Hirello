package com.sparta.hirello.primary.comment.service;

import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.repository.CardRepository;
import com.sparta.hirello.primary.comment.dto.CommentRequest;
import com.sparta.hirello.primary.comment.entity.Comment;
import com.sparta.hirello.primary.comment.repository.CommentRepository;
import com.sparta.hirello.primary.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    CommentRepository commentRepository;
    CardRepository cardRepository;

    // 댓글 생성 Service
    public void createComment(Long id, CommentRequest request, User user) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("존재 하지 않는 카드입니다"));

        Comment comment = new Comment(request, card, user);
        commentRepository.save(comment);
    }
}
