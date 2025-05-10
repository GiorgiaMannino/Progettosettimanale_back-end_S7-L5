package it.epicode.Progettosettimanale_back_end_S7_L5.prenotazioni;

import it.epicode.Progettosettimanale_back_end_S7_L5.auth.AppUser;
import it.epicode.Progettosettimanale_back_end_S7_L5.eventi.Evento;
import it.epicode.Progettosettimanale_back_end_S7_L5.eventi.EventoRepository;
import it.epicode.Progettosettimanale_back_end_S7_L5.eventi.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private EventoRepository eventoRepository;

    public List<PrenotazioneResponse> getPrenotazioniPerPartecipante(AppUser partecipante) {
        if (partecipante == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non trovato");
        }

        List<Prenotazione> prenotazioni = prenotazioneRepository.findByUtente(partecipante);
        if (prenotazioni.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessuna prenotazione trovata");
        }

        return prenotazioni.stream()
                .map(this::getPrenotazioneResponse)
                .collect(Collectors.toList());
    }

    public Prenotazione getPrenotazioneById(Long id) {
        return prenotazioneRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prenotazione non trovata"));
    }

    public void createPrenotazione(Evento evento, AppUser partecipante) {
        if (evento.getNumeroPostiDisponibili() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Posti esauriti");
        }

        if (prenotazioneRepository.existsByEventoAndUtente(evento, partecipante)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Hai già prenotato questo evento");
        }
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtente(partecipante);
        prenotazione.setEvento(evento);
        evento.setNumeroPostiDisponibili(evento.getNumeroPostiDisponibili() - 1);
        prenotazioneRepository.save(prenotazione);
        eventoRepository.save(evento);
    }

    public void deletePrenotazione(Prenotazione prenotazione) {
        Evento evento = prenotazione.getEvento();

        if (!prenotazioneRepository.existsByEventoAndUtente(evento, prenotazione.getUtente())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Non hai prenotato questo evento");
        }
        evento.setNumeroPostiDisponibili(evento.getNumeroPostiDisponibili() + 1);

        prenotazioneRepository.delete(prenotazione);
        eventoRepository.save(evento);
    }

    public PrenotazioneResponse getPrenotazioneResponse(Prenotazione prenotazione) {
        PrenotazioneResponse prenotazioneResponse = new PrenotazioneResponse(
                prenotazione.getId(),
                prenotazione.getUtente().getUsername(),
                eventoService.toResponse(prenotazione.getEvento())
        );
        return prenotazioneResponse;
    }

}
