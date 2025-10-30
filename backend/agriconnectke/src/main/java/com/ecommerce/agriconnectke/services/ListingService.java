package com.ecommerce.agriconnectke.services;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.agriconnectke.models.Listing;
import com.ecommerce.agriconnectke.repositories.ListingRepository;

@Service
public class ListingService {
    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public List<Listing> getListingsByFarmer(Long farmerId) {
        return listingRepository.findByFarmerId(farmerId);
    }

    public Listing createListing(Listing listing) {
        return listingRepository.save(listing);
    }

    public void deleteListing(Long id) {
        listingRepository.deleteById(id);
    }
}