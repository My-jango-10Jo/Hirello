package com.sparta.hirello.secondary.exception;

import jakarta.persistence.EntityNotFoundException;

public class CardNotFoundException extends EntityNotFoundException {

    public CardNotFoundException(Long cardId) {
        super("Card with id " + cardId + " not found");
    }
}
