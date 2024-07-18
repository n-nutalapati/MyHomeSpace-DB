package com.nnutalap.MyHomeProjectDb.service;

import com.nnutalap.MyHomeProjectDb.models.MhpCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class MongoSequenceGenerator {

    @Autowired
    private MongoOperations mongoOperations;

    public int getSeqUserId(String sequenceName){
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seqNo", 1);

        MhpCounter seqId = mongoOperations.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true).upsert(true), MhpCounter.class);

        return !Objects.isNull(seqId) ? seqId.getSeqNo() : 1;
    }
}
