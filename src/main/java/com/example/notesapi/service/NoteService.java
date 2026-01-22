package com.example.notesapi.service;

import com.example.notesapi.dto.NoteRequest;
import com.example.notesapi.dto.NoteResponse;
import com.example.notesapi.model.Note;
import com.example.notesapi.repository.NoteRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;

    public NoteService(NoteRepository noteRepository, UserService userService) {
        this.noteRepository = noteRepository;
        this.userService = userService;
    }

    public NoteResponse createNote(NoteRequest request, String username) {
        Long userId = userService.getUserIdByUsername(username);
        Note note = new Note(null, userId, request.getTitle(), request.getContent());
        Note savedNote = noteRepository.save(note);
        return toResponse(savedNote);
    }

    public List<NoteResponse> getAllNotesForUser(String username) {
        Long userId = userService.getUserIdByUsername(username);
        return noteRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<NoteResponse> getNoteById(Long id, String username) {
        Long userId = userService.getUserIdByUsername(username);
        Optional<Note> note = noteRepository.findById(id);

        if (note.isEmpty()) {
            return Optional.empty();
        }

        if (!note.get().getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to access this note");
        }

        return note.map(this::toResponse);
    }

    public Optional<NoteResponse> updateNote(Long id, NoteRequest request, String username) {
        Long userId = userService.getUserIdByUsername(username);
        Optional<Note> existingNote = noteRepository.findById(id);

        if (existingNote.isEmpty()) {
            return Optional.empty();
        }

        if (!existingNote.get().getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to update this note");
        }

        Note note = existingNote.get();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        Note savedNote = noteRepository.save(note);
        return Optional.of(toResponse(savedNote));
    }

    public boolean deleteNote(Long id, String username) {
        Long userId = userService.getUserIdByUsername(username);
        Optional<Note> existingNote = noteRepository.findById(id);

        if (existingNote.isEmpty()) {
            return false;
        }

        if (!existingNote.get().getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to delete this note");
        }

        noteRepository.deleteById(id);
        return true;
    }

    public List<NoteResponse> searchNotes(String query, String username) {
        Long userId = userService.getUserIdByUsername(username);
        return noteRepository.searchByUserIdAndQuery(userId, query).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private NoteResponse toResponse(Note note) {
        return new NoteResponse(note.getId(), note.getTitle(), note.getContent(), note.getUserId());
    }
}
