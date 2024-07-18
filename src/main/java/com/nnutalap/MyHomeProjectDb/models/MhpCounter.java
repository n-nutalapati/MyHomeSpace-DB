package com.nnutalap.MyHomeProjectDb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mhp_counter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MhpCounter {

    @Id
    private String id;
    private int seqNo;
}
