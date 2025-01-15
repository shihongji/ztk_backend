package me.ztk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ztk.model.Difficulty;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String title;
    private String content;
    private String answer;
    private Difficulty difficulty;
    private List<TagDTO> tags;
    private LocalDateTime updatedAt;

    public QuestionDTO(String title, String content, String answer, Difficulty difficulty, List<TagDTO> tags) {
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.difficulty = difficulty;
        this.tags = tags;
    }
}
