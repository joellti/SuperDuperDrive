package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int insert(Note note) {
        //System.out.println("userId: " + note.getUserId());
        return this.noteMapper.insert(note);
    }

    public int update(Note note) {
        return noteMapper.update(note);
    }

    public List<Note> findNotesByUserId(Integer userid) {
        List<Note> notes = this.noteMapper.findByUserId(userid);
        //System.out.println("notes.size() " + notes.size());
        return notes;
    }

    public Note findNoteByNoteId(Integer noteId) {
        return this.noteMapper.findByNoteId(noteId);
    }

    public int deleteNoteByNoteId(Integer noteId) {
        return this.noteMapper.delete(noteId);
    }

}
