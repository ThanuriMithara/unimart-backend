package lk.ac.kln.unimartbackend.listing.repository;

import lk.ac.kln.unimartbackend.listing.entity.Listing;
import lk.ac.kln.unimartbackend.listing.entity.ListingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ListingRepository extends JpaRepository<Listing, Long>,
        JpaSpecificationExecutor<Listing> {

    Optional<Listing> findByIdAndStatusNot(Long id, ListingStatus status);
}