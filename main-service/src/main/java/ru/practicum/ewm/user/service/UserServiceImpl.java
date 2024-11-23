package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.utils.Constants.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        user = userRepository.save(user);
        userDto.setId(user.getId());
        return userDto;
    }

   @Override
   @Transactional
   public List<UserDto> get(List<Integer> ids, Integer from, Integer size) {
       Pageable pageable = PageRequest.of(from / size, size);
       Page<User> usersPage = (ids == null || ids.isEmpty())
               ? userRepository.findAll(pageable)
               : userRepository.findByIdIn(ids, pageable);

       return usersPage.stream()
               .map(userMapper::toUserDto)
               .collect(Collectors.toList());
   }

    @Override
    public void deleteById(Integer userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId))
        );
        userRepository.deleteById(userId);
    }
}
