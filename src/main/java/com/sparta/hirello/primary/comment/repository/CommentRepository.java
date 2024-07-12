package com.sparta.hirello.primary.comment.repository;

import com.sparta.hirello.primary.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
