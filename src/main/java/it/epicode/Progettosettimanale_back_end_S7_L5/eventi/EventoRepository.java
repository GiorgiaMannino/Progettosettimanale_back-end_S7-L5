package it.epicode.Progettosettimanale_back_end_S7_L5.eventi;


import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}