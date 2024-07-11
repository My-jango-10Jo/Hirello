package com.sparta.hirello.primary.comment.repository;

import com.sparta.hirello.primary.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    List<Comment> findByCardCardId(Long cardId);
}
