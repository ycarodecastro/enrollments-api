package com.example.projectapi.infra.exception.offer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OfferNoAvailableSeatsException extends RuntimeException {

    public OfferNoAvailableSeatsException() {
        super("Nao ha vagas disponiveis para esta oferta.");
    }
}

