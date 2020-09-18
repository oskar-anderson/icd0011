package model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    private Long id;
    private String title;
    private boolean hidden;
    private List<String> tags;
}
