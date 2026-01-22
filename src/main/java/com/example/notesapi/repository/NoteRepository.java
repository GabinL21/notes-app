package com.example.notesapi.repository;

import com.example.notesapi.model.Note;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class NoteRepository {

    private final Map<Long, Note> notes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public NoteRepository() {
        // Seed data for testing
        Note note1 = new Note(idGenerator.getAndIncrement(), 1L, "User1's First Note", "This is user1's first note content");
        Note note2 = new Note(idGenerator.getAndIncrement(), 1L, "User1's Second Note", "This is user1's second note content");
        Note note3 = new Note(idGenerator.getAndIncrement(), 2L, "User2's Note", "This is user2's note content");
        Note note4 = new Note(idGenerator.getAndIncrement(), 3L, "Admin's Note", "This is admin's note content");

        notes.put(note1.getId(), note1);
        notes.put(note2.getId(), note2);
        notes.put(note3.getId(), note3);
        notes.put(note4.getId(), note4);
    }

    public Note save(Note note) {
        if (note.getId() == null) {
            note.setId(idGenerator.getAndIncrement());
        }
        notes.put(note.getId(), note);
        return note;
    }

    public Optional<Note> findById(Long id) {
        return Optional.ofNullable(notes.get(id));
    }

    public List<Note> findByUserId(Long userId) {
        return notes.values().stream()
                .filter(note -> note.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Note> findAll() {
        return new ArrayList<>(notes.values());
    }

    public void deleteById(Long id) {
        notes.remove(id);
    }

    public boolean existsById(Long id) {
        return notes.containsKey(id);
    }

    public List<Note> searchByUserIdAndQuery(Long userId, String query) {
        String lowerQuery = query.toLowerCase();
        return notes.values().stream()
                .filter(note -> note.getUserId().equals(userId))
                .filter(note ->
                    note.getTitle().toLowerCase().contains(lowerQuery) ||
                    note.getContent().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }
}
