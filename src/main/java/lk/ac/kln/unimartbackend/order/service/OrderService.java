package lk.ac.kln.unimartbackend.order.service;

import lk.ac.kln.unimartbackend.auth.entity.User;
import lk.ac.kln.unimartbackend.auth.repository.UserRepository;
import lk.ac.kln.unimartbackend.common.exception.ForbiddenException;
import lk.ac.kln.unimartbackend.common.exception.ResourceNotFoundException;
import lk.ac.kln.unimartbackend.listing.entity.Listing;
import lk.ac.kln.unimartbackend.listing.repository.ListingRepository;
import lk.ac.kln.unimartbackend.order.dto.OrderRequest;
import lk.ac.kln.unimartbackend.order.dto.OrderResponse;
import lk.ac.kln.unimartbackend.order.entity.Order;
import lk.ac.kln.unimartbackend.order.entity.OrderStatus;
import lk.ac.kln.unimartbackend.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orders;
    private final ListingRepository listings;
    private final UserRepository users;

    public OrderService(OrderRepository orders, ListingRepository listings, UserRepository users) {
        this.orders = orders;
        this.listings = listings;
        this.users = users;
    }

    @Transactional
    public OrderResponse create(OrderRequest request, String buyerEmail) {
        User buyer = users.findByUniversityEmail(buyerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Listing listing = listings.findById(request.listingId())
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));

        Order order = new Order(listing, buyer, listing.getPrice(), "MOCK");
        return toResponse(orders.save(order));
    }

    @Transactional
    public OrderResponse markCompleted(Long orderId, String requesterEmail) {
        Order order = orders.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getBuyer().getUniversityEmail().equalsIgnoreCase(requesterEmail)) {
            throw new ForbiddenException("Only the buyer can complete this order");
        }

        order.setStatus(OrderStatus.COMPLETED);
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        Order order = orders.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getListing().getId(),
                order.getListing().getTitle(),
                order.getBuyer().getId(),
                order.getBuyer().getFullName(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}