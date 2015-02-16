package com.saurabh.apigee.bidding.impl;

import com.saurabh.apigee.bidding.models.Bid;
import com.saurabh.apigee.bidding.models.BidTracker;
import com.saurabh.apigee.bidding.models.Bidding;
import com.saurabh.apigee.bidding.models.Item;

/**
 * The BiddingImpl works as the entry point for all
 * bidding related calls.
 *
 * @author: Saurabh [1147am@gmail.com]
 */
public class BiddingImpl {

    private final Bidding bidding;
    private final BidTracker bidHistory;

    public BiddingImpl(Bidding bidding, BidTracker bidHistory) {
        this.bidding = bidding;
        this.bidHistory = bidHistory;
    }

    public void bid(Bid bid, String itemName) {
        if(bid == null)
            throw new RuntimeException("Unable to bid as the bid is null.");
        if(itemName == null)
            throw new RuntimeException("Unable to bid as the item name is null.");
        if(bid.getUser() == null)
            throw new RuntimeException("Unable to bid as the user is null.");

        bidHistory.registerBid(bid, bidding.find(itemName));
    }

    public BidTracker getBidHistory() {
        return bidHistory;
    }

    public Item find(String itemName) {
        return bidding.find(itemName);
    }
}