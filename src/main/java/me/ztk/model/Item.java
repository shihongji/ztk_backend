package me.ztk.model;

import jakarta.persistence.*;
import lombok.Data;
import me.ztk.converter.YearAttributeConverter;

import java.time.Year;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Convert(converter = YearAttributeConverter.class)
    @Column(name = "bought_year")
    private Year boughtYear;

    private boolean destroyed;

    private boolean liked;
}
