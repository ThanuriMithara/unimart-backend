package lk.ac.kln.unimartbackend.listing.repository;

import lk.ac.kln.unimartbackend.listing.entity.Listing;
import lk.ac.kln.unimartbackend.listing.entity.ListingStatus;
import org.springframework.data.jpa.domain.Specification;

public class ListingSpecifications {

    public static Specification<Listing> hasQuery(String q) {
        return (root, query, cb) -> {
            if (q == null || q.isBlank()) return cb.conjunction();
            String pattern = "%" + q.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("title")), pattern);
        };
    }

    public static Specification<Listing> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return cb.conjunction();
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Listing> hasStatus(ListingStatus status) {
        return (root, query, cb) -> {
            if (status == null) return cb.notEqual(root.get("status"), ListingStatus.ARCHIVED);
            return cb.equal(root.get("status"), status);
        };
    }
}