package it.epicode.Progettosettimanale_back_end_S7_L5.prenotazioni;

import it.epicode.Progettosettimanale_back_end_S7_L5.auth.AppUser;
import it.epicode.Progettosettimanale_back_end_S7_L5.eventi.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByUtente(AppUser utente);
    boolean existsByEventoAndUtente(Evento evento, AppUser utente);
}
