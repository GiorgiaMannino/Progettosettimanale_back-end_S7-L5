package it.epicode.Progettosettimanale_back_end_S7_L5.prenotazioni;

import it.epicode.Progettosettimanale_back_end_S7_L5.eventi.EventoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrenotazioneResponse {
    private long id;
    private String utente;
    private EventoResponse evento;
}
