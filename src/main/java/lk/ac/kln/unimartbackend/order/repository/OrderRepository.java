package lk.ac.kln.unimartbackend.order.repository;

import lk.ac.kln.unimartbackend.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}