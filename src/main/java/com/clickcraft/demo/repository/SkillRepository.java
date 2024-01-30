package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository < Skill, Long > {

    Optional < Skill > findBySkillName(String skillName);

    List < Skill > findBySkillNameIn(List < String > skillNames);

}