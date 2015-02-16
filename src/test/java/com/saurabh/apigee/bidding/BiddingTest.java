package com.saurabh.apigee.bidding;

import org.junit.Before;
import org.junit.Test;

import com.saurabh.apigee.bidding.impl.BidTrackerImpl;
import com.saurabh.apigee.bidding.impl.BiddingImpl;
import com.saurabh.apigee.bidding.models.Bid;
import com.saurabh.apigee.bidding.models.BidTracker;
import com.saurabh.apigee.bidding.models.Bidding;
import com.saurabh.apigee.bidding.models.BiddingBid;
import com.saurabh.apigee.bidding.models.BiddingItem;
import com.saurabh.apigee.bidding.models.BiddingUser;
import com.saurabh.apigee.bidding.models.Item;
import com.saurabh.apigee.bidding.models.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;

public class BiddingTest {

    private User user1;
    private User user2;
    private Bidding bidding = new Bidding();
    private BidTracker bidHistory = new BidTrackerImpl();

    Item ITEM1 = new BiddingItem("Item 1", 100, 60);
    Item ITEM2 = new BiddingItem("Item 2", 50, 600);
    Item ITEM3 = new BiddingItem("Item 3", 10, 400);
    Item ITEM4 = new BiddingItem("Item 4", 1000, 1500);
    Item ITEM5 = new BiddingItem("Item 5", 300, 400);
    

    BiddingImpl biddingImpl = new BiddingImpl(bidding, bidHistory);

    @Before
    public void before() {

        bidding.add(ITEM1);
        bidding.add(ITEM2);
        bidding.add(ITEM3);
        bidding.add(ITEM4);
        bidding.add(ITEM5);

        user1 = new BiddingUser("User 1");
        user2 = new BiddingUser("User 2");
    }
    
    @Test
    public void testItemWithExpiryMoreThanAnHourFailsRegisterForBidding() {
    	Item ITEM6 = new BiddingItem("Item 6", 300, 4000);
    	assertThat(biddingImpl.find("Item 6"), is(nullValue()));
    }

    @Test
    public void testFindASingleItemHasCorrectNameAndInitialValueAndReserveValue() {

        Item item = biddingImpl.find("Item 1");

        assertThat(item.getName(), equalTo("Item 1"));
        assertThat(item.getDesired(), equalTo(100));
    }

    @Test
    public void testTwoBidsWithEqualValueOnlyRegisterOnce() {

        Bid bid1 = new BiddingBid(101, user1);
        Bid bid2 = new BiddingBid(101, user1);
        biddingImpl.bid(bid1, ITEM1.getName());
        biddingImpl.bid(bid2, ITEM1.getName());

        assertThat(biddingImpl.getBidHistory().getAllItems(user1).size(), is(1));
        assertThat(biddingImpl.getBidHistory().getWinningBid(ITEM1), is(bid1));
    }

    @Test
    public void testRetrieveAllBidsForAnItem() {

        Bid bid1 = new BiddingBid(101, user1);
        Bid bid2 = new BiddingBid(102, user1);
        Bid bid3 = new BiddingBid(103, user1);

        biddingImpl.bid(bid1, ITEM1.getName());
        biddingImpl.bid(bid2, ITEM1.getName());
        biddingImpl.bid(bid3, ITEM1.getName());

        assertThat(biddingImpl.getBidHistory().getAllItems(user1).size(), is(3));
    }

    @Test
    public void WinningBidGivenMultipleBidsOnAnItem() {

        Bid bid1 = new BiddingBid(10, user1); // ok
        Bid bid2 = new BiddingBid(20, user1); // ok
        Bid bid3 = new BiddingBid(50, user1);  // ok
        Bid bid4 = new BiddingBid(70, user1); // expected
        Bid bid5 = new BiddingBid(20, user1); // no
        Bid bid6 = new BiddingBid(1, user1);   // no

        biddingImpl.bid(bid1, ITEM1.getName());
        biddingImpl.bid(bid2, ITEM1.getName());
        biddingImpl.bid(bid3, ITEM1.getName());
        biddingImpl.bid(bid4, ITEM1.getName());
        biddingImpl.bid(bid5, ITEM1.getName());
        biddingImpl.bid(bid6, ITEM1.getName());

        assertThat(biddingImpl.getBidHistory().getWinningBid(ITEM1), is(bid4));

        // there should only be four valid bids
        assertThat(biddingImpl.getBidHistory().getAllBids(ITEM1).size(), is(4));
    }
    
    @Test
    public void testWinningBidGivenMultipleBidOnAnItemIncludingOneBidWithDesiredAmount() {
    	Bid bid1 = new BiddingBid(10, user1); // ok
        Bid bid2 = new BiddingBid(20, user1); // ok
        Bid bid3 = new BiddingBid(100, user1);  // expected
        Bid bid4 = new BiddingBid(70, user1); // no
        
        biddingImpl.bid(bid1, ITEM1.getName());
        biddingImpl.bid(bid2, ITEM1.getName());
        biddingImpl.bid(bid3, ITEM1.getName());
        biddingImpl.bid(bid4, ITEM1.getName());
        
        assertThat(biddingImpl.getBidHistory().getWinningBid(ITEM1), is(bid3));
        
        // there should only be three valid bids
        assertThat(biddingImpl.getBidHistory().getAllBids(ITEM1).size(), is(3));
    }


}