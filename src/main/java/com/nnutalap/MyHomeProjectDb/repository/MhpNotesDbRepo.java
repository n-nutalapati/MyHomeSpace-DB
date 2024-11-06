package com.nnutalap.MyHomeProjectDb.repository;

import com.nnutalap.MyHomeProjectDb.models.MhpNotesDb;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
// import java.util.Optional;

public interface MhpNotesDbRepo extends MongoRepository<MhpNotesDb, String> {

    List<MhpNotesDb> findByUserId(int userId);
}
