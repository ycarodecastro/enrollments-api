package com.example.projectapi.infra.scheduler;

import com.example.projectapi.domain.offer.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class OfferCleanupScheduler {

    private final OfferRepository offerRepository;

    // Roda todos os dias às 00:00 (meia-noite)
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredOffers() {
        log.info("Iniciando limpeza de ofertas expiradas...");
        offerRepository.deleteByEndDateLessThan(LocalDate.now());
        log.info("Limpeza concluída.");
    }
}