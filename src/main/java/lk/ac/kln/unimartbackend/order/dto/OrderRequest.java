package lk.ac.kln.unimartbackend.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull Long listingId
) {}