package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Autowired
    public ItemService(@Qualifier("InMemoryItemStorage") ItemStorage itemStorage,
                       @Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public ItemDto create(ItemDto itemDto, Long ownerId) throws NotFoundException, ValidationException {
        userStorage.getUserById(ownerId);
        Item newItem = ItemMapper.toItem(itemDto, ownerId);
        System.out.println(newItem);
        itemStorage.create(newItem);
        return ItemMapper.toItemDto(newItem);
    }

    public List<ItemDto> getItemsByOwner(Long ownderId) {
        return itemStorage.getItemsByOwner(ownderId).stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
    }

    public ItemDto getItemById(Long id) throws NotFoundException {
        return ItemMapper.toItemDto(itemStorage.getItemById(id));
    }

    public ItemDto update(ItemDto itemDto, Long ownerId, Long itemId) throws NotFoundException, ValidationException {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }

        Item oldItem = itemStorage.getItemById(itemId);
        if (!oldItem.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("У пользователя c id = " + ownerId + " нет такой вещи!");
        }

        return ItemMapper.toItemDto(itemStorage.update(ItemMapper.toItem(itemDto, ownerId)));
    }

    public ItemDto delete(Long itemId, Long ownerId) throws ValidationException, NotFoundException {
        Item item = itemStorage.getItemById(itemId);
        if (!item.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("У пользователя нет такой вещи!");
        }
        return ItemMapper.toItemDto(itemStorage.delete(itemId));
    }

    public void deleteItemsByOwner(Long ownderId) {
        itemStorage.deleteItemsByOwner(ownderId);
    }

    public List<ItemDto> getItemsBySearchQuery(String text) {
        text = text.toLowerCase();
        return itemStorage.getItemsBySearchQuery(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
    }
}
