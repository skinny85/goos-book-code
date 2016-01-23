package test.auctionsniper;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import auctionsniper.AuctionSniper;
import auctionsniper.SniperCollector;
import auctionsniper.SniperLauncher;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;

@RunWith(JMock.class)
public class SniperLauncherTest {
    private final Mockery context = new Mockery();
    private final States auctionState = context.states("auction state").startsAs("not joined");
    private final Auction auction = context.mock(Auction.class);
    private final AuctionHouse auctionHouse = context.mock(AuctionHouse.class);
    private final SniperCollector sniperCollector = context.mock(SniperCollector.class);
    private final SniperLauncher launcher = new SniperLauncher(auctionHouse, sniperCollector);

    @Test
    public void addsNewSniperToCollectorAndThenJoinsAuction() {
        final String itemId = "item 123";

        context.checking(new Expectations() {{
            allowing(auctionHouse).auctionFor(itemId);
            will(returnValue(auction));

            oneOf(auction).addAuctionEventListener(with(sniperForItem(itemId))); when(auctionState.is("not joined"));
            oneOf(sniperCollector).addSniper(with(sniperForItem(itemId))); when(auctionState.is("not joined"));

            one(auction).join(); then(auctionState.is("joined"));
        }});

        launcher.joinAuction(itemId);
    }

    protected Matcher<AuctionSniper> sniperForItem(String itemId) {
        return new FeatureMatcher<AuctionSniper, String>(equalTo(itemId), "sniper with itemId id", "itemId") {
            @Override
            protected String featureValueOf(AuctionSniper actual) {
                return actual.getSnapshot().itemId;
            }
        };
    }
}
