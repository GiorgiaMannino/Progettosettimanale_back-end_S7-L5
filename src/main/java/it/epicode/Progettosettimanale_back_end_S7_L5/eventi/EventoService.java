package it.epicode.Progettosettimanale_back_end_S7_L5.eventi;

import it.epicode.Progettosettimanale_back_end_S7_L5.auth.AppUser;
import it.epicode.Progettosettimanale_back_end_S7_L5.auth.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Validated
public class EventoService {


    @Autowired
    private EventoRepository eventoRepository;


    // Metodo per trasformare un oggetto Evento in EventoResponse
    private EventoResponse toResponse(Evento evento) {
        EventoResponse response = new EventoResponse();
        BeanUtils.copyProperties(evento, response);
        response.setOrganizzatore(evento.getOrganizzatore().getUsername());
        return response;
    }

    // Recupera tutti gli eventi
    public List<EventoResponse> getAll() {
        return eventoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Recupera un evento specifico tramite ID
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


    // Modifica un evento esistente
    public EventoResponse update(Long id, EventoRequest request, AppUser organizzatore) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato con id: " + id));

        boolean isOwner = evento.getOrganizzatore().getId().equals(organizzatore.getId());
        boolean isOrganizer = organizzatore.getRoles().contains(Role.ROLE_ORGANIZER);

        // Controllo per verificare se l'utente è l'organizzatore e il proprietario dell'evento
        if (isOrganizer && isOwner) {
            evento.setTitolo(request.getTitolo());
            evento.setDescrizione(request.getDescrizione());
            evento.setData(request.getData());
            evento.setLuogo(request.getLuogo());
            evento.setNumeroPostiDisponibili(request.getNumeroPostiDisponibili());

            eventoRepository.save(evento);
            return toResponse(evento);  // Restituisce l'EventoResponse
        } else {
            throw new AccessDeniedException("Non sei autorizzato a modificare questo evento.");
        }
    }




    // Elimina un evento
    public void delete(Long id, AppUser organizzatore) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato con id: " + id));

        boolean isOwner = evento.getOrganizzatore().getId().equals(organizzatore.getId());
        boolean isOrganizer = organizzatore.getRoles().contains(Role.ROLE_ORGANIZER);

        // Controllo per verificare se l'utente è l'organizzatore e il proprietario dell'evento
        if (isOrganizer && isOwner) {
            eventoRepository.delete(evento);
        } else {
            throw new AccessDeniedException("Non sei autorizzato a eliminare questo evento.");
        }
    }

}
