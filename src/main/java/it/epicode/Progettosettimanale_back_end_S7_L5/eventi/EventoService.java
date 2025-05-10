package it.epicode.Progettosettimanale_back_end_S7_L5.eventi;

import it.epicode.Progettosettimanale_back_end_S7_L5.auth.AppUser;
import it.epicode.Progettosettimanale_back_end_S7_L5.auth.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    // Trasformo un Evento in EventoResponse
    public EventoResponse toResponse(Evento evento) {
        EventoResponse response = new EventoResponse();
        BeanUtils.copyProperties(evento, response);
        response.setOrganizzatore(evento.getOrganizzatore().getUsername());
        return response;
    }

    // Recupero tutti gli eventi
    public List<EventoResponse> getAll() {
        return eventoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Recupero un evento con id
    public EventoResponse getById(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato con id: " + id));
        return toResponse(evento);
    }

    // Crea un nuovo evento
    public EventoResponse save(EventoRequest request, AppUser organizzatore) {
        if (!organizzatore.getRoles().contains(Role.ROLE_ORGANIZER)) {
            throw new AccessDeniedException("Non sei autorizzato a creare un nuovo evento.");
        }
        Evento evento = new Evento();
        evento.setTitolo(request.getTitolo());
        evento.setDescrizione(request.getDescrizione());
        evento.setData(request.getData());
        evento.setLuogo(request.getLuogo());
        evento.setNumeroPostiDisponibili(request.getNumeroPostiDisponibili());
        evento.setOrganizzatore(organizzatore);

        return toResponse(eventoRepository.save(evento));
    }

    // Modifico un evento esistente
    public EventoResponse update(Long id, EventoRequest request, AppUser organizzatore) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato con id: " + id));

        boolean isOwner = evento.getOrganizzatore().getId().equals(organizzatore.getId());
        boolean isOrganizer = organizzatore.getRoles().contains(Role.ROLE_ORGANIZER);

        if (isOrganizer && isOwner) {
            evento.setTitolo(request.getTitolo());
            evento.setDescrizione(request.getDescrizione());
            evento.setData(request.getData());
            evento.setLuogo(request.getLuogo());
            evento.setNumeroPostiDisponibili(request.getNumeroPostiDisponibili());

            eventoRepository.save(evento);
            return toResponse(evento);
        } else {
            throw new AccessDeniedException("Non sei autorizzato a modificare questo evento.");
        }
    }

    // Elimino un evento
    public void delete(Long id, AppUser organizzatore) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato con id: " + id));

        boolean isOwner = evento.getOrganizzatore().getId().equals(organizzatore.getId());
        boolean isOrganizer = organizzatore.getRoles().contains(Role.ROLE_ORGANIZER);

        if (isOrganizer && isOwner) {
            eventoRepository.delete(evento);
        } else {
            throw new AccessDeniedException("Non sei autorizzato a eliminare questo evento.");
        }
    }

    public Evento getEventoById(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato con id: " + id));
    }
}

