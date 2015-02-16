package com.saurabh.apigee.bidding.models;


/**
 * The BiddingItem holds information on the item being sold such as
 * name, desired price, expiry time and winning bid.
 *
 * @author: Saurabh [1147am@gmail.com]
 */
public class BiddingItem implements Item {

    private final String name;
    private final int desired;
    private final int expiryInSec;
    private long expiryTimeInSec;
    public Bid wonBid = null;

    public BiddingItem(final String name, final int desired, final int expiryInSec) {
        this.name = name;
        this.desired = desired;
        this.expiryInSec = expiryInSec;
        this.expiryTimeInSec = (System.currentTimeMillis() / 1000L) + expiryInSec;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public int getDesired() {
        return desired;
    }
    
    @Override 
    public int getExpiryInSec() {
    	return expiryInSec;
    }
    
    @Override
    public long getExpiryTimeInSec() {
    	return expiryTimeInSec;
    }
    
	@Override
	public Bid getWonBid() {
		return wonBid;
	}
	
	@Override
	public void setWonBid(Bid bid) {
		wonBid = bid;
	}
	
	@Override
	public boolean isAValidItem() {
		return expiryInSec < 3600;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BiddingItem that = (BiddingItem) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name + 
        		" (desired: " + desired + 
        		", expiryInSec: " + expiryInSec + 
        		", expiryTimeInSec: " + expiryTimeInSec + 
        		")";
    }

}