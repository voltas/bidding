package com.saurabh.apigee.bidding.models;

import com.saurabh.apigee.bidding.util.Immutable;
import com.sun.istack.internal.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.logging.Level;

/**
 * The BidUser represents the individual bidding in the
 * bidding.
 *
 * @author: Saurabh [1147am@gmail.com]
 */
@Immutable
public class BiddingUser implements User {

    private final String name;

    public BiddingUser(String name) {
        this.name = name;
    }

    /**
     * If we receive a notification from the Bid that is has executed
     * then we register the details here. This would then store the information in
     * some form of database (but that's outside the scope here).
     *
     * @param bid the observable
     * @param item the item the user is bidding on
     */
    @Override
    public void update(Observable bid, Object item) {
        System.out.print("Update database " +
                name +
                " executed a " +
                 bid +
                " on " +
                item +
                " at " +
                new SimpleDateFormat("MM/dd/yyyy h:mm:ss:S a").format(new Date()));
    }
}