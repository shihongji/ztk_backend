package me.ztk.service;

import me.ztk.dto.QuestionDTO;
import me.ztk.dto.TagDTO;
import me.ztk.model.Question;
import me.ztk.model.Tag;
import me.ztk.repository.QuestionRepository;
import me.ztk.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagService tagService;

    @InjectMocks
    private QuestionService questionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllQuestions() {
        // Arrange
        Question mockQuestion = new Question();
        mockQuestion.setId(101L);
        mockQuestion.setTitle("Test with JUnit 5");
        mockQuestion.setContent("How to test with JUnit 5?");
        Mockito.when(questionRepository.findAll()).thenReturn(List.of(mockQuestion));

        // Act
        List<QuestionDTO> result = questionService.getAllQuestions();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test with JUnit 5", result.getFirst().getTitle());
    }

    @Test
    void testUpdateQuestionTags() {
        // Arrange
        Question mockQuestion = new Question();
        TagDTO tagDTOJava = new TagDTO(23L, "Java", 0, null, null);
        TagDTO tagDTOJUnit = new TagDTO(24L, "JUnit", 0, null, null);
        Tag tagJava = new Tag(23L, "Java", null, 0, null, null);
        Tag tagJUnit = new Tag(24L, "JUnit", null, 0, null, null);
        mockQuestion.setId(101L);
        mockQuestion.setTitle("Test with JUnit 5");
        mockQuestion.setContent("How to test with JUnit 5?");
        Mockito.when(questionRepository.findById(101L)).thenReturn(Optional.of(mockQuestion));
        Mockito.when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(tagRepository.findByName("Java")).thenReturn(Optional.of(tagJava));
        Mockito.when(tagRepository.findByName("JUnit")).thenReturn(Optional.of(tagJUnit));
        Mockito.when(tagService.mapToEntity(tagDTOJava)).thenReturn(tagJava);
        Mockito.when(tagService.mapToEntity(tagDTOJUnit)).thenReturn(tagJUnit);
        Mockito.when(tagService.mapToDTO(tagJava)).thenReturn(tagDTOJava);
        Mockito.when(tagService.mapToDTO(tagJUnit)).thenReturn(tagDTOJUnit);

        // Act
        QuestionDTO result = questionService.updateTags(101L, List.of(tagDTOJava, tagDTOJUnit));
        assertNotNull(result);
        assertEquals(2, result.getTags().size());
        // Verify that the tags are updated correctly
        List<String> tagNames = result.getTags().stream().map(TagDTO::getName).toList();
        assertTrue(tagNames.contains("Java"));
        assertTrue(tagNames.contains("JUnit"));

        verify(tagService, Mockito.times(2)).mapToEntity(any(TagDTO.class));
    }
}
