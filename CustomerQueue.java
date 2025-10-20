package app;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

/**
 * CustomerQueue models a simple FIFO queue of customers.
 * Each customer has a name and (optionally) a note (e.g., order or reason).
 *
 * This class is thread-unsafe on purpose (Swing EDT usage expected).
 */
public class CustomerQueue {

    public static class Customer {
        private final String name;
        private final String note; // optional metadata

        public Customer(String name) {
            this(name, "");
        }

        public Customer(String name, String note) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Customer name cannot be empty");
            }
            this.name = name.trim();
            this.note = note == null ? "" : note.trim();
        }

        public String getName() { return name; }
        public String getNote() { return note; }

        @Override public String toString() {
            return note.isEmpty() ? name : (name + " — " + note);
        }
    }

    private final Deque<Customer> queue = new ArrayDeque<>();

    /** Add a customer to the end of the queue. */
    public void enqueue(Customer c) { queue.addLast(c); }

    /** Remove and return the next customer, or empty if none. */
    public Optional<Customer> dequeue() {
        Customer c = queue.pollFirst();
        return Optional.ofNullable(c);
    }

    /** View the next customer without removing, or empty if none. */
    public Optional<Customer> peek() {
        return Optional.ofNullable(queue.peekFirst());
    }

    /** Remove all customers. */
    public void clear() { queue.clear(); }

    /** Current size of the queue. */
    public int size() { return queue.size(); }

    /** Find all customers whose name contains the query (case-insensitive). */
    public List<Customer> searchByName(String query) {
        String q = (query == null) ? "" : query.trim().toLowerCase();
        List<Customer> out = new ArrayList<>();
        if (q.isEmpty()) return out;
        for (Customer c : queue) {
            if (c.getName().toLowerCase().contains(q)) out.add(c);
        }
        return out;
    }

    /** Return a snapshot (shallow copy) of the queue contents in order. */
    public List<Customer> toList() {
        return new ArrayList<>(queue);
    }
}
