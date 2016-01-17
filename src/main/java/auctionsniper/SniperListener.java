package auctionsniper;

public interface SniperListener {
    void sniperLost();

    void sniperStateChanged(SniperSnapshot sniperSnapshot);

    void sniperWinning();

    void sniperWon();
}
