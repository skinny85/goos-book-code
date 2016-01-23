package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;

public class XMPPAuctionHouse implements AuctionHouse {
    public static XMPPAuctionHouse connect(String hostname, String username, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Auction auctionFor(String itemId) {
        throw new UnsupportedOperationException();
    }

    public void disconnect() {
        throw new UnsupportedOperationException();
    }
}
