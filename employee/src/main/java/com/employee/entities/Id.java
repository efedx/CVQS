package com.employee.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;

import java.io.Serializable;

@Getter
@MappedSuperclass
public abstract class Id implements Serializable {
    @jakarta.persistence.Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq_gen", strategy = GenerationType.SEQUENCE)
    private Long id;
}
