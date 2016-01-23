package auctionsniper;

import auctionsniper.Main.SwingThreadSniperListener;
import auctionsniper.ui.SnipersTableModel;

import java.util.ArrayList;

public class SniperLauncher implements UserRequestListener {
    private final AuctionHouse auctionHouse;
    private final SnipersTableModel snipers;
    private final ArrayList<Auction> notToBeGCd = new ArrayList<Auction>();

    public SniperLauncher(AuctionHouse auctionHouse, SnipersTableModel snipers) {
        this.auctionHouse = auctionHouse;
        this.snipers = snipers;
    }

    @Override
    public void joinAuction(String itemId) {
        snipers.addSniper(SniperSnapshot.joining(itemId));

        Auction auction = auctionHouse.auctionFor(itemId);
        notToBeGCd.add(auction);
        AuctionSniper sniper = new AuctionSniper(
                auction,
                new SwingThreadSniperListener(snipers),
                itemId);
        auction.addAuctionEventListener(sniper);
        auction.join();
    }
}
