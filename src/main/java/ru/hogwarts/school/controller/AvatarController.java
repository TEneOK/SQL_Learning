package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.util.Map;

@RestController
@RequestMapping("/avatars")
@Tag(name = "Avatar Controller", description = "Управление аватарами с пагинацией")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @Operation(summary = "Получение аватарок с пагинацией")
    @GetMapping("/paged")
    public ResponseEntity<Map<String, Object>> getAllAvatarsWithPagination(
            @Parameter(description = "Номер страницы (начиная с 0)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы (количество элементов)")
            @RequestParam(defaultValue = "10") int size) {

        Page<Avatar> avatarsPage = avatarService.getAllAvatarsWithPagination(page, size);
        return ResponseEntity.ok((Map<String, Object>) avatarsPage);
    }
}