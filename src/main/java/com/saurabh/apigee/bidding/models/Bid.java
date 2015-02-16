package com.saurabh.apigee.bidding.models;


public interface Bid extends Comparable<Bid> {
    int value();
    long getBidTime();
    User getUser();
    boolean isAValidBid(Item item);
    void execute(Item item);
}