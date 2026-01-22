package com.example.notesapi.controller;

import com.example.notesapi.dto.NoteRequest;
import com.example.notesapi.dto.NoteResponse;
import com.example.notesapi.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest request, Authentication authentication) {
        NoteResponse note = noteService.createNote(request, authentication.getName());
        return ResponseEntity.status(201).body(note);
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAllNotes(Authentication authentication) {
        List<NoteResponse> notes = noteService.getAllNotesForUser(authentication.getName());
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable Long id, Authentication authentication) {
        try {
            return noteService.getNoteById(id, authentication.getName())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long id, @RequestBody NoteRequest request, Authentication authentication) {
        try {
            return noteService.updateNote(id, request, authentication.getName())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id, Authentication authentication) {
        try {
            boolean deleted = noteService.deleteNote(id, authentication.getName());
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteResponse>> searchNotes(@RequestParam("q") String query, Authentication authentication) {
        List<NoteResponse> notes = noteService.searchNotes(query, authentication.getName());
        return ResponseEntity.ok(notes);
    }
}
