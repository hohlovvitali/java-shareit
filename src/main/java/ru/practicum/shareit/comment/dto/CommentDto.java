package ru.practicum.shareit.comment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;                // уникальный идентификатор комментария;
    @NotEmpty
    @NotBlank
    private String text;            // содержимое комментария;
    @JsonIgnore
    private Item item;              // вещь, к которой относится комментарий;
    private String authorName;      // имя автора комментария;
    private LocalDateTime created;  // дата создания комментария.
}