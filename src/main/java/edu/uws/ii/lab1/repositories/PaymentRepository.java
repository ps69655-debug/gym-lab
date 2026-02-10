package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.Payment;
import edu.uws.ii.lab1.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByMemberId(Long memberId);
    List<Payment> findByStatus(PaymentStatus status);
}