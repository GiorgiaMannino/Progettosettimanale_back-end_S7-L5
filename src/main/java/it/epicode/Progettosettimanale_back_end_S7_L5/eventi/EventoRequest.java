package it.epicode.Progettosettimanale_back_end_S7_L5.eventi;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoRequest {

    @NotBlank (message = "Il titolo è obbligatorio")
    private String titolo;

    @NotBlank (message = "La descrizione è obbligatoria")
    private String descrizione;

    @NotNull  (message = "La data dell'evento è obbligatoria")
    private LocalDate data;

    @NotBlank (message = "Il luogo è obbligatorio")
    private String luogo;

    @NotNull  (message = "Il numero di posti disponibili è obbligatorio")
    private int numeroPostiDisponibili;

}
