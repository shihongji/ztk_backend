package me.ztk.service;

import me.ztk.dto.TagDTO;
import me.ztk.model.Tag;
import me.ztk.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;

public class TagServiceTest {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    public TagServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTags() {
        // Arrange
        Tag mockTag = new Tag(23L, "Java", null, 0, null, null);
        Mockito.when(tagRepository.findAll()).thenReturn(List.of(mockTag));
        // Act
        List<TagDTO> result = tagService.getAllTags();
        // Assert
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
    }

    @Test
    void testGetTagByName() {
        // Arrange
        Tag mockTag = new Tag(23L, "Java", null, 0, null, null);
        Mockito.when(tagRepository.findByName("Java")).thenReturn(java.util.Optional.of(mockTag));
        // Act
        TagDTO result = tagService.getTagByName("Java");
        // Assert
        assertEquals("Java", result.getName());
    }

    @Test
    void testMapToDTO() {
        // Arrange
        Tag tag = new Tag(23L, "Java", null, 0, null, null);
        // Act
        TagDTO result = tagService.mapToDTO(tag);
        // Assert
        assertEquals("Java", result.getName());
    }

    @Test
    void testMapToEntity() {
        // Arrange
        TagDTO tagDTO = new TagDTO(null, "Java", 0, null, null);
        // Act
        Tag result = tagService.mapToEntity(tagDTO);
        // Assert
        assertEquals("Java", result.getName());
    }

    @Test
    void testGetTagById() {
        // Arrange
        Tag mockTag = new Tag(23L, "Java", null, 0, null, null);
        Mockito.when(tagRepository.findById(23L)).thenReturn(java.util.Optional.of(mockTag));
        // Act
        TagDTO result = tagService.getTagById(23L);
        // Assert
        assertEquals("Java", result.getName());
    }

    @Test
    void testCreateTag() {
        // Arrange
        TagDTO tagDTO = new TagDTO(23L, "Java", 0, null, null);
        Tag mockTag = new Tag(23L, "Java", null, 0, null, null);
        Mockito.when(tagRepository.save(Mockito.any(Tag.class))).thenReturn(mockTag);
        // Act
        TagDTO result = tagService.createTag(tagDTO);
        // Assert
        assertEquals("Java", result.getName());
    }

    @Test
    void testBuildParentHierarchy() {
        // Arrange
        Tag parent = new Tag(23L, "Java", null, 0, null, null);
        Tag tag = new Tag(23L, "Spring", parent, 0, null, null);
        Tag child = new Tag(23L, "Boot", tag, 0, null, null);
        // Act
        String result = TagService.buildParentHierarchy(child);
        // Assert
        assertEquals("Java/Spring/Boot", result);
    }

    @Test
    void testDeleteTag() {
        // Arrange
        Tag mockTag = new Tag(23L, "Java", null, 0, null, null);
        Mockito.when(tagRepository.findById(23L)).thenReturn(java.util.Optional.of(mockTag));
        // Act
        tagService.deleteTag(23L);
        // Assert
        Mockito.verify(tagRepository).delete(mockTag);
    }

    @Test
    void testDeleteTagNotFound() {
        // Arrange
        Mockito.when(tagRepository.findById(23L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tagService.deleteTag(23L);
        });

        // Verify exception message and status
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Tag not found", exception.getReason());

        // Verify delete is never called
        Mockito.verify(tagRepository, never()).delete(Mockito.any(Tag.class));
    }
}
