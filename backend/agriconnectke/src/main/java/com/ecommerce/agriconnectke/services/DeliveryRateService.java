package com.ecommerce.agriconnectke.services;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.agriconnectke.models.DeliveryRate;
import com.ecommerce.agriconnectke.repositories.DeliveryRateRepository;

@Service
public class DeliveryRateService {
    private final DeliveryRateRepository repo;

    public DeliveryRateService(DeliveryRateRepository repo) { this.repo = repo; }

    public DeliveryRate create(DeliveryRate r) { return repo.save(r); }
    public List<DeliveryRate> all() { return repo.findAll(); }
    public DeliveryRate latest() { return repo.findLatestRate(); }
}