package auctionsniper.ui;

import auctionsniper.SniperState;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

import static auctionsniper.Main.STATUS_JOINING;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
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

    public void sniperStatusChanged(SniperState sniperState, String statusText) {
        snipers.sniperStatusChanged(sniperState, statusText);
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
        private final static SniperState STARTING_UP = new SniperState("", 0, 0);

        private String statusText = STATUS_JOINING;
        private SniperState sniperState = STARTING_UP;

        public void setStatusText(String newStatusText) {
            statusText = newStatusText;
            fireTableRowsUpdated(0, 0);
        }

        public void sniperStatusChanged(SniperState newSniperState,
                                        String newStatusText) {
            sniperState = newSniperState;
            statusText = newStatusText;
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
                    return sniperState.itemId;
                case LAST_PRICE:
                    return sniperState.lastPrice;
                case LAST_BID:
                    return sniperState.lastBid;
                case SNIPER_STATUS:
                    return statusText;
                default:
                    throw new IllegalArgumentException("No column at " + columnIndex);
            }
        }
    }
}
