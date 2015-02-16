package com.saurabh.apigee.bidding.models;

public interface Item {

    String getName();
    int getDesired();
    int getExpiryInSec();
    long getExpiryTimeInSec();
    Bid getWonBid();
    void setWonBid(Bid bid);
    boolean isAValidItem();
}