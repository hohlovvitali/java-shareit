package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
public class InMemoryItemStorage implements ItemStorage {

    public Map<Long, Item> items;
    private Long currentId;

    public InMemoryItemStorage() {
        currentId = 0L;
        items = new HashMap<>();
    }

    @Override
    public Item create(Item item) throws ValidationException {
        if (isValidItem(item)) {
            item.setId(++currentId);
            items.put(item.getId(), item);
        }

        return item;
    }

    @Override
    public Item update(Item item) throws ValidationException, NotFoundException {
        if (item.getId() == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!items.containsKey(item.getId())) {
            throw new NotFoundException("Вещь с ID=" + item.getId() + " не найдена!");
        }
        if (item.getName() == null) {
            item.setName(items.get(item.getId()).getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(items.get(item.getId()).getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(items.get(item.getId()).getAvailable());
        }
        if (isValidItem(item)) {
            items.put(item.getId(), item);
        }

        return item;
    }

    @Override
    public Item delete(Long itemId) throws ValidationException, NotFoundException {
        if (itemId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещь с ID=" + itemId + " не найден!");
        }

        return items.remove(itemId);
    }

    @Override
    public List<Item> getItemsByOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId)).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteItemsByOwner(Long ownerId) {
        List<Long> deleteIds = items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .map(Item::getId).toList();
        for (Long deleteId : deleteIds) {
            items.remove(deleteId);
        }
    }

    @Override
    public Item getItemById(Long itemId) throws NotFoundException {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещь с ID=" + itemId + " не найдена!");
        }

        return items.get(itemId);
    }

    @Override
    public List<Item> getItemsBySearchQuery(String text) {
        List<Item> searchItems = new ArrayList<>();
        if (!text.isBlank()) {
            searchItems = items.values().stream()
                    .filter(Item::getAvailable)
                    .filter(item -> item.getName().toLowerCase().contains(text) ||
                            item.getDescription().toLowerCase().contains(text))
                    .collect(toList());
        }

        return searchItems;
    }

    private boolean isValidItem(Item item) throws ValidationException {
        if ((item.getName().isEmpty()) || (item.getDescription().isEmpty()) || (item.getAvailable() == null)) {
            throw new ValidationException("У вещи некорректные данные");
        }

        return true;
    }
}
