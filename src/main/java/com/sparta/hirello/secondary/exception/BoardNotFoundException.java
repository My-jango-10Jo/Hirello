package com.sparta.hirello.secondary.exception;

import jakarta.persistence.EntityNotFoundException;

public class BoardNotFoundException extends EntityNotFoundException {

    public BoardNotFoundException(Long boardId) {
        super("Board with id " + boardId + " not found");
    }

}
