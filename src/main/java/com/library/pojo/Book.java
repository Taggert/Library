package com.library.pojo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book {
    private String id;
    private String bookName;
    private String autor;
    private Integer issueYear;
}