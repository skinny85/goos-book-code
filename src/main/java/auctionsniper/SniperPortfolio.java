package auctionsniper;

import auctionsniper.util.Announcer;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class SniperPortfolio implements SniperCollector {
    public interface PortfolioListener extends EventListener {
        void sniperAdded(AuctionSniper sniper);
    }

    private final List<AuctionSniper> snipers = new ArrayList<AuctionSniper>();
    private final Announcer<PortfolioListener> announcer = Announcer.to(PortfolioListener.class);

    @Override
    public void addSniper(AuctionSniper sniper) {
        snipers.add(sniper);
        announcer.announce().sniperAdded(sniper);
    }

    public void addPortfolioListener(PortfolioListener listener) {
        announcer.addListener(listener);
    }
}
