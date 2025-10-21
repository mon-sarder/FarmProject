package com.example.queueinv;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final Map<String, Integer> inventory = new ConcurrentHashMap<>(); // key=lowercase name
    private final Map<String, String> inventoryNames = new ConcurrentHashMap<>(); // original case
    private final Deque<CustomerDTO> queue = new ConcurrentLinkedDeque<>();

    /* Inventory */
    public List<ItemDTO> listItems() {
        return inventory.entrySet().stream()
                .map(e -> new ItemDTO(inventoryNames.get(e.getKey()), e.getValue()))
                .sorted(Comparator.comparing(ItemDTO::name, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public void addOrIncrement(String name, int amount) {
        String key = name.trim().toLowerCase();
        inventoryNames.put(key, name.trim());
        inventory.merge(key, amount, Integer::sum);
    }

    public void setQuantity(String name, int qty) {
        String key = name.trim().toLowerCase();
        inventoryNames.put(key, name.trim());
        inventory.put(key, qty);
    }

    public boolean removeItem(String name) {
        String key = name.trim().toLowerCase();
        inventoryNames.remove(key);
        return inventory.remove(key) != null;
    }

    public List<ItemDTO> searchItems(String q) {
        String s = q == null ? "" : q.trim().toLowerCase();
        if (s.isEmpty()) return List.of();
        return listItems().stream()
                .filter(it -> it.name().toLowerCase().contains(s))
                .collect(Collectors.toList());
    }

    /* Queue */
    public List<CustomerDTO> listQueue() {
        return List.copyOf(queue);
    }

    public void enqueue(CustomerDTO c) {
        queue.addLast(new CustomerDTO(c.name().trim(), Optional.ofNullable(c.note()).orElse("").trim()));
    }

    public Optional<CustomerDTO> dequeue() {
        return Optional.ofNullable(queue.pollFirst());
    }

    public void clearQueue() {
        queue.clear();
    }

    public List<CustomerDTO> searchQueue(String q) {
        String s = q == null ? "" : q.trim().toLowerCase();
        if (s.isEmpty()) return List.of();
        return queue.stream().filter(c -> c.name().toLowerCase().contains(s)).toList();
    }
}
