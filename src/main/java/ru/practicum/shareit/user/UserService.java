package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(toList());
    }


    public UserDto getUserById(Long id) throws NotFoundException {
        return UserMapper.toUserDto(userStorage.getUserById(id));
    }

    public UserDto create(UserDto userDto) throws ValidationException, DuplicateException {
        return UserMapper.toUserDto(userStorage.create(UserMapper.toUser(userDto)));
    }

    public UserDto update(UserDto userDto, Long id) throws ValidationException, DuplicateException, NotFoundException {
        if (userDto.getId() == null) {
            userDto.setId(id);
        }

        return UserMapper.toUserDto(userStorage.update(UserMapper.toUser(userDto)));
    }

    public UserDto delete(Long userId) throws ValidationException, NotFoundException {
        return UserMapper.toUserDto(userStorage.delete(userId));
    }
}
