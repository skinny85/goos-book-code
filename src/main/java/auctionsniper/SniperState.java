package auctionsniper;

public enum SniperState {
    JOINING,
    BIDDING,
    WINNING,
    LOST,
    WON;

    public SniperState whenAuctionClosed() {
        throw new UnsupportedOperationException();
    }
}
