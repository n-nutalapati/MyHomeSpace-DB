package com.nnutalap.MyHomeProjectDb.controller;


import com.nnutalap.MyHomeProjectDb.models.MhpChildDb;
import com.nnutalap.MyHomeProjectDb.models.MhpMenusDb;
import com.nnutalap.MyHomeProjectDb.models.MhpNotesDb;
import com.nnutalap.MyHomeProjectDb.repository.MhpMenusDbRepo;
import com.nnutalap.MyHomeProjectDb.repository.MhpNotesDbRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mdb")
public class MhpRestDbController {

    @Autowired
    private MhpNotesDbRepo mhpNotesDbRepo;

    @Autowired
    private MhpMenusDbRepo mhpMenuDbRepo;

    @GetMapping("/getNotes/{userId}")
    public ResponseEntity<?> getNotes(@PathVariable int userId) {
//        List<MhpNotesDb> notesList = mhpNotesDbRepo.findAll();
        List<MhpNotesDb> notesList = mhpNotesDbRepo.findByUserId(userId);
        if (!notesList.isEmpty()) {
            return new ResponseEntity<List<MhpNotesDb>>(notesList, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("No notes Available", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createNote")
    public ResponseEntity<?> createNote(@RequestBody MhpNotesDb newNotes) {

        try {
            newNotes.setCreationDate(new Date(System.currentTimeMillis()));
            newNotes.setLastUpdateDate(new Date(System.currentTimeMillis()));
            mhpNotesDbRepo.save(newNotes);
            return new ResponseEntity<MhpNotesDb>(newNotes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

//    @GetMapping("/getNoteById/{id}")
//    public ResponseEntity<?> getNoteById(@PathVariable("id") String id) {
//        Optional<MhpNotesDb> noteOptional = mhpNotesDbRepo.findById(id);
//        if (noteOptional.isPresent()) {
//            return new ResponseEntity<>(noteOptional.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Note not found for this id: " + id, HttpStatus.NOT_FOUND);
//        }
//    }

    @DeleteMapping("/deleteNote/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable String id) {

        try {
            mhpNotesDbRepo.deleteById(id);
            return new ResponseEntity<>("Note deleted successfully with id: " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateNote/{id}")
    public ResponseEntity<?> updateNote(@PathVariable("id") String id, @RequestBody MhpNotesDb updatedNote) {
        Optional<MhpNotesDb> noteOptional = mhpNotesDbRepo.findById(id);
        if (noteOptional.isPresent()) {
            MhpNotesDb noteToSave = noteOptional.get();
            noteToSave.setTitle(updatedNote.getTitle() != null ? updatedNote.getTitle() : noteToSave.getTitle());
            noteToSave
                    .setContent(updatedNote.getContent() != null ? updatedNote.getContent() : noteToSave.getContent());
            noteToSave.setLastUpdateDate(new Date(System.currentTimeMillis()));
            mhpNotesDbRepo.save(noteToSave);
            return new ResponseEntity<>(noteToSave, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Note not found for this id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getMenus/{userId}")
    public ResponseEntity<?> getMenus(@PathVariable int userId) {

//        List<MhpMenusDb> menuList = mhpMenuDbRepo.findAll();
        List<MhpMenusDb> menuList = mhpMenuDbRepo.findByUserId(userId);

        return !menuList.isEmpty() ? new ResponseEntity<List<MhpMenusDb>>(menuList, HttpStatus.OK)
                : new ResponseEntity<String>("No notes Available", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/createMenu")
    public ResponseEntity<?> createMenu(@RequestBody MhpMenusDb newMenu) {

        long idSeq = 1000;
        try {
            for(MhpChildDb child : newMenu.getChildren()) {
                child.setId(idSeq);
                idSeq++;
            }
            mhpMenuDbRepo.save(newMenu);
            return new ResponseEntity<MhpMenusDb>(newMenu, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteMenu/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable String id) {
        try {
            mhpMenuDbRepo.deleteById(id);
            return new ResponseEntity<>("Menu deleted successfully with id: " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateMenu/{id}")
    public ResponseEntity<?> updateMenu(@PathVariable String id, @RequestBody MhpMenusDb updMenu) {

        Optional<MhpMenusDb> menuOptional = mhpMenuDbRepo.findById(id);

        if (menuOptional.isPresent()) {

            MhpMenusDb menuToSave = menuOptional.get();

            menuToSave.setName(updMenu.getName() != null ? updMenu.getName() : menuToSave.getName());
            menuToSave.setChildren(!updMenu.getChildren().isEmpty() ? updMenu.getChildren() : menuToSave.getChildren());

            mhpMenuDbRepo.save(menuToSave);
            return new ResponseEntity<>(menuToSave, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Update Failed. Menu not found!", HttpStatus.NOT_FOUND);
        }
    }
}
