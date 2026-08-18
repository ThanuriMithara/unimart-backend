package lk.ac.kln.unimartbackend.order.controller;

import jakarta.validation.Valid;
import lk.ac.kln.unimartbackend.order.dto.OrderRequest;
import lk.ac.kln.unimartbackend.order.dto.OrderResponse;
import lk.ac.kln.unimartbackend.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication) {
        OrderResponse created = service.create(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id, Authentication authentication) {
        return service.get(id);
    }

    @PostMapping("/{id}/complete")
    public OrderResponse complete(@PathVariable Long id, Authentication authentication) {
        return service.markCompleted(id, authentication.getName());
    }
}