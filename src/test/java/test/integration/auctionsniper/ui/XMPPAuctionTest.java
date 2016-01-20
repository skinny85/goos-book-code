package test.integration.auctionsniper.ui;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.Main;
import auctionsniper.xmpp.XMPPAuction;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.endtoend.auctionsniper.ApplicationRunner;
import test.endtoend.auctionsniper.FakeAuctionServer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class XMPPAuctionTest {
    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    private XMPPConnection connection;

    @Before
    public void createConnection() throws XMPPException {
        connection = new XMPPConnection(FakeAuctionServer.XMPP_HOSTNAME);
        connection.connect();
        connection.login(ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD, Main.AUCTION_RESOURCE);
    }

    @Before
    public void startTheAuction() throws XMPPException {
        auctionServer.startSellingItem();
    }

    @After
    public void closeConnection() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    @After public void stopAuction() {
        auctionServer.stop();
    }

    @Test
    public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);
        Auction auction = new XMPPAuction(connection, auctionServer.getItemId());
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
        auction.join();
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();

        assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS));
    }

    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            public void currentPrice(int price, int increment, PriceSource priceSource) {
                // not implemented
            }
        };
    }
}
