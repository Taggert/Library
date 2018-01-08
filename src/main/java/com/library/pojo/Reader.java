package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reader {
    private String id;
    private String firstName;
    private String lastName;
    private Integer birthYear;
}