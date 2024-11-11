package me.ztk.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TagDTO {
    private Long id;
    private String name;
    private int count;
    private String parent;
    private String hierarchy;
}
