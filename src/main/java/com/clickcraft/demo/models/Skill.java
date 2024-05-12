package com.clickcraft.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "skills", indexes = {@Index(name = "idx_skill_name", columnList = "skillName", unique = true)})
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String skillName;

    public Skill() {
    }

    public Skill(String skillName) {
        this.skillName = skillName;
    }
}