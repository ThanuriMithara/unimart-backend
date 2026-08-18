package lk.ac.kln.unimartbackend.review.repository;

import lk.ac.kln.unimartbackend.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByOrderId(Long orderId);
}
