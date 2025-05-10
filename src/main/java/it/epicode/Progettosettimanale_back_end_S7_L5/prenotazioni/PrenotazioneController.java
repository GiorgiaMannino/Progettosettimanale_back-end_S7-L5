package it.epicode.Progettosettimanale_back_end_S7_L5.prenotazioni;

import it.epicode.Progettosettimanale_back_end_S7_L5.auth.AppUser;
import it.epicode.Progettosettimanale_back_end_S7_L5.eventi.Evento;
import it.epicode.Progettosettimanale_back_end_S7_L5.eventi.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private EventoService eventoService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<PrenotazioneResponse> getPrenotazioniPerPartecipante(@AuthenticationPrincipal AppUser user) {
        return prenotazioneService.getPrenotazioniPerPartecipante(user);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPrenotazione(@RequestBody PrenotazioneRequest prenotazioneRequest, @AuthenticationPrincipal AppUser user) {
        Evento evento = eventoService.getEventoById(prenotazioneRequest.getIdEvento());
        prenotazioneService.createPrenotazione(evento, user);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrenotazione(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        Prenotazione prenotazione = prenotazioneService.getPrenotazioneById(id);
        if (!prenotazione.getUtente().getId().equals(user.getId())) {
            throw new AccessDeniedException("Non sei autorizzato a eliminare questa prenotazione.");
        }
        prenotazioneService.deletePrenotazione(prenotazione);
    }
}
