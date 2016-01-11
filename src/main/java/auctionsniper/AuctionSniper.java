package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;

    public AuctionSniper(Auction auction, SniperListener sniperListener) {
        this.sniperListener = sniperListener;
    }

    public void auctionClosed() {
        sniperListener.sniperLost();
    }

    @Override
    public void currentPrice(int price, int increment) {
    }
}