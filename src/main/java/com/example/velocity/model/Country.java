package com.example.velocity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private String id;
    private String code;
    private String code2;
    private String label;
    private String nationality;
}
