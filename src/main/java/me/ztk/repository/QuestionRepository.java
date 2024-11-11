package me.ztk.repository;

import me.ztk.model.Difficulty;
import me.ztk.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByTitle(String title);
    List<Question> findByDifficulty(Difficulty difficulty);

    @Query("SELECT q FROM Question q JOIN q.tags t WHERE t.name = :tagName")
    List<Question> findByTagName(@Param("tagName") String tagName);

    @Query("SELECT q FROM Question q JOIN q.tags t WHERE t.id = :tagId")
    List<Question> findByTagId(@Param("tagId") Long tagId);
}
