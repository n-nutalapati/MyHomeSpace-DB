package com.nnutalap.MyHomeProjectDb.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MhpChildDb {

    private Long id;
    private String name;
    private String url;

}
