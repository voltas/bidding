package com.saurabh.apigee.bidding.models;

import java.util.List;

public interface BidTracker {
    void registerBid(Bid bid, Item item);

    Bid getWinningBid(Item item);

    List<Bid> getAllBids(Item item);
    
    List<Bid> getTopFiveBids(Item item);

    List<Item> getAllItems(User user);

}