package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;                // уникальный идентификатор комментария;
    @NotBlank
    private String text;            // содержимое комментария;
    private String authorName;      // имя автора комментария;
    private LocalDateTime created;  // дата создания комментария.
}
