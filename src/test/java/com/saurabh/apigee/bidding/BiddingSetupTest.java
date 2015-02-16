package com.saurabh.apigee.bidding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.saurabh.apigee.bidding.impl.BiddingImpl;
import com.saurabh.apigee.bidding.models.Bid;
import com.saurabh.apigee.bidding.models.BidTracker;
import com.saurabh.apigee.bidding.models.Bidding;
import com.saurabh.apigee.bidding.models.BiddingBid;
import com.saurabh.apigee.bidding.models.BiddingItem;
import com.saurabh.apigee.bidding.models.Item;
import com.saurabh.apigee.bidding.models.User;

import java.util.Observable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BiddingSetupTest {

   @Mock
   User user;

   @Mock
   Bid bid;

   @Mock
   BidTracker bidHistory;

   @Mock
   Bidding bidding;

   private final Item item = new BiddingItem("Item 1", 6500, 1500);

   @Spy
   @InjectMocks
   BiddingImpl biddingImpl = new BiddingImpl(bidding, bidHistory);

   @Test
   public void testCreateAuctionItemAndNamesMatch() {

       final String name = "Item 1";
       assertThat(item.getName(), equalTo(name));

   }

   @Test(expected = RuntimeException.class)
   public void testCreatingABidWithANegativeValueThrowsARuntimeException()  {

       new BiddingBid(-100, user);

   }

   @Test(expected = RuntimeException.class)
   public void testBidWithNullBidIsNotRegisteredAndThrowsARuntimeException() {

	   biddingImpl.bid(null, "Item 1");
       verify(bidHistory, times(0)).registerBid(any(Bid.class), any(Item.class));

   }

   @Test(expected = RuntimeException.class)
   public void testBidWithNullUserIsNotRegisteredAndThrowsARuntimeException() {

	   biddingImpl.bid(bid, "Item 1");
       verify(bidHistory, times(0)).registerBid(any(Bid.class), any(Item.class));

   }

   @Test(expected = RuntimeException.class)
   public void testBidWithNullItemNameIsNotRegisteredAndThrowsARuntimeException() {

	   biddingImpl.bid(bid, null);
       verify(bidHistory, times(0)).registerBid(any(Bid.class), any(Item.class));

   }

   @Test
   public void testASingleUserBidOnAnItemIsRegistered() {

       when(bid.getUser()).thenReturn(user);
       biddingImpl.bid(bid, "Item 1");
       verify(bidHistory, times(1)).registerBid(any(Bid.class), any(Item.class));

   }

   @Test
   public void testExecutingABidCausesTheUserToRegisterTheEvent() {

       Bid bid = spy(new BiddingBid(100, user));
       bid.execute(item);

       verify(user, times(1)).update(any(Observable.class),anyObject());

   }

    @Test
    public void testExecutingThreeBidsCausesTheUserToRegisterTheEventThreeTimes() {

        spy(new BiddingBid(100, user)).execute(item);
        spy(new BiddingBid(100, user)).execute(item);
        spy(new BiddingBid(100, user)).execute(item);

        verify(user, times(3)).update(any(Observable.class),anyObject());

    }


}