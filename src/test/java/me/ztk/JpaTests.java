package me.ztk;

import jakarta.transaction.Transactional;
import me.ztk.model.Tag;
import me.ztk.repository.QuestionRepository;
import me.ztk.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Transactional
@Rollback
public class JpaTests {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void testTagRepository() {
        // Test the TagRepository
//        Tag tag1 = new Tag();
//        tag1.setName("Java");
//        tag1.setCount(1);
//        tagRepository.save(tag1);
//
//        System.out.println("Tag1 ID: " + tag1.getId());
//        Tag retrievedTag = tagRepository.findById(tag1.getId()).get();
        tagRepository.findAll().forEach(tag -> System.out.println(tag.getId()));
//        System.out.println("Retrieved Tag ID: " + retrievedTag.getId());
//        assert(retrievedTag.getName().equals("Java"));
//        assert(retrievedTag.getCount() == 1);
    }

}
