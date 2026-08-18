package lk.ac.kln.unimartbackend.order.dto;

import lk.ac.kln.unimartbackend.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        Long id,
        Long listingId,
        String listingTitle,
        Long buyerId,
        String buyerName,
        BigDecimal totalAmount,
        OrderStatus status,
        String paymentMethod,
        Instant createdAt,
        Instant updatedAt
) {}