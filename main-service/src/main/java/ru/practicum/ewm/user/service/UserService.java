package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    List<UserDto> get(List<Integer> ids, Integer from, Integer size);

    void deleteById(Integer userId);
}
