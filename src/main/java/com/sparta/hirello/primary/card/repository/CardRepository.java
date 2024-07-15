package com.sparta.hirello.primary.card.repository;

import com.sparta.hirello.primary.card.entity.Card;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Lock(LockModeType.WRITE)
    @Query("select c from Card c where c.id = :id")
    Optional<Card> findByIdWithPessimisticLock(Long id);

    List<Card> findByWorkerId(Long id);

    List<Card> findByProgressId(Long progressId);
}

