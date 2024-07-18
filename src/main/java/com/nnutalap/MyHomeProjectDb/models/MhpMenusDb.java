package com.nnutalap.MyHomeProjectDb.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Document(collection = "mhp_menus")
public class MhpMenusDb {

    @Id
    private String id;
    private Integer userId;
    private String name;
    private List<MhpChildDb> children;

}