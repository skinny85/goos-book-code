package auctionsniper;

import auctionsniper.ui.MainWindow;
import auctionsniper.ui.MainWindow.SnipersTableModel;
import auctionsniper.xmpp.XMPPAuction;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Main{
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT =
            ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

    public static void main(String... args) throws Exception {
        Main main = new Main();
        XMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(connection);

        for (int i = 3; i < args.length; i++) {
            main.joinAuction(connection, args[i]);
        }
    }

    private static XMPPConnection connection(String hostname, String username, String password)
            throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId,
                connection.getServiceName());
    }

    private final SnipersTableModel snipers = new SnipersTableModel();
    private MainWindow ui;

    private List<Chat> notToBeGCd = new ArrayList<Chat>();

    public Main() throws Exception {
        startUserInterface();
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ui = new MainWindow(snipers);
            }
        });
    }

    private void joinAuction(XMPPConnection connection, String itemId)
            throws XMPPException {
        Chat chat = connection.getChatManager()
                .createChat(auctionId(itemId, connection), null);

        notToBeGCd.add(chat);

        Auction auction = new XMPPAuction(chat);
        chat.addMessageListener(
                new AuctionMessageTranslator(
                        connection.getUser(),
                        new AuctionSniper(
                                auction,
                                new SwingThreadSniperListener(snipers),
                                itemId)));
        auction.join();
    }

    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    public class SwingThreadSniperListener implements SniperListener {
        private final SniperListener sniperListener;

        public SwingThreadSniperListener(SniperListener sniperListener) {
            this.sniperListener = sniperListener;
        }

        @Override
        public void sniperStateChanged(final SniperSnapshot snapshot) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    sniperListener.sniperStateChanged(snapshot);
                }
            });
        }
    }
}
