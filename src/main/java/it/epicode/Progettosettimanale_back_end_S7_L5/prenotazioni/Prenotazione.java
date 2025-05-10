package it.epicode.Progettosettimanale_back_end_S7_L5.prenotazioni;

import it.epicode.Progettosettimanale_back_end_S7_L5.auth.AppUser;
import it.epicode.Progettosettimanale_back_end_S7_L5.eventi.Evento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prenotazioni")
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Evento evento;

    @ManyToOne
    private AppUser utente;
}
