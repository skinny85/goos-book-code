package auctionsniper.ui;

import auctionsniper.SniperSnapshot;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

import static auctionsniper.SniperState.BIDDING;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String STATUS_JOINING = "Joining";
    private static final String SNIPERS_TABLE_NAME = "Snipers Table";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WINNING = "Winning";
    public static final String STATUS_WON = "Won";

    private final SnipersTableModel snipers = new SnipersTableModel();

    public MainWindow() {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void showStatus(String statusText) {
        snipers.setStatusText(statusText);
    }

    public void sniperStatusChanged(SniperSnapshot sniperSnapshot) {
        snipers.sniperStatusChanged(sniperSnapshot);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    public static class SnipersTableModel extends AbstractTableModel {
        private static String[] STATUS_TEXT = {
                MainWindow.STATUS_JOINING,
                MainWindow.STATUS_BIDDING,
                MainWindow.STATUS_WINNING
        };
        private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, BIDDING);

        private String statusText = STATUS_JOINING;
        private SniperSnapshot sniperSnapshot = STARTING_UP;

        public void setStatusText(String newStatusText) {
            statusText = newStatusText;
            fireTableRowsUpdated(0, 0);
        }

        public void sniperStatusChanged(SniperSnapshot newSniperSnapshot) {
            sniperSnapshot = newSniperSnapshot;
            statusText = STATUS_TEXT[newSniperSnapshot.state.ordinal()];
            fireTableRowsUpdated(0, 0);
        }

        public int getColumnCount() {
            return Column.values().length;
        }

        public int getRowCount() {
            return 1;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (Column.at(columnIndex)) {
                case ITEM_IDENTIFIER:
                    return sniperSnapshot.itemId;
                case LAST_PRICE:
                    return sniperSnapshot.lastPrice;
                case LAST_BID:
                    return sniperSnapshot.lastBid;
                case SNIPER_STATE:
                    return statusText;
                default:
                    throw new IllegalArgumentException("No column at " + columnIndex);
            }
        }
    }
}
