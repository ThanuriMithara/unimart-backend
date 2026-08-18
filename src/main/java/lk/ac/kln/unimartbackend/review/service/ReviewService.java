package lk.ac.kln.unimartbackend.review.service;

import lk.ac.kln.unimartbackend.auth.repository.UserRepository;
import lk.ac.kln.unimartbackend.common.exception.ConflictException;
import lk.ac.kln.unimartbackend.common.exception.ForbiddenException;
import lk.ac.kln.unimartbackend.common.exception.ResourceNotFoundException;
import lk.ac.kln.unimartbackend.order.entity.Order;
import lk.ac.kln.unimartbackend.order.entity.OrderStatus;
import lk.ac.kln.unimartbackend.order.repository.OrderRepository;
import lk.ac.kln.unimartbackend.review.dto.ReviewCreateRequest;
import lk.ac.kln.unimartbackend.review.dto.ReviewResponse;
import lk.ac.kln.unimartbackend.review.dto.ReviewUpdateRequest;
import lk.ac.kln.unimartbackend.review.entity.Review;
import lk.ac.kln.unimartbackend.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviews;
    private final OrderRepository orders;
    private final UserRepository users;

    public ReviewService(ReviewRepository reviews, OrderRepository orders, UserRepository users) {
        this.reviews = reviews;
        this.orders = orders;
        this.users = users;
    }

    @Transactional
    public ReviewResponse create(ReviewCreateRequest request, String email) {
        Order order = orders.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new ConflictException("Only completed orders can be reviewed");
        }

        if (!order.getBuyer().getUniversityEmail().equalsIgnoreCase(email)) {
            throw new ForbiddenException("Only the buyer can review this order");
        }

        if (reviews.existsByOrderId(order.getId())) {
            throw new ConflictException("This order already has a review");
        }

        Review review = new Review(
                order,
                order.getBuyer(),
                order.getListing().getSeller(),
                request.rating(),
                normalize(request.comment())
        );

        return toResponse(reviews.save(review));
    }

    @Transactional
    public ReviewResponse update(Long id, ReviewUpdateRequest request, String email) {
        Review review = reviews.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getReviewer().getUniversityEmail().equalsIgnoreCase(email)) {
            throw new ForbiddenException("You do not own this review");
        }

        review.setRating(request.rating());
        review.setComment(normalize(request.comment()));

        return toResponse(review);
    }

    @Transactional
    public void delete(Long id, String email) {
        Review review = reviews.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getReviewer().getUniversityEmail().equalsIgnoreCase(email)) {
            throw new ForbiddenException("You do not own this review");
        }

        reviews.delete(review);
    }

    @Transactional(readOnly = true)
    public ReviewResponse get(Long id) {
        Review review = reviews.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        return toResponse(review);
    }

    private String normalize(String comment) {
        return comment == null ? null : comment.trim();
    }

    private ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getOrder().getId(),
                review.getReviewer().getId(),
                review.getReviewer().getFullName(),
                review.getReviewee().getId(),
                review.getReviewee().getFullName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}