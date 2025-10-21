package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * ArrayListMethods manages a simple inventory of (itemName -> quantity).
 * It provides safe helpers for CRUD-like operations.
 */
public class ArrayListMethods {

    public static final class Item {
        private final String name;
        private final int quantity;

        public Item(String name, int quantity) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Item name cannot be empty");
            }
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
            this.name = name.trim();
            this.quantity = quantity;
        }

        public String getName() { return name; }
        public int getQuantity() { return quantity; }

        public Item withQuantity(int q) { return new Item(name, q); }

        @Override public String toString() {
            return name + " (" + quantity + ")";
        }
    }

    private final ArrayList<Item> items = new ArrayList<>();

    /** Return an immutable snapshot of items (sorted by name). */
    public List<Item> list() {
        ArrayList<Item> copy = new ArrayList<>(items);
        copy.sort((a,b) -> a.getName().compareToIgnoreCase(b.getName()));
        return Collections.unmodifiableList(copy);
    }

    /** Add a new item (or increase quantity if it already exists). */
    public void addOrIncrement(String name, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        int idx = indexOf(name);
        if (idx >= 0) {
            Item old = items.get(idx);
            items.set(idx, old.withQuantity(old.getQuantity() + amount));
        } else {
            items.add(new Item(name, amount));
        }
    }

    /** Set the quantity of an item, creating it if missing. */
    public void setQuantity(String name, int quantity) {
        int idx = indexOf(name);
        if (idx >= 0) {
            items.set(idx, items.get(idx).withQuantity(quantity));
        } else {
            items.add(new Item(name, quantity));
        }
    }

    /** Remove an item by exact (case-insensitive) name. */
    public boolean remove(String name) {
        int idx = indexOf(name);
        if (idx >= 0) {
            items.remove(idx);
            return true;
        }
        return false;
    }

    /** Find an item by exact (case-insensitive) name. */
    public Optional<Item> find(String name) {
        int idx = indexOf(name);
        return idx >= 0 ? Optional.of(items.get(idx)) : Optional.empty();
    }

    /** Search items whose name contains the query (case-insensitive). */
    public List<Item> search(String query) {
        String q = (query == null) ? "" : query.trim().toLowerCase();
        ArrayList<Item> out = new ArrayList<>();
        if (q.isEmpty()) return out;
        for (Item it : items) {
            if (it.getName().toLowerCase().contains(q)) out.add(it);
        }
        out.sort((a,b) -> a.getName().compareToIgnoreCase(b.getName()));
        return out;
    }

    private int indexOf(String name) {
        if (name == null) return -1;
        String target = name.trim();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(target)) return i;
        }
        return -1;
    }
}
