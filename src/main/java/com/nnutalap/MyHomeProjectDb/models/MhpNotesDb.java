package com.nnutalap.MyHomeProjectDb.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Setter
@Getter
@Document(collection = "mhp_notes")
public class MhpNotesDb {

    @Id
    private String id;
    private Integer userId;
    private String title;
    private String content;
    private Date creationDate;
    private Date lastUpdateDate;

    public MhpNotesDb() {
    }

}