package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.Payment;
import edu.uws.ii.lab1.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    public PaymentServiceImpl(PaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Payment> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Payment> findByMember(Long memberId) {
        return repository.findByMemberId(memberId);
    }

    @Override
    public Payment save(Payment payment) {
        return repository.save(payment);
    }
}