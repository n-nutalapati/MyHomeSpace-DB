package com.nnutalap.MyHomeProjectDb.repository;

import com.nnutalap.MyHomeProjectDb.models.MhpMenusDb;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MhpMenusDbRepo extends MongoRepository<MhpMenusDb, String> {

    List<MhpMenusDb> findByUserId(int userId);

}
