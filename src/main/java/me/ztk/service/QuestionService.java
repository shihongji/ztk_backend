package me.ztk.service;

import me.ztk.dto.QuestionDTO;
import me.ztk.dto.TagDTO;
import me.ztk.model.Difficulty;
import me.ztk.model.Question;
import me.ztk.repository.QuestionRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);
    private final QuestionRepository questionRepository;
    private final TagService tagService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, TagService tagService) {
        this.questionRepository = questionRepository;
        this.tagService = tagService;
    }

    private QuestionDTO mapToDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setTitle(question.getTitle());
        questionDTO.setContent(question.getContent());
        questionDTO.setAnswer(question.getAnswer());
        questionDTO.setDifficulty(question.getDifficulty());
        questionDTO.setCorrectAnswerTime(question.getCorrectAnswerTime());
        questionDTO.setWrongAnswerTime(question.getWrongAnswerTime());
        if (question.getTags() != null) {
            questionDTO.setTags(question.getTags().stream().map(tagService::mapToDTO).collect(Collectors.toList()));
        }
        return questionDTO;
    }

    private Question mapToEntity(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setTitle(questionDTO.getTitle());
        question.setContent(questionDTO.getContent());
        question.setAnswer(questionDTO.getAnswer());
        question.setDifficulty(questionDTO.getDifficulty());
        question.setCorrectAnswerTime(questionDTO.getCorrectAnswerTime());
        question.setWrongAnswerTime(questionDTO.getWrongAnswerTime());
        if (questionDTO.getTags() != null) {
            question.setTags(questionDTO.getTags().stream().map(tagService::mapToEntity).collect(Collectors.toSet()));
        }
        return question;
    }

    public QuestionDTO getQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        return mapToDTO(question);
    }

    public QuestionDTO getQuestionByTitle(String title) {
        Question question = questionRepository.findByTitle(title).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        return mapToDTO(question);
    }

    public List<QuestionDTO> getQuestionsByDifficulty(String difficulty) {
        List<Question> questions = questionRepository.findByDifficulty(Difficulty.valueOf(difficulty.toUpperCase()));
        return questions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<QuestionDTO> getQuestionsByTagName(String tagName) {
        // valid tag name
        TagDTO tag = tagService.getTagByName(tagName);
        if (tag == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        List<Question> questions = questionRepository.findByTagName(tagName);
        return questions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<QuestionDTO> getQuestionsByTagId(Long tagId) {
        // valid tag id
        TagDTO tag = tagService.getTagById(tagId);
        if (tag == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        List<Question> questions = questionRepository.findByTagId(tagId);
        return questions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        try {
            Question question = mapToEntity(questionDTO);
            Question savedQuestion = questionRepository.save(question);
            return mapToDTO(savedQuestion);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Question already exists");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while creating the question");
        }

    }

    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        questionRepository.delete(question);
    }

    public QuestionDTO updateTags(Long id, List<TagDTO> tags) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        question.setTags(tags.stream().map(tagService::mapToEntity).collect(Collectors.toSet()));
        Question updatedQuestion = questionRepository.save(question);
        return mapToDTO(updatedQuestion);
    }

}
