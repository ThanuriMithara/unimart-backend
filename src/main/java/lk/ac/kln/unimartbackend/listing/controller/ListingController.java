package lk.ac.kln.unimartbackend.listing.controller;

import jakarta.validation.Valid;
import lk.ac.kln.unimartbackend.listing.dto.ListingRequest;
import lk.ac.kln.unimartbackend.listing.dto.ListingResponse;
import lk.ac.kln.unimartbackend.listing.entity.ListingStatus;
import lk.ac.kln.unimartbackend.review.dto.ReviewResponse;
import lk.ac.kln.unimartbackend.review.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lk.ac.kln.unimartbackend.listing.service.ListingService;

@RestController
@RequestMapping("/api/v1/listings")
public class ListingController {

    private final ListingService service;
    private final ReviewService reviewService;

    public ListingController(ListingService service, ReviewService reviewService) {
        this.service = service;
        this.reviewService = reviewService;
    }

    @GetMapping
    public Page<ListingResponse> getAll(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) ListingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        int cappedSize = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, cappedSize, Sort.by("createdAt").descending());
        return service.getAll(q, categoryId, status, pageable);
    }

    @GetMapping("/{id}")
    public ListingResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/{id}/reviews")
    public Page<ReviewResponse> getReviews(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        int cappedSize = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, cappedSize, Sort.by("createdAt").descending());
        return reviewService.getReviewsForListing(id, pageable);
    }

    @PostMapping
    public ResponseEntity<ListingResponse> create(
            @Valid @RequestBody ListingRequest request,
            Authentication authentication) {
        ListingResponse created = service.create(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ListingResponse update(@PathVariable Long id,
                                  @Valid @RequestBody ListingRequest request,
                                  Authentication authentication) {
        return service.update(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication authentication) {
        service.archive(id, authentication.getName());
    }
}
