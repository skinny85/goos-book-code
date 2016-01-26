package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import auctionsniper.Item;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.apache.commons.io.FilenameUtils.getFullPath;

public class XMPPAuctionHouse implements AuctionHouse {
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT =
            ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    public static final String LOG_FILE_NAME = "auction-sniper.log";
    private static final String LOGGER_NAME = "auction-sniper";

    public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPException, XMPPAuctionException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return new XMPPAuctionHouse(connection);
    }

    private final XMPPConnection connection;
    private final XMPPFailureReporter failureReporter;

    public XMPPAuctionHouse(XMPPConnection connection) throws XMPPAuctionException {
        this.connection = connection;
        this.failureReporter = new LoggingXMPPFailureReporter(makeLogger());
    }

    private Logger makeLogger() throws XMPPAuctionException {
        Logger logger = Logger.getLogger(XMPPAuctionHouse.LOGGER_NAME);
        logger.setUseParentHandlers(false);
        logger.addHandler(simpleFileHandler());
        return logger;
    }

    private FileHandler simpleFileHandler() throws XMPPAuctionException {
        try {
            FileHandler handler = new FileHandler(LOG_FILE_NAME);
            handler.setFormatter(new SimpleFormatter());
            return handler;
        } catch (Exception e) {
            throw new XMPPAuctionException("Could not create logger FileHandler " + getFullPath(LOG_FILE_NAME), e);
        }
    }

    @Override
    public Auction auctionFor(Item item) {
        return new XMPPAuction(connection, item, failureReporter);
    }

    public void disconnect() {
        connection.disconnect();
    }
}
