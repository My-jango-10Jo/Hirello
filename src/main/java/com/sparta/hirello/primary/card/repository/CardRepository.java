package com.sparta.hirello.primary.card.repository;

import com.sparta.hirello.primary.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByWorkerId(Long id);

    List<Card> findByProgressId(Long progressId);
}

