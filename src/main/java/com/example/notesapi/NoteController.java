package com.example.notesapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final Map<Long, Note> notes = new HashMap<>();

    public NoteController() {
        // Seed data: notes belonging to different users
        notes.put(1L, new Note(1L, 100L, "Alice's Private Note", "This is Alice's secret content"));
        notes.put(2L, new Note(2L, 100L, "Alice's Work Note", "Alice's work related information"));
        notes.put(3L, new Note(3L, 200L, "Bob's Personal Note", "Bob's confidential data"));
        notes.put(4L, new Note(4L, 200L, "Bob's Passwords", "Bob's sensitive credentials"));
        notes.put(5L, new Note(5L, 300L, "Charlie's Note", "Charlie's private thoughts"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNote(@PathVariable Long id) {
        Note note = notes.get(id);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note updatedNote) {
        Note existingNote = notes.get(id);
        if (existingNote == null) {
            return ResponseEntity.notFound().build();
        }
        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setContent(updatedNote.getContent());
        return ResponseEntity.ok(existingNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        Note note = notes.get(id);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }
        notes.remove(id);
        return ResponseEntity.noContent().build();
    }
}
