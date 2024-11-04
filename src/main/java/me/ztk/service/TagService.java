package me.ztk.service;

import me.ztk.dto.TagDTO;
import me.ztk.model.Tag;
import me.ztk.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagDTO mapToDTO(Tag tag) {
        return new TagDTO(tag.getId(), tag.getName(), tag.getCount(), tag.getParent() != null ? tag.getParent().getName() : null, buildParentHierarchy(tag));
    }

    private static String buildParentHierarchy(Tag tag) {
        if (tag == null) {
            return "";
        }
        StringBuilder hierarchy = new StringBuilder(tag.getName());
        while (tag.getParent() != null) {
            hierarchy.insert(0, tag.getParent().getName() + "/");
            tag = tag.getParent();
        }
        return hierarchy.toString();
    }

    public Tag mapToEntity(TagDTO tagDTO) {
        // if exists, return the tag
        if (tagRepository.findByName(tagDTO.getName()).isPresent()) {
            return tagRepository.findByName(tagDTO.getName()).get();
        }
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        tag.setCount(tagDTO.getCount());
        if (tagDTO.getParent() != null) {
            tag.setParent(tagRepository.findByName(tagDTO.getParent()).orElse(null));
        }
        return tag;
    }

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public TagDTO getTagByName(String name) {
        Tag tag = tagRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        return mapToDTO(tag);
    }

    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        return mapToDTO(tag);
    }

    public TagDTO createTag(TagDTO tagDTO) {
        try {
            Tag tag = mapToEntity(tagDTO);
            Tag savedTag = tagRepository.save(tag);
            return mapToDTO(savedTag);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag already exists");
        }
    }

    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        tagRepository.delete(tag);
    }

}
