package auctionsniper;

public interface SniperListener {
    void sniperLost();

    void sniperBidding(SniperSnapshot sniperSnapshot);

    void sniperWinning();

    void sniperWon();
}
