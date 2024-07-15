package com.sparta.hirello.primary.card.repository;

import com.sparta.hirello.primary.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {

    boolean existsByTitle(String title);

}

