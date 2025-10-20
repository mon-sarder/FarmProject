package com.example.queueinv;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StoreService svc;

    public ApiController(StoreService svc) {
        this.svc = svc;
    }

    /* Inventory */
    @GetMapping("/inventory")
    public List<ItemDTO> listInventory() { return svc.listItems(); }

    @GetMapping("/inventory/search")
    public List<ItemDTO> searchInventory(@RequestParam String q) { return svc.searchItems(q); }

    @PostMapping("/inventory/addOrIncrement")
    public ResponseEntity<Void> addOrIncrement(@Valid @RequestBody AddOrIncDTO body) {
        svc.addOrIncrement(body.name(), body.amount());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/inventory/setQuantity")
    public ResponseEntity<Void> setQuantity(@Valid @RequestBody SetQtyDTO body) {
        svc.setQuantity(body.name(), body.qty());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/inventory/{name}")
    public ResponseEntity<Void> deleteItem(@PathVariable String name) {
        boolean ok = svc.removeItem(name);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /* Queue */
    @GetMapping("/queue")
    public List<CustomerDTO> listQueue() { return svc.listQueue(); }

    @GetMapping("/queue/search")
    public List<CustomerDTO> searchQueue(@RequestParam String q) { return svc.searchQueue(q); }

    @PostMapping("/queue/enqueue")
    public ResponseEntity<Void> enqueue(@Valid @RequestBody CustomerDTO body) {
        svc.enqueue(body);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/queue/dequeue")
    public ResponseEntity<CustomerDTO> dequeue() {
        return ResponseEntity.of(svc.dequeue());
    }

    @DeleteMapping("/queue")
    public ResponseEntity<Void> clearQueue() {
        svc.clearQueue();
        return ResponseEntity.noContent().build();
    }
}
