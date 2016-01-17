package auctionsniper.ui;

import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

import static auctionsniper.SniperState.JOINING;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    private static final String SNIPERS_TABLE_NAME = "Snipers Table";

    private final SnipersTableModel snipers = new SnipersTableModel();

    public MainWindow() {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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
        private static String[] STATUS_TEXT = {"Joining", "Bidding", "Winning", "Lost", "Won"};
        private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, JOINING);

        private SniperSnapshot sniperSnapshot = STARTING_UP;

        public void sniperStatusChanged(SniperSnapshot newSniperSnapshot) {
            sniperSnapshot = newSniperSnapshot;
            fireTableRowsUpdated(0, 0);
        }

        public int getColumnCount() {
            return Column.values().length;
        }

        public int getRowCount() {
            return 1;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return Column.at(columnIndex).valueIn(sniperSnapshot);
        }

        public static String textFor(SniperState state) {
            return STATUS_TEXT[state.ordinal()];
        }
    }
}
