package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Notes;
import com.udacity.jwdnd.course1.cloudstorage.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {
    @Autowired
    private NotesRepository notesMapper;

    public List<Notes> getAllNotes(int userid) throws Exception {
        List<Notes> notes = notesMapper.findByUserId(userid);
        if (notes == null) {
            throw new Exception();
        }
        return notes;
    }

    public void addNote(Notes note, int userid) {
        notesMapper.insertNote(note, userid);
    }

    public void updateNote(Notes note) {
        notesMapper.updateNote(note);
    }

    public void deleteNote(int noteid) {
        notesMapper.deleteNote(noteid);
    }
}
