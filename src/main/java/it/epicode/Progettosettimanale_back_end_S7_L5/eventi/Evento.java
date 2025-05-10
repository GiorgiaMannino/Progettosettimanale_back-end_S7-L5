package it.epicode.Progettosettimanale_back_end_S7_L5.eventi;


import it.epicode.Progettosettimanale_back_end_S7_L5.auth.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "eventi")
public class Evento {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column (length = 50, nullable = false)
    private String titolo;

    @Column (length = 150, nullable = false)
    private String descrizione;

    @Column (nullable = false)
    private LocalDate  data;

    @Column (length = 50, nullable =false)
    private String luogo;

    @Column (nullable = false)
    private int numeroPostiDisponibili;

    @ManyToOne
    private AppUser organizzatore;

}
