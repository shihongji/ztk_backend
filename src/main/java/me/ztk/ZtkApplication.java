package me.ztk;

import me.ztk.model.Tag;
import me.ztk.repository.TagRepository;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ZtkApplication {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ZtkApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(ZtkApplication.class, args);
    }
}
