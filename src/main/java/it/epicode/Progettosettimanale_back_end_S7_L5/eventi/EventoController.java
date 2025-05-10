package it.epicode.Progettosettimanale_back_end_S7_L5.eventi;

import it.epicode.Progettosettimanale_back_end_S7_L5.auth.AppUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // Get di tutti gli eventi
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<EventoResponse> getAll() {
        return eventoService.getAll();
    }

    // Get di un evento per id
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public EventoResponse getById(@PathVariable Long id) {
        return eventoService.getById(id);
    }

    // Post per la creazione di un evento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ORGANIZER')")
    public EventoResponse create(@RequestBody @Valid EventoRequest eventoRequest, @AuthenticationPrincipal AppUser organizer) {
        return eventoService.save(eventoRequest, organizer);
    }


    // Put per la modifica di un evento
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public EventoResponse update(@PathVariable Long id, @RequestBody @Valid EventoRequest eventoRequest, @AuthenticationPrincipal AppUser organizer) {
        return eventoService.update(id, eventoRequest, organizer);
    }

    // Delete evento
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvento(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        eventoService.delete(id, user);
    }

}
