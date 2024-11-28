package ru.practicum.ewm.likes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.likes.model.Like;
import ru.practicum.ewm.likes.service.LikesService;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.DEFAULT_FROM;
import static ru.practicum.ewm.utils.Constants.DEFAULT_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/likes")
public class LikesAdminController {

    private final LikesService likesService;

    @GetMapping("/search")
    public List<Like> search(@RequestParam(required = false) Integer eventId,
                             @RequestParam(required = false) Integer userId,
                             @RequestParam(defaultValue = DEFAULT_FROM + "") Integer from,
                             @RequestParam(defaultValue = DEFAULT_SIZE + "") Integer size) {
        return likesService.search(eventId, userId, from, size);
    }
}
