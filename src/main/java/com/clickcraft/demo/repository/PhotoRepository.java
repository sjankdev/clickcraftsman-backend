package com.clickcraft.demo.repository;

import com.clickcraft.demo.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository <Photo, Long > {


}