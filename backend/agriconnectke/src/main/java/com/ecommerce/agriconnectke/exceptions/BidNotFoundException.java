package com.ecommerce.agriconnectke.exceptions;

public class BidNotFoundException extends AgriconnectException {
    public BidNotFoundException(Long bidId) {
        super("BID_NOT_FOUND", "Bid not found with ID: " + bidId);
    }
}
