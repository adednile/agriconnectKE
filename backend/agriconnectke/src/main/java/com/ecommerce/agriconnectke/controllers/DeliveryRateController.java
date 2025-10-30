package com.ecommerce.agriconnectke.controllers;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.agriconnectke.models.DeliveryRate;
import com.ecommerce.agriconnectke.services.DeliveryRateService;

@RestController
@RequestMapping("/api/delivery-rates")
@CrossOrigin(origins = "*")
public class DeliveryRateController {
    private final DeliveryRateService rateService;
    public DeliveryRateController(DeliveryRateService rateService) { this.rateService = rateService; }

    @PostMapping
    public DeliveryRate create(@RequestBody DeliveryRate r) { return rateService.create(r); }

    @GetMapping("/latest")
    public DeliveryRate latest() { return rateService.latest(); }

    @GetMapping
    public List<DeliveryRate> all() { return rateService.all(); }
}
