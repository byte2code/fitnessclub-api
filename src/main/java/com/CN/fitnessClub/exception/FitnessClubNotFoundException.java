package com.CN.fitnessClub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FitnessClubNotFoundException extends RuntimeException {

    public FitnessClubNotFoundException(String message) {
        super(message);
    }
}
