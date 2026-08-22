package lk.ac.kln.unimartbackend.review.mapper;

import lk.ac.kln.unimartbackend.review.dto.ReviewResponse;
import lk.ac.kln.unimartbackend.review.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewResponse toResponse(Review review) {
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
