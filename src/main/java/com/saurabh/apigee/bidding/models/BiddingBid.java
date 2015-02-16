package com.saurabh.apigee.bidding.models;

import java.util.Observable;

import com.saurabh.apigee.bidding.util.Immutable;

/**
 * The Bid class stores information regarding the bid
 * value and bid time a user is making on an item. It uses an
 * Observer pattern in order to notify the user when
 * a bid has been created.
 *
 * @author: Saurabh [1147am@gmail.com]
 */
@Immutable
public class BiddingBid extends Observable implements Bid {

    private final int bidAmount;
    private final long bidTime;
    private final User user;

    /**
     * Create an BiddingBid object for a given value and user.
     * @param bidAmount how much is the bid worth
     * @param user the person bidding
     */
    public BiddingBid(final int bidAmount, final User user) {
        if(bidAmount < 0) throw new RuntimeException("Unable to create a bid with a negative amount: " +bidAmount);

        addObserver(user); // we want to record a bid on an item with the user

        this.user = user;
        this.bidAmount = bidAmount;
        this.bidTime = System.currentTimeMillis() / 1000L;;
    }

    @Override
    public int value() {
        return bidAmount;
    }

    @Override
    public User getUser() {
        return user;
    }
    
    @Override
    public long getBidTime() {
    	return bidTime;
    }

    /**
     * A bid is only valid if its time is
     * before than the items expiry time and 
     * Item has not been won yet.
     * @param item the item we want to check
     * @return true iff (bid time is less than item expiry time and item has not been already won)
     */
    @Override
    public boolean isAValidBid(final Item item) {
        return (bidTime < item.getExpiryTimeInSec() && item.getWonBid() == null);
    }

    /**
     * Inform the observers when we execute a bid on
     * an item.
     */
    @Override
    public void execute(final Item item) {
    	if (bidAmount == item.getDesired()) item.setWonBid(this);
        setChanged();
        notifyObservers(item);
    }

    @Override
    public int compareTo(final Bid that) {
        return that.value() - this.value();
    }

    @Override
    public String toString() {
        return "Bid (" + bidAmount + ")";
    }
}