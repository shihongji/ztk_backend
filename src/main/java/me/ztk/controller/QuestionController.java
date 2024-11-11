package me.ztk.controller;

import me.ztk.dto.QuestionDTO;
import me.ztk.dto.TagDTO;
import me.ztk.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        List<QuestionDTO> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        QuestionDTO question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/title")
    public ResponseEntity<QuestionDTO> getQuestionByTitle(@RequestParam String title) {
        QuestionDTO question = questionService.getQuestionByTitle(title);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByDifficulty(@PathVariable String difficulty) {
        List<QuestionDTO> questions = questionService.getQuestionsByDifficulty(difficulty);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/tag/{name}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByTag(@PathVariable String name) {
        List<QuestionDTO> questions = questionService.getQuestionsByTagName(name);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/tag/id/{id}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByTagId(@PathVariable Long id) {
        List<QuestionDTO> questions = questionService.getQuestionsByTagId(id);
        return ResponseEntity.ok(questions);
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) {
        try {
            QuestionDTO question = questionService.createQuestion(questionDTO);
            return ResponseEntity.status(201).body(question);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/tags")
    public ResponseEntity<QuestionDTO> updateTags(@PathVariable Long id, @RequestBody List<TagDTO> tags) {
        QuestionDTO question = questionService.updateTags(id, tags);
        return ResponseEntity.ok(question);
    }
}
