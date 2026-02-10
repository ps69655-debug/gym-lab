package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.Payment;
import java.util.List;

public interface PaymentService {
    List<Payment> findAll();
    List<Payment> findByMember(Long memberId);
    Payment save(Payment payment);
}