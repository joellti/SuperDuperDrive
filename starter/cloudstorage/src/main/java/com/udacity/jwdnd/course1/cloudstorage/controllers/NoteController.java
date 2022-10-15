package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class NoteController {

    private UserService userService;
    private NoteService noteService;

    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping("/note/update")
    public String updateNote(@ModelAttribute("note") Note noteToUpdate, Authentication authentication) {
        String username = authentication.getName();
        Integer userId = userService.getUser(username).getUserId();
        //System.out.println(userId);

        if (noteToUpdate.getNoteid() == null) {
            //System.out.println("New note: " + noteToUpdate.getNotetitle());
            noteToUpdate.setUserid(userId);
            noteService.insert(noteToUpdate);
        } else {
            //System.out.println("Existing note: " + noteToUpdate.getNotetitle());
            Note note = noteService.findNoteByNoteId(noteToUpdate.getNoteid());
            //System.out.println(noteToUpdate.getNoteid());
            if (note == null || userId != note.getUserId()) return "redirect:/result?other";
            noteService.update(noteToUpdate);
        }
        return "redirect:/result?success";
    }

    @GetMapping("/note/delete")
    public String deleteNote(@RequestParam("noteid") Integer noteId, Authentication authentication) {
        String username = authentication.getName();
        Integer userId = userService.getUser(username).getUserId();
        System.out.println(userId);

        System.out.println(noteId);
        Note note = noteService.findNoteByNoteId(noteId);
        System.out.println(note.getUserId());
        if (note == null || userId != note.getUserId()) return "redirect:/result?other";

        noteService.deleteNoteByNoteId(noteId);
        return "redirect:/result?success";

    }
}
