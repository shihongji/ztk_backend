package me.ztk.repository;

import me.ztk.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // Add a method to find a tag by name
    Optional<Tag> findByName(String name);
}
